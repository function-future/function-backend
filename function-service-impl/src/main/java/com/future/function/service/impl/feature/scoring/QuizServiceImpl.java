package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.BadRequestException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.service.api.feature.scoring.QuestionService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

  private QuestionBankService questionBankService;

  private QuestionService questionService;

  private StudentQuizService studentQuizService;

  @Autowired
  public QuizServiceImpl(QuizRepository quizRepository,
                         QuestionBankService questionBankService,
                         QuestionService questionService,
                         StudentQuizService studentQuizService) {
    this.quizRepository = quizRepository;
    this.questionBankService = questionBankService;
    this.questionService = questionService;
    this.studentQuizService = studentQuizService;
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
    return quizRepository.findAll(pageable);
  }

  @Override
  public List<Question> findAllQuestionByMultipleQuestionBank(boolean random, String quizId) {
    Quiz quiz = Optional.ofNullable(quizId)
            .map(this::findById)
            .orElseThrow(() -> new NotFoundException("Quiz Not Found"));
    return Optional.of(quiz)
            .map(Quiz::getQuestionBanks)
            .filter(list -> !list.isEmpty())
            .map(this::getIdFromQuestionBanks)
            .map(questionService::findAllByMultipleQuestionBankId)
            .map(questionList -> getListOfQuestions(random, quiz, questionList))
            .orElseGet(ArrayList::new);
  }

  /**
   * Used to create new quiz in repository by passing the requested quiz entity object
   * @param request (Quiz)
   * @return Quiz object
   */
  @Override
  public Quiz createQuiz(Quiz request) {
    return Optional.of(request)
            .map(quizRepository::save)
            .map(quiz -> {
              studentQuizService.createStudentQuizByBatchCode(quiz.getBatch().getCode(), quiz);
              return quiz;
            })
            .orElseThrow(() -> new BadRequestException("Bad Request"));
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
              BeanUtils.copyProperties(
                      request,
                      quiz,
                      FieldName.BaseEntity.CREATED_AT,
                      FieldName.BaseEntity.CREATED_BY,
                      FieldName.BaseEntity.VERSION
              );
              checkAndEditBatchCodeByRequest(quiz, requestedBatchCode);
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
              val.setDeleted(true);
              quizRepository.save(val);
            });
  }

  private List<Question> getListOfQuestions(boolean random, Quiz quiz, List<Question> questionList) {
    if (random)
      Collections.shuffle(questionList);
    return questionList.subList(0, (quiz.getQuestionCount() - 1));
  }

  private List<String> getIdFromQuestionBanks(List<QuestionBank> list) {
    return list
            .stream()
            .map(QuestionBank::getId)
            .collect(Collectors.toList());
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
      studentQuizService.createStudentQuiz(quiz.getBatch().getCode(), quiz);
    }
  }
}
