package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.feature.core.Batch;
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
import com.future.function.service.impl.helper.AuthorizationHelper;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
      .map(userService::getUser)
      .map(User::getId)
      .map(studentQuizRepository::findAllByStudentIdAndDeletedFalse)
      .orElseGet(ArrayList::new);
  }

  @Override
  public List<StudentQuizDetail> findAllQuizByStudentId(String studentId) {

    return Optional.ofNullable(studentId)
      .map(studentQuizRepository::findAllByStudentIdAndDeletedFalse)
      .map(this::mapStudentQuizzesToDetail)
      .orElseGet(ArrayList::new);
  }

  private List<StudentQuizDetail> mapStudentQuizzesToDetail(
    List<StudentQuiz> quizList
  ) {

    return quizList.stream()
      .map(StudentQuiz::getId)
      .map(studentQuizDetailService::findLatestByStudentQuizId)
      .collect(Collectors.toList());
  }

  @Override
  public StudentQuiz findOrCreateByStudentIdAndQuizId(String studentId, String quizId) {

    return Optional.ofNullable(studentId)
      .flatMap(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
      .filter(Objects::nonNull)
      .orElseGet(() -> createNewStudentQuiz(studentId, quizId));
  }

  private StudentQuiz createNewStudentQuiz(String studentId, String quizId) {
    Quiz quiz = quizService.findById(quizId);
    User student = userService.getUser(studentId);
    return this.createStudentQuizAndSave(student, quiz);
  }

  @Override
  public List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(
    String studentId, String quizId
  ) {

    return Optional.ofNullable(studentId)
      .flatMap(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
      .map(this::updateStudentQuizTrialsAndReturnId)
      .map(studentQuizDetailService::findAllUnansweredQuestionsByStudentQuizId)
      .orElseThrow(() -> new UnsupportedOperationException("EMPTY_TRIALS"));
  }

  private String updateStudentQuizTrialsAndReturnId(
    StudentQuiz studentQuiz
  ) {

    return Optional.ofNullable(studentQuiz)
      .filter(Objects::nonNull)
      .filter(currentStudentQuiz -> currentStudentQuiz.getTrials() < currentStudentQuiz.getQuiz().getTrials())
      .map(this::addStudentQuizTrials)
      .map(studentQuizRepository::save)
      .map(StudentQuiz::getId)
      .orElse(null);
  }

  private StudentQuiz addStudentQuizTrials(StudentQuiz studentQuiz) {

    studentQuiz.setTrials(studentQuiz.getTrials() + 1);
    return studentQuiz;
  }

  @Override
  public StudentQuizDetail answerQuestionsByStudentQuizId(
    String studentId, String quizId, List<StudentQuestion> answers
  ) {

    return Optional.of(studentId)
      .flatMap(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
      .filter(Objects::nonNull)
      .map(StudentQuiz::getId)
      .map(id -> studentQuizDetailService.answerStudentQuiz(id, answers))
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #answerQuestionsByStudentQuizId #StudentQuizService"));
  }

  private StudentQuiz createStudentQuizAndSave(User user, Quiz quiz) {

    return Optional.ofNullable(quiz)
      .map(quizObj -> toStudentQuizWithUserAndQuiz(user, quizObj))
      .map(studentQuizRepository::save)
      .orElseThrow(() -> new UnsupportedOperationException(
        "Failed at #createStudentQuizAndSave #StudentQuizService"));
  }

  @Override
  public void deleteById(String id) {

    Optional.ofNullable(id)
      .flatMap(studentQuizRepository::findByIdAndDeletedFalse)
      .ifPresent(quiz -> {
        quiz.setDeleted(true);
        studentQuizRepository.save(quiz);
      });
  }

  @Override
  public void deleteByBatchCodeAndQuiz(Quiz quiz) {

    userService.getStudentsByBatchCode(quiz.getBatch()
                                         .getCode())
      .parallelStream()
      .forEach(user -> findStudentQuizAndDelete(user, quiz));
  }

  private void findStudentQuizAndDelete(User student, Quiz quiz) {
    Optional.ofNullable(student)
          .map(User::getId)
          .flatMap(studentId -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(studentId, quiz.getId()))
          .ifPresent(this::deleteAllDetailAndSaveDeletedStudentQuiz);
  }

  private void deleteAllDetailAndSaveDeletedStudentQuiz(StudentQuiz value) {
    studentQuizDetailService.deleteByStudentQuiz(value);
    value.setDeleted(true);
    studentQuizRepository.save(value);
  }

  private StudentQuiz toStudentQuizWithUserAndQuiz(User user, Quiz quiz) {

    return StudentQuiz.builder()
      .quiz(quiz)
      .student(user)
      .trials(0)
      .done(false)
      .build();
  }

  @Override
  public void update(Observable o, Object arg) {
    if(arg instanceof Quiz) {
      this.deleteByBatchCodeAndQuiz((Quiz) arg);
    }
  }
}
