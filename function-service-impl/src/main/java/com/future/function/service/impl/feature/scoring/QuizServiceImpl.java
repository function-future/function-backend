package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl extends Observable implements QuizService {

  private QuizRepository quizRepository;

  private QuestionBankService questionBankService;

  private BatchService batchService;

  @Autowired
  public QuizServiceImpl(
    QuizRepository quizRepository, QuestionBankService questionBankService,
      BatchService batchService
  ) {

    this.quizRepository = quizRepository;
    this.questionBankService = questionBankService;
    this.batchService = batchService;
  }

  @Override
  public Quiz findById(String id, Role role, String sessionBatchId) {

    return Optional.ofNullable(id)
      .flatMap(quizRepository::findByIdAndDeletedFalse)
      .map(quiz -> this.validateStudentBatchWithQuiz(quiz, role, sessionBatchId))
      .orElseThrow(
        () -> new NotFoundException("Failed at #findById #QuizService"));
  }

  private Quiz validateStudentBatchWithQuiz(Quiz quiz, Role role, String sessionBatchId) {
    boolean isStudent = role.equals(Role.STUDENT);
    if(!isStudent)  {
      return quiz;
    } else if (sessionBatchId.equals(quiz.getBatch().getId())) {
      return quiz;
    } else {
      throw new ForbiddenException("User Not Allowed");
    }
  }

  @Override
  public Page<Quiz> findAllByBatchCodeAndPageable(
    String batchCode, Pageable pageable, Role role, String sessionBatchId, boolean deadline
  ) {

    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode)
      .map(batch -> this.validateStudentBatchWithBatch(batch, role, sessionBatchId))
      .map(batch -> this.getQuizPage(batch, pageable, deadline))
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  private Page<Quiz> getQuizPage(Batch batch, Pageable pageable, boolean deadline) {
    return Optional.of(batch)
        .filter(filter -> deadline)
        .map(currentBatch -> quizRepository
            .findAllByBatchAndDeletedFalseAndEndDateAfterOrderByEndDateDesc(currentBatch, getDateInLong(), pageable))
        .orElseGet(() -> quizRepository
            .findAllByBatchAndDeletedFalseAndEndDateBeforeOrderByEndDateAsc(batch, getDateInLong(), pageable));
  }

  private Long getDateInLong() {
    return LocalDate.now().atTime(23, 59)
        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }
  private Batch validateStudentBatchWithBatch(Batch batch, Role role, String sessionBatchId) {
    boolean isStudent = role.equals(Role.STUDENT);
    if(isStudent && sessionBatchId.equals(batch.getId())) {
      return batch;
    } else if (!isStudent) {
      return batch;
    } else {
      throw new ForbiddenException("User Not Allowed");
    }
  }

  @Override
  public Quiz copyQuizWithTargetBatchCode(String targetBatchCode, String quizId) {

    return Optional.ofNullable(quizId)
      .flatMap(quizRepository::findByIdAndDeletedFalse)
      .map(foundQuiz -> this.createQuiz(foundQuiz, targetBatchCode))
      .map(quizRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #copyQuizWithTargetBatchCode #QuizService"));
  }

  private Quiz createQuiz(Quiz quiz, String targetBatchCode) {
    Quiz newQuiz = Quiz.builder().build();
    Batch codeOnlyBatch = Batch.builder().code(targetBatchCode).build();
    CopyHelper.copyProperties(quiz, newQuiz);
    newQuiz.setBatch(codeOnlyBatch);
    return this.getBatchObjAndSetToQuiz(newQuiz);
  }

  @Override
  public Quiz createQuiz(Quiz request) {

    return Optional.ofNullable(request)
      .map(this::getBatchObjAndSetToQuiz)
      .map(this::setQuestionBankList)
      .map(quizRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed on #createQuiz #QuizService"));
  }

  private Quiz setQuestionBankList(Quiz quiz) {
    List<QuestionBank> idOnlyQuestionBanks = quiz.getQuestionBanks();
    List<QuestionBank> questionBankList = getQuestionBankList(idOnlyQuestionBanks);
    quiz.setQuestionBanks(questionBankList);
    return quiz;
  }

  private Quiz getBatchObjAndSetToQuiz(Quiz quiz) {
    Batch batchObj = batchService.getBatchByCode(quiz.getBatch().getCode());
    quiz.setBatch(batchObj);
    return quiz;
  }

  private List<QuestionBank> getQuestionBankList(
    List<QuestionBank> idOnlyQuestionBanks
  ) {

    return Optional.of(idOnlyQuestionBanks)
      .filter(this::isSelectedOrAll)
      .map(this::getSelectedQuestionBanks)
      .orElseGet(questionBankService::findAll);
  }

  private boolean isSelectedOrAll(List<QuestionBank> questionBankList) {
    return !questionBankList.get(0).getId().equals("ALL");
  }

  private List<QuestionBank> getSelectedQuestionBanks(
    List<QuestionBank> idOnlyQuestionBanks
  ) {

    return idOnlyQuestionBanks.stream()
      .map(QuestionBank::getId)
      .map(questionBankService::findById)
      .collect(Collectors.toList());
  }

  @Override
  public Quiz updateQuiz(Quiz requestedQuiz) {

    return Optional.ofNullable(requestedQuiz)
      .map(Quiz::getId)
      .flatMap(quizRepository::findByIdAndDeletedFalse)
      .map(quiz -> copyAttributes(requestedQuiz, quiz))
      .map(this::getBatchObjAndSetToQuiz)
      .map(this::setQuestionBankList)
      .map(quizRepository::save)
      .orElse(requestedQuiz);
  }

  private Quiz copyAttributes(Quiz requestedQuiz, Quiz quiz) {

    CopyHelper.copyProperties(requestedQuiz, quiz);
    return quiz;
  }

  @Override
  public void deleteById(String id) {

    Optional.ofNullable(id)
      .flatMap(quizRepository::findByIdAndDeletedFalse)
      .ifPresent(this::notifyObserversAndDeleteQuiz);
  }

  private void notifyObserversAndDeleteQuiz(Quiz quiz) {
    this.setChanged();
    this.notifyObservers(quiz);
    quiz.setDeleted(true);
    quizRepository.save(quiz);
  }

}
