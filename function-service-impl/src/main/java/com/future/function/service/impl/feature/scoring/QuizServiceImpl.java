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
import com.future.function.service.impl.helper.SortHelper;
import com.future.function.session.model.Session;
import java.util.ArrayList;
import java.util.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
      .map(quiz -> this.validateStudentBatch(quiz, role, sessionBatchId))
      .orElseThrow(
        () -> new NotFoundException("Failed at #findById #QuizService"));
  }

  @Override
  public Page<Quiz> findAllByBatchCodeAndPageable(
    String batchCode, Pageable pageable, Role role, String sessionBatchId
  ) {

    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode)
      .map(batch -> this.validateStudentBatch(batch, role, sessionBatchId))
      .map(
        batch -> quizRepository.findAllByBatchAndDeletedFalseOrderByEndDateAsc(batch, pageable))
      .map(this::sortByClosestDeadline)
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  private Batch validateStudentBatch(Batch batch, Role role, String sessionBatchId) {
    boolean isStudent = role.equals(Role.STUDENT);
    if(isStudent && sessionBatchId.equals(batch.getId())) {
      return batch;
    } else if (!isStudent) {
      return batch;
    } else {
      throw new ForbiddenException("User Not Allowed");
    }
  }

  private Quiz validateStudentBatch(Quiz quiz, Role role, String sessionBatchId) {
    boolean isStudent = role.equals(Role.STUDENT);
    if(isStudent && sessionBatchId.equals(quiz.getBatch().getId())) {
      return quiz;
    } else if (!isStudent) {
      return quiz;
    } else {
      throw new ForbiddenException("User Not Allowed");
    }
  }

  private Page<Quiz> sortByClosestDeadline(Page<Quiz> quizPage) {
    List<Quiz> sortedQuiz = new ArrayList<>(quizPage.getContent());
    sortedQuiz.sort((quiz1, quiz2) -> SortHelper.compareClosestDeadline(quiz1.getEndDate(), quiz2.getEndDate()));
    return new PageImpl<>(sortedQuiz, new PageRequest(quizPage.getNumber(), quizPage.getSize()),
        quizPage.getTotalElements());
  }

  @Override
  public Quiz copyQuizWithTargetBatchCode(String targetBatchCode, String quizId) {

    return Optional.ofNullable(quizId)
      .flatMap(quizRepository::findByIdAndDeletedFalse)
      .map(currentQuiz -> this.initializeNewQuiz(currentQuiz, targetBatchCode))
      .map(quizRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #copyQuizWithTargetBatchCode #QuizService"));
  }

  private Quiz initializeNewQuiz(Quiz quiz, String targetBatchCode) {
    Quiz newQuiz = Quiz.builder().build();
    CopyHelper.copyProperties(quiz, newQuiz);
    newQuiz.setBatch(Batch.builder().code(targetBatchCode).build());
    return this.setBatch(newQuiz);
  }

  @Override
  public Quiz createQuiz(Quiz request) {

    return Optional.ofNullable(request)
      .map(this::setBatch)
      .map(this::setQuestionBank)
      .map(quizRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed on #createQuiz #QuizService"));
  }

  private Quiz setQuestionBank(Quiz quiz) {

    quiz.setQuestionBanks(getQuestionBanksFromService(quiz.getQuestionBanks()));
    return quiz;
  }

  private Quiz setBatch(Quiz quiz) {
    quiz.setBatch(batchService.getBatchByCode(quiz.getBatch().getCode()));
    return quiz;
  }

  private List<QuestionBank> getQuestionBanksFromService(
    List<QuestionBank> questionBanks
  ) {

    return Optional.of(questionBanks)
      .filter(questionBankList -> !questionBankList.get(0)
        .getId()
        .equals("ALL"))
      .map(this::getSelectedQuestionBanks)
      .orElseGet(questionBankService::findAll);
  }

  private List<QuestionBank> getSelectedQuestionBanks(
    List<QuestionBank> questionBanks
  ) {

    return questionBanks.stream()
      .map(QuestionBank::getId)
      .map(questionBankService::findById)
      .collect(Collectors.toList());
  }

  @Override
  public Quiz updateQuiz(Quiz request) {

    return Optional.ofNullable(request)
      .map(Quiz::getId)
      .flatMap(quizRepository::findByIdAndDeletedFalse)
      .map(quiz -> copyRequestedQuizAttributes(request, quiz))
      .map(this::setBatch)
      .map(this::setQuestionBank)
      .map(quizRepository::save)
      .orElse(request);
  }

  private Quiz copyRequestedQuizAttributes(Quiz request, Quiz quiz) {

    CopyHelper.copyProperties(request, quiz);
    return quiz;
  }

  @Override
  public void deleteById(String id) {

    Optional.ofNullable(id)
      .flatMap(quizRepository::findByIdAndDeletedFalse)
      .ifPresent(this::notifyStudentQuizServiceAndSaveDeletedQuiz);
  }

  private void notifyStudentQuizServiceAndSaveDeletedQuiz(Quiz quiz) {
    this.setChanged();
    this.notifyObservers(quiz);
    quiz.setDeleted(true);
    quizRepository.save(quiz);
  }

}
