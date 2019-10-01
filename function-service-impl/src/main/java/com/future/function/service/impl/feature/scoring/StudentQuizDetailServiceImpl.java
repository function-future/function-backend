package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizDetailRepository;
import com.future.function.service.api.feature.scoring.StudentQuestionService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import com.future.function.service.impl.helper.CopyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentQuizDetailServiceImpl implements StudentQuizDetailService {

  private StudentQuizDetailRepository studentQuizDetailRepository;

  private StudentQuestionService studentQuestionService;

  @Autowired
  public StudentQuizDetailServiceImpl(
    StudentQuizDetailRepository studentQuizDetailRepository,
    StudentQuestionService studentQuestionService
  ) {

    this.studentQuizDetailRepository = studentQuizDetailRepository;
    this.studentQuestionService = studentQuestionService;
  }

  @Override
  public StudentQuizDetail findLatestByStudentQuizId(String studentQuizId) {

    return Optional.ofNullable(studentQuizId)
      .flatMap(
        studentQuizDetailRepository::findTopByStudentQuizIdAndDeletedFalseOrderByCreatedAtDesc)
      .orElseThrow(() -> new NotFoundException(
        "Failed at #findLatestByStudentQuizId #StudentQuizDetailService"));
  }

  @Override
  public List<StudentQuestion> findAllUnansweredQuestionsByStudentQuizId(
    StudentQuiz studentQuiz
  ) {

    return Optional.ofNullable(studentQuiz)
      .map(StudentQuiz::getQuiz)
      .filter(quiz -> quiz.getEndDate() > new Date().getTime())
      .map(this::findAllQuestionsFromStudentQuestionService)
      .map(questionList -> createStudentQuestions(studentQuiz, questionList))
      .orElseThrow(() -> new UnsupportedOperationException("DEADLINE"));
  }

  private List<StudentQuestion> createStudentQuestions(
    StudentQuiz studentQuiz, List<Question> questionList
  ) {

    return Optional.ofNullable(studentQuiz)
      .map(this::createNewStudentQuizDetail)
      .map(
        studentQuizDetail -> studentQuestionService.createStudentQuestionsFromQuestionList(
          questionList, studentQuizDetail))
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #createStudentQuestions #StudentQuizDetailService"));
  }

  private List<Question> findAllQuestionsFromStudentQuestionService(Quiz quiz) {

    return studentQuestionService.findAllRandomQuestionsFromMultipleQuestionBank(
      quiz.getQuestionBanks(), quiz.getQuestionCount());
  }

  private StudentQuizDetail createNewStudentQuizDetail(StudentQuiz studentQuiz) {

    return Optional.ofNullable(studentQuiz)
      .map(this::initializeStudentQuizDetail)
      .map(studentQuizDetailRepository::save)
      .orElse(null);
  }

  private StudentQuizDetail initializeStudentQuizDetail(
    StudentQuiz studentQuiz
  ) {

    return StudentQuizDetail.builder()
      .studentQuiz(studentQuiz)
      .build();
  }

  @Override
  public StudentQuizDetail answerStudentQuiz(
    String studentQuizId, List<StudentQuestion> answers
  ) {

    StudentQuizDetail detail = this.findLatestByStudentQuizId(studentQuizId);
    return Optional.ofNullable(detail)
      .map(value -> mapToStudentQuestionsWithDetail(answers, value))
      .map(answerList -> studentQuestionService.postAnswerForAllStudentQuestion(
        answerList, detail.getId()))
      .map(point -> setStudentQuizDetailPoint(detail, point))
      .map(studentQuizDetailRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #answerStudentQuiz #StudentQuizDetailService"));
  }

  private StudentQuizDetail setStudentQuizDetailPoint(
    StudentQuizDetail detail, Integer point
  ) {

    detail.setPoint(point);
    return detail;
  }

  private List<StudentQuestion> mapToStudentQuestionsWithDetail(
    List<StudentQuestion> answers, StudentQuizDetail detail
  ) {

    return answers.stream()
      .map(
        answer -> setStudentQuestionStudentQuizDetailAttribute(detail, answer))
      .collect(Collectors.toList());
  }

  private StudentQuestion setStudentQuestionStudentQuizDetailAttribute(
    StudentQuizDetail detail, StudentQuestion answer
  ) {

    answer.setStudentQuizDetail(detail);
    return answer;
  }

  @Override
  public void deleteByStudentQuiz(StudentQuiz studentQuiz) {

    Optional.ofNullable(studentQuiz)
      .map(StudentQuiz::getId)
      .map(this::findLatestByStudentQuizId)
      .ifPresent(detail -> {
        studentQuestionService.deleteAllByStudentQuizDetailId(detail.getId());
        detail.setDeleted(true);
        studentQuizDetailRepository.save(detail);
      });
  }
}
