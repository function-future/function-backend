package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class used to manipulate Quiz Entity
 * Used QuizRepository and QuestionBankService to help manipulate quiz entity
 */
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

  /**
   * Used to find quiz from repository by passing the quiz id
   * @param id (String)
   * @return Quiz object
   */
  @Override
  public Quiz findById(String id) {
    return Optional.ofNullable(id)
            .filter(val -> !val.isEmpty())
            .map(quizRepository::findByIdAndDeletedFalse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .orElseThrow(() -> new NotFoundException("Quiz Not Found"));
  }

  /**
   * Used to find all quiz from repository in paging format by passing pageable, filter, and search parameter
   * @param pageable (Pageable)
   * @param filter (String)
   * @param search (String)
   * @return Page<Quiz> object
   */
  @Override
  public Page<Quiz> findAllByPageableAndFilterAndSearch(Pageable pageable, String filter, String search) {
      return quizRepository.findAllByDeletedFalse(pageable);
  }

  @Override
  public Quiz copyQuizWithTargetBatch(String targetBatch, Quiz quiz) {
      Batch batch = batchService.getBatchByCode(targetBatch);
    quiz = this.findById(quiz.getId());
      quiz = studentQuizService.copyQuizWithTargetBatch(batch, quiz);
      return quizRepository.save(quiz);
  }

  /**
   * Used to create new quiz in repository by passing the requested quiz entity object
   * @param request (Quiz)
   * @return Quiz object
   */
  @Override
  public Quiz createQuiz(Quiz request) {
    return Optional.of(request)
            .map(quiz -> {
                quiz.setBatch(batchService.getBatchByCode(quiz.getBatch().getCode()));
                quiz.setQuestionBanks(getQuestionBanksFromService(quiz.getQuestionBanks()));
                return quiz;
            })
            .map(quizRepository::save)
            .map(quiz -> studentQuizService.createStudentQuizByBatchCode(quiz.getBatch().getCode(), quiz))
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

    private List<QuestionBank> getQuestionBanksFromService(List<QuestionBank> questionBanks) {
        return questionBanks
                .stream()
                .map(QuestionBank::getId)
                .map(questionBankService::findById)
                .collect(Collectors.toList());
    }

  /**
   * Used to update existing quiz from repository by passing the requested quiz entity object with its id
   * @param request (Quiz)
   * @return Quiz object
   */
  @Override
  public Quiz updateQuiz(Quiz request) {
    return Optional.ofNullable(request)
            .map(Quiz::getId)
            .map(this::findById)
            .map(quiz -> {
              String requestedBatchCode = getRequestedBatchCode(request, quiz);
              checkAndEditBatchCodeByRequest(quiz, requestedBatchCode);
              BeanUtils.copyProperties(
                      request,
                      quiz,
                      FieldName.BaseEntity.CREATED_AT,
                      FieldName.BaseEntity.CREATED_BY,
                      FieldName.BaseEntity.VERSION
              );
              return quiz;
            })
            .map(quizRepository::save)
            .orElseThrow(() -> new BadRequestException("Bad Request"));
  }

  /**
   * Used to delete existing quiz from repository by passing the quiz id
   * @param id (String)
   */
  @Override
  public void deleteById(String id) {
    Optional.ofNullable(id)
            .map(this::findById)
            .ifPresent(val -> {
                String batchCode = val.getBatch().getCode();
                studentQuizService.deleteByBatchCodeAndQuiz(batchCode, val.getId());
              val.setDeleted(true);
              quizRepository.save(val);
            });
  }


    private String getRequestedBatchCode(Quiz request, Quiz quiz) {
    return Optional.ofNullable(request)
            .map(Quiz::getBatch)
            .map(Batch::getCode)
            .orElseGet(() -> quiz.getBatch().getCode());


  }

  private void checkAndEditBatchCodeByRequest(Quiz quiz, String requestedBatchCode) {
    if (!quiz.getBatch().getCode().equals(requestedBatchCode)) {
      studentQuizService.deleteByBatchCodeAndQuiz(quiz.getBatch().getCode(), quiz.getId());
      studentQuizService.createStudentQuizByBatchCode(requestedBatchCode, quiz);
    }
  }
}
