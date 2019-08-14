package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

  private QuizRepository quizRepository;
  private StudentQuizService studentQuizService;
  private QuestionBankService questionBankService;
  private BatchService batchService;

  @Autowired
  public QuizServiceImpl(QuizRepository quizRepository,
      StudentQuizService studentQuizService,
      QuestionBankService questionBankService,
      BatchService batchService) {
    this.quizRepository = quizRepository;
    this.studentQuizService = studentQuizService;
    this.questionBankService = questionBankService;
    this.batchService = batchService;
  }

  @Override
  public Quiz findById(String id) {
    return Optional.ofNullable(id)
            .flatMap(quizRepository::findByIdAndDeletedFalse)
            .orElseThrow(() -> new NotFoundException("Failed at #findById #QuizService"));
  }

  @Override
  public Page<Quiz> findAllByBatchCodeAndPageable(String batchCode, Pageable pageable) {
    return Optional.ofNullable(batchCode)
            .map(batchService::getBatchByCode)
            .map(batch -> quizRepository.findAllByBatchAndDeletedFalse(batch, pageable))
            .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public Quiz copyQuizWithTargetBatchCode(String targetBatchCode, Quiz quiz) {
    Batch batch = batchService.getBatchByCode(targetBatchCode);
    return Optional.ofNullable(quiz)
            .map(Quiz::getId)
            .map(this::findById)
            .map(value -> studentQuizService.copyQuizWithTargetBatch(batch, value))
            .map(quizRepository::save)
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #copyQuizWithTargetBatchCode #QuizService"));
  }

  @Override
  public Quiz createQuiz(Quiz request) {
    return Optional.ofNullable(request)
            .map(this::setBatchAndQuestionBank)
            .map(quizRepository::save)
            .map(quiz -> studentQuizService.createStudentQuizByBatchCode(quiz.getBatch().getCode(), quiz))
            .orElseThrow(() -> new UnsupportedOperationException("Failed on #createQuiz #QuizService"));
  }

  private Quiz setBatchAndQuestionBank(Quiz quiz) {
    quiz.setBatch(batchService.getBatchByCode(quiz.getBatch().getCode()));
    quiz.setQuestionBanks(getQuestionBanksFromService(quiz.getQuestionBanks()));
    return quiz;
  }

  private List<QuestionBank> getQuestionBanksFromService(List<QuestionBank> questionBanks) {
    return Optional.of(questionBanks)
            .filter(questionBankList -> !questionBankList.get(0).getId().equals("ALL"))
            .map(this::getSelectedQuestionBanks)
            .orElseGet(questionBankService::findAll);
  }

  private List<QuestionBank> getSelectedQuestionBanks(List<QuestionBank> questionBanks) {
    return questionBanks
            .stream()
            .map(QuestionBank::getId)
            .map(questionBankService::findById)
            .collect(Collectors.toList());
  }

  @Override
  public Quiz updateQuiz(Quiz request) {
    return Optional.ofNullable(request)
        .map(Quiz::getId)
        .flatMap(quizRepository::findByIdAndDeletedFalse)
        .map(quiz -> updateStudentQuizTrialsIfChanged(request, quiz))
        .map(quiz -> copyRequestedQuizAttributes(request, quiz))
        .map(this::setBatchAndQuestionBank)
        .map(quizRepository::save)
        .orElse(request);
  }

  private Quiz updateStudentQuizTrialsIfChanged(Quiz request, Quiz quiz) {
    return Optional.of(request)
            .filter(currentQuiz -> currentQuiz.getTrials() != quiz.getTrials())
            .map(studentQuizService::updateQuizTrials)
            .map(ignored -> quiz)
            .orElse(quiz);
  }

  private Quiz copyRequestedQuizAttributes(Quiz request, Quiz quiz) {
    CopyHelper.copyProperties(request, quiz);
    return quiz;
  }

  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
            .flatMap(quizRepository::findByIdAndDeletedFalse)
            .ifPresent(this::deleteAllStudentQuizAndSaveDeletedQuiz);
  }

  private void deleteAllStudentQuizAndSaveDeletedQuiz(Quiz quiz) {
    studentQuizService.deleteByBatchCodeAndQuiz(quiz);
    quiz.setDeleted(true);
    quizRepository.save(quiz);
  }
}
