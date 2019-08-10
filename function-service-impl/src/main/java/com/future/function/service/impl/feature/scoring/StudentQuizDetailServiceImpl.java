package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.scoring.*;
import com.future.function.repository.feature.scoring.StudentQuizDetailRepository;
import com.future.function.service.api.feature.scoring.StudentQuestionService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import com.future.function.service.impl.helper.CopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentQuizDetailServiceImpl implements StudentQuizDetailService {

  private StudentQuizDetailRepository studentQuizDetailRepository;
  private StudentQuestionService studentQuestionService;

  @Autowired
  public StudentQuizDetailServiceImpl(StudentQuizDetailRepository studentQuizDetailRepository,
      StudentQuestionService studentQuestionService) {
    this.studentQuizDetailRepository = studentQuizDetailRepository;
    this.studentQuestionService = studentQuestionService;
  }

  @Override
  public StudentQuizDetail findLatestByStudentQuizId(String studentQuizId) {
    return Optional.ofNullable(studentQuizId)
        .flatMap(studentQuizDetailRepository::findTopByStudentQuizIdAndDeletedFalseOrderByCreatedAtDesc)
            .orElseThrow(() -> new NotFoundException("Failed at #findLatestByStudentQuizId #StudentQuizDetailService"));
  }

  @Override
  public List<StudentQuestion> findAllQuestionsByStudentQuizId(String studentQuizId) {
    return Optional.ofNullable(studentQuizId)
        .map(this::findLatestByStudentQuizId)
        .map(StudentQuizDetail::getId)
        .map(studentQuestionService::findAllByStudentQuizDetailId)
            .orElseThrow(() -> new NotFoundException("Failed at #findAllQuestionsByStudentQuizId #StudentQuizDetailService"));
  }

  @Override
  public List<StudentQuestion> findAllUnansweredQuestionsByStudentQuizId(String studentQuizId) {
    return Optional.ofNullable(studentQuizId)
        .map(this::findLatestByStudentQuizId)
        .map(StudentQuizDetail::getStudentQuiz)
        .map(StudentQuiz::getQuiz)
            .filter(quiz -> quiz.getEndDate() > new Date().getTime())
            .map(this::findAllQuestionsFromStudentQuestionService)
            .map(questionList -> createStudentQuestions(studentQuizId, questionList))
            .orElseThrow(() -> new UnsupportedOperationException("DEADLINE"));
  }

    private List<StudentQuestion> createStudentQuestions(String studentQuizId, List<Question> questionList) {
        return Optional.ofNullable(studentQuizId)
                .map(this::createNewStudentQuizDetail)
                .map(studentQuizDetail -> studentQuestionService.createStudentQuestionsFromQuestionList(questionList, studentQuizDetail))
                .orElseThrow(() -> new UnsupportedOperationException("Failed at #createStudentQuestions #StudentQuizDetailService"));
    }

    private List<Question> findAllQuestionsFromStudentQuestionService(Quiz quiz) {
        return studentQuestionService.findAllRandomQuestionsFromMultipleQuestionBank(quiz.getQuestionBanks(),
                quiz.getQuestionCount());
    }

    private StudentQuizDetail createNewStudentQuizDetail(String studentQuizId) {
        return Optional.ofNullable(studentQuizId)
                .map(this::findLatestByStudentQuizId)
                .map(this::initializeStudentQuizDetail)
                .map(studentQuizDetailRepository::save)
                .orElse(null);
    }

    private StudentQuizDetail initializeStudentQuizDetail(StudentQuizDetail source) {
        StudentQuizDetail studentQuizDetail = StudentQuizDetail.builder().build();
        CopyHelper.copyProperties(source, studentQuizDetail);
        return studentQuizDetail;
    }

    @Override
  public StudentQuizDetail answerStudentQuiz(String studentQuizId, List<StudentQuestion> answers) {
    StudentQuizDetail detail = this.findLatestByStudentQuizId(studentQuizId);
    return Optional.ofNullable(detail)
            .map(value -> mapToStudentQuestionsWithDetail(answers, value))
            .map(answerList -> studentQuestionService.postAnswerForAllStudentQuestion(answerList, detail.getId()))
            .map(point -> setStudentQuizDetailPoint(detail, point))
            .map(studentQuizDetailRepository::save)
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #answerStudentQuiz #StudentQuizDetailService"));
    }

    private StudentQuizDetail setStudentQuizDetailPoint(StudentQuizDetail detail, Integer point) {
        detail.setPoint(point);
        return detail;
    }

    private List<StudentQuestion> mapToStudentQuestionsWithDetail(List<StudentQuestion> answers, StudentQuizDetail detail) {
        return answers.stream()
                .map(answer -> setStudentQuestionStudentQuizDetailAttribute(detail, answer))
                .collect(Collectors.toList());
  }

    private StudentQuestion setStudentQuestionStudentQuizDetailAttribute(StudentQuizDetail detail, StudentQuestion answer) {
        answer.setStudentQuizDetail(detail);
        return answer;
    }

    @Override
  public StudentQuizDetail createStudentQuizDetail(StudentQuiz studentQuiz, List<StudentQuestion> questions) {
    return Optional.ofNullable(studentQuiz)
        .map(this::toStudentQuizDetail)
            .map(studentQuizDetailRepository::save)
            .map(detail -> validateQuestionsAndCreateStudentQuestions(detail, questions))
        .orElseThrow(() -> new UnsupportedOperationException("create quiz failed"));
  }

    private StudentQuizDetail validateQuestionsAndCreateStudentQuestions(StudentQuizDetail studentQuizDetail,
                                                                         List<StudentQuestion> questions) {
    return Optional.ofNullable(questions)
        .map(questionList -> studentQuestionService.createStudentQuestionsByStudentQuizDetail(studentQuizDetail, questionList))
            .map(ignored -> studentQuizDetail)
            .orElse(studentQuizDetail);
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

  private StudentQuizDetail toStudentQuizDetail(StudentQuiz studentQuiz) {
    return StudentQuizDetail
        .builder()
        .studentQuiz(studentQuiz)
        .point(0)
        .build();
  }
}
