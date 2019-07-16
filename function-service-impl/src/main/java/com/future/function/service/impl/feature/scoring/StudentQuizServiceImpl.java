package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.StudentQuestion;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.repository.feature.scoring.StudentQuizRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.StudentQuizDetailService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentQuizServiceImpl implements StudentQuizService {

  private StudentQuizRepository studentQuizRepository;
  private UserService userService;
  private StudentQuizDetailService studentQuizDetailService;

  @Autowired
  public StudentQuizServiceImpl(StudentQuizRepository studentQuizRepository, UserService userService,
      StudentQuizDetailService studentQuizDetailService) {
    this.studentQuizRepository = studentQuizRepository;
    this.userService = userService;
    this.studentQuizDetailService = studentQuizDetailService;
  }

  @Override
  public Page<StudentQuiz> findAllByStudentId(String studentId, Pageable pageable, String userId) {
    return Optional.of(userId)
        .map(userService::getUser)
        .map(user -> checkUserEligibilityAndReturnStudentId(user, studentId))
        .map(id -> studentQuizRepository.findAllByStudentIdAndDeletedFalse(studentId, pageable))
        .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public List<StudentQuizDetail> findAllQuizByStudentId(String studentId) {
    return Optional.ofNullable(studentId)
            .map(studentQuizRepository::findAllByStudentIdAndDeletedFalse)
            .map(this::mapStudentQuizzesToDetail)
            .orElseGet(ArrayList::new);
  }

  private List<StudentQuizDetail> mapStudentQuizzesToDetail(List<StudentQuiz> quizList) {
    return quizList.stream()
            .map(StudentQuiz::getId)
            .map(studentQuizDetailService::findLatestByStudentQuizId)
            .collect(Collectors.toList());
  }

  private String checkUserEligibilityAndReturnStudentId(User user, String studentId) {
    return Optional.of(user)
        .filter(value -> value.getRole().equals(Role.STUDENT))
        .map(value -> isUserIdEqualStudentId(studentId, value))
        .map(User::getId)
        .orElse(studentId);
  }

  private User isUserIdEqualStudentId(String studentId, User value) {
    if (value.getId().equals(studentId))
      return value;
    throw new ForbiddenException("Failed at #isUserIdEqualsStudentId #StudentQuizService");
  }

  @Override
  public StudentQuiz findById(String id, String userId) {
    User user = userService.getUser(userId);
    return Optional.ofNullable(id)
        .flatMap(studentQuizRepository::findByIdAndDeletedFalse)
        .map(studentQuiz -> {
          checkUserEligibilityAndReturnStudentId(user, studentQuiz.getStudent().getId());
          return studentQuiz;
        })
            .orElseThrow(() -> new NotFoundException("Failed at #findById #StudentQuizService"));
  }

  @Override
  public List<StudentQuestion> findAllQuestionsByStudentQuizId(String studentQuizId, String userId) {
    return Optional.of(studentQuizId)
        .flatMap(studentQuizRepository::findByIdAndDeletedFalse)
        .map(StudentQuiz::getStudent)
        .map(user -> checkUserEligibilityAndReturnStudentId(user, userId))
        .map(id -> studentQuizDetailService.findAllQuestionsByStudentQuizId(studentQuizId))
        .orElseGet(ArrayList::new);
  }

  @Override
  public List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(String studentQuizId, String userId) {
    StudentQuiz studentQuiz = findAndUpdateTrials(studentQuizId, userId);
    return Optional.of(studentQuiz)
        .filter(value -> value.getTrials() > 0)
        .map(val -> studentQuizDetailService.findAllUnansweredQuestionsByStudentQuizId(val.getId()))
        .orElseGet(ArrayList::new);
  }

  private StudentQuiz findAndUpdateTrials(String studentQuizId, String userId) {
    StudentQuiz studentQuiz = this.findById(studentQuizId, userId);
    if(studentQuiz.getTrials() > 0) {
      studentQuiz.setTrials(studentQuiz.getTrials() - 1);
      return studentQuizRepository.save(studentQuiz);
    }
    return studentQuiz;
  }

  @Override
  public StudentQuizDetail answerQuestionsByStudentQuizId(String studentQuizId, String userId, List<StudentQuestion> answers) {
    StudentQuiz studentQuiz = this.findById(studentQuizId, userId);
    return Optional.of(userId)
        .map(userService::getUser)
        .filter(user -> user.getRole().equals(Role.STUDENT))
            .map(user -> this.checkUserEligibilityAndReturnStudentId(user, studentQuiz.getStudent().getId()))
        .map(id -> studentQuizDetailService.answerStudentQuiz(studentQuizId, answers))
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #answerQuestionsByStudentQuizId #StudentQuizService"));
  }

  @Override
  public StudentQuiz createStudentQuizAndSave(String userId, Quiz quiz) {
    return Optional.ofNullable(quiz)
        .map(quizObj -> toStudentQuizWithUserAndQuiz(userId, quizObj))
        .map(studentQuizRepository::save)
        .orElseThrow(() -> new UnsupportedOperationException("Create quiz failed"));
  }

  @Override
  public Quiz createStudentQuizByBatchCode(String batchCode, Quiz quiz) {
    return Optional.ofNullable(batchCode)
        .map(userService::getStudentsByBatchCode)
        .filter(userList -> !userList.isEmpty())
        .map(userList -> createStudentQuizFromUserList(quiz, userList))
        .map(studentQuiz -> studentQuiz.get(0))
        .map(this::createStudentQuizDetailAndSave)
        .map(returnValue -> quiz)
            .orElseThrow(() -> new UnsupportedOperationException("Failed at#CreateStudentQuizByBatchCode #StudentQuizService"));
  }

  private StudentQuizDetail createStudentQuizDetailAndSave(StudentQuiz studentQuiz) {
    return studentQuizDetailService.createStudentQuizDetail(studentQuiz, null);
  }

  @Override
  public Quiz copyQuizWithTargetBatch(Batch targetBatch, Quiz quiz) {
    return Optional.ofNullable(quiz)
        .map(this::createNewQuiz)
        .map(newQuiz -> {
          newQuiz.setBatch(targetBatch);
          return newQuiz;
        })
        .map(newQuiz -> this.createStudentQuizByBatchCode(targetBatch.getCode(), newQuiz))
        .orElseThrow(() -> new UnsupportedOperationException("Copy Quiz Failed"));
  }

  private Quiz createNewQuiz(Quiz quiz) {
    Quiz newQuiz = Quiz.builder().build();
    CopyHelper.copyProperties(quiz, newQuiz);
    newQuiz.setId(UUID.randomUUID().toString());
    return newQuiz;
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
  public void deleteByBatchCodeAndQuiz(String batchCode, String quizId) {
    List<User> userList = userService.getStudentsByBatchCode(batchCode);
    userList.stream()
        .map(User::getId)
        .map(id -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(id, quizId))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .forEach(this::checkAndDeleteStudentQuiz);
  }

  private void checkAndDeleteStudentQuiz(StudentQuiz studentQuiz) {
    studentQuiz.setDeleted(true);
    studentQuizRepository.save(studentQuiz);
    studentQuizDetailService.deleteByStudentQuiz(studentQuiz);
  }

  private List<StudentQuiz> createStudentQuizFromUserList(Quiz quiz, List<User> userList) {
    return userList.stream()
        .map(User::getId)
        .map(userId -> createStudentQuizAndSave(userId, quiz))
        .collect(Collectors.toList());
  }

  private StudentQuiz toStudentQuizWithUserAndQuiz(String userId, Quiz quiz) {
    return StudentQuiz
        .builder()
        .quiz(quiz)
        .student(userService.getUser(userId))
        .trials(quiz.getTrials())
        .done(false)
        .build();
  }

}
