package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.service.impl.helper.PageHelper;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentQuizServiceImpl implements StudentQuizService, Observer {

  private StudentQuizRepository studentQuizRepository;

  private StudentQuizDetailService studentQuizDetailService;

  private UserService userService;

  private QuizService quizService;

  @Autowired
  public StudentQuizServiceImpl(StudentQuizRepository studentQuizRepository,
      StudentQuizDetailService studentQuizDetailService,
      UserService userService, QuizService quizService) {
    this.studentQuizRepository = studentQuizRepository;
    this.studentQuizDetailService = studentQuizDetailService;
    this.userService = userService;
    this.quizService = quizService;

    this.quizService.addObserver(this);
  }

  @Override
  public List<StudentQuiz> findAllByStudentId(String studentId) {

    return Optional.of(studentId)
      .map(studentQuizRepository::findAllByStudentIdAndDeletedFalse)
      .orElseGet(ArrayList::new);
  }

  @Override
  public Page<StudentQuizDetail> findAllQuizByStudentId(String studentId, Pageable pageable) {

    return Optional.ofNullable(studentId)
      .map(userId -> studentQuizRepository.findAllByStudentIdAndDeletedFalse(userId, pageable))
      .map(this::toStudentQuizDetails)
      .map(list -> PageHelper.toPage(list, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }

  private List<StudentQuizDetail> toStudentQuizDetails(
    Page<StudentQuiz> studentQuizPage
  ) {

    return studentQuizPage.getContent().stream()
      .map(StudentQuiz::getId)
      .map(studentQuizDetailService::findLatestByStudentQuizId)
      .collect(Collectors.toList());
  }

  @Override
  public StudentQuiz findOrCreateByStudentIdAndQuizId(String studentId, String quizId) {

    return Optional.ofNullable(studentId)
      .flatMap(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
      .orElseGet(() -> this.createStudentQuiz(studentId, quizId));
  }

  private StudentQuiz createStudentQuiz(String studentId, String quizId) {
    User student = userService.getUser(studentId);
    return Optional.ofNullable(student)
        .map(currentStudent -> quizService.findById(quizId, currentStudent.getRole(), currentStudent.getBatch().getId()))
        .map(quiz -> toStudentQuiz(student, quiz))
        .map(studentQuizRepository::save)
        .orElseThrow(() -> new UnsupportedOperationException(
            "Failed at #createStudentQuizAndSave #StudentQuizService"));
  }

  private StudentQuiz toStudentQuiz(User user, Quiz quiz) {

    return StudentQuiz.builder()
        .quiz(quiz)
        .student(user)
        .trials(0)
        .done(false)
        .build();
  }

  @Override
  public List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(
    String studentId, String quizId
  ) {

    return Optional.ofNullable(studentId)
      .flatMap(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
      .map(this::updateTrialsAndReturn)
      .map(studentQuizDetailService::findAllUnansweredQuestionsByStudentQuizId)
      .orElseThrow(() -> new UnsupportedOperationException("EMPTY_TRIALS"));
  }

  private StudentQuiz updateTrialsAndReturn(
    StudentQuiz studentQuiz
  ) {

    return Optional.ofNullable(studentQuiz)
      .filter(this::validateTrials)
      .map(this::addTrials)
      .map(studentQuizRepository::save)
      .orElse(null);
  }

  private boolean validateTrials(StudentQuiz studentQuiz) {
    return studentQuiz.getTrials() < studentQuiz.getQuiz().getTrials();
  }

  private StudentQuiz addTrials(StudentQuiz studentQuiz) {

    studentQuiz.setTrials(studentQuiz.getTrials() + 1);
    return studentQuiz;
  }

  @Override
  public StudentQuizDetail answerQuestionsByStudentQuizId(
    String studentId, String quizId, List<StudentQuestion> answers
  ) {

    return Optional.ofNullable(studentId)
      .flatMap(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
      .map(StudentQuiz::getId)
      .map(studentQuizId -> studentQuizDetailService.answerStudentQuiz(studentQuizId, answers))
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #answerQuestionsByStudentQuizId #StudentQuizService"));
  }

  @Override
  public Long findTimeLimitByStudentQuiz(String studentId, String quizId) {
    return Optional.ofNullable(studentId)
        .flatMap(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
        .map(StudentQuiz::getQuiz)
        .map(Quiz::getTimeLimit)
        .orElseThrow(() -> new NotFoundException("NOT_FOUND"));
  }

  @Override
  public void deleteById(String id) {

    Optional.ofNullable(id)
      .flatMap(studentQuizRepository::findByIdAndDeletedFalse)
      .ifPresent(this::deleteAllDetailAndDelete);
  }

  @Override
  public void deleteByBatchCodeAndQuiz(Quiz quiz) {

    userService.getStudentsByBatchCode(quiz.getBatch()
                                         .getCode())
      .parallelStream()
      .forEach(user -> findAndDelete(user, quiz));
  }

  private void findAndDelete(User student, Quiz quiz) {

    Optional.ofNullable(student)
          .map(User::getId)
          .flatMap(studentId -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(studentId, quiz.getId()))
          .ifPresent(this::deleteAllDetailAndDelete);
  }

  private void deleteAllDetailAndDelete(StudentQuiz studentQuiz) {

    studentQuizDetailService.deleteByStudentQuiz(studentQuiz);
    studentQuiz.setDeleted(true);
    studentQuizRepository.save(studentQuiz);
  }

  @Override
  public void update(Observable o, Object arg) {

    if(arg instanceof Quiz) {
      this.deleteByBatchCodeAndQuiz((Quiz) arg);
    }
  }
}
