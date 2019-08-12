package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
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
import com.future.function.service.impl.helper.AuthorizationHelper;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentQuizServiceImpl implements StudentQuizService {

  private StudentQuizRepository studentQuizRepository;
  private StudentQuizDetailService studentQuizDetailService;
  private UserService userService;

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
            .filter(user -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentId,
                AuthorizationHelper.getScoringAllowedRoles()))
            .map(ignored -> studentQuizRepository.findAllByStudentIdAndDeletedFalse(studentId, pageable))
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

  @Override
  public StudentQuiz findById(String id, String userId) {
    User user = userService.getUser(userId);
    return Optional.ofNullable(id)
        .flatMap(studentQuizRepository::findByIdAndDeletedFalse)
            .filter(studentQuiz -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentQuiz.getStudent().getId(),
                AuthorizationHelper.getScoringAllowedRoles()))
            .orElseThrow(() -> new NotFoundException("Failed at #findById #StudentQuizService"));
  }

  @Override
  public List<StudentQuestion> findAllUnansweredQuestionByStudentQuizId(String studentQuizId, String userId) {
    return Optional.ofNullable(studentQuizId)
            .map(id -> this.updateStudentQuizTrialsAndReturnId(id, userId))
            .map(studentQuizDetailService::findAllUnansweredQuestionsByStudentQuizId)
            .orElseThrow(() -> new UnsupportedOperationException("EMPTY_TRIALS"));
  }

  private String updateStudentQuizTrialsAndReturnId(String studentQuizId, String userId) {
    return Optional.ofNullable(studentQuizId)
            .map(id -> this.findById(id, userId))
            .filter(studentQuiz -> studentQuiz.getTrials() > 0)
            .map(this::reduceStudentQuizTrials)
            .map(studentQuizRepository::save)
            .map(StudentQuiz::getId)
            .orElse(null);
  }

    @Override
    public Quiz updateQuizTrials(Quiz quiz) {
        return Optional.of(quiz)
                .map(Quiz::getBatch)
                .map(Batch::getCode)
                .map(userService::getStudentsByBatchCode)
                .map(students -> this.updateEveryStudentQuiz(quiz, students))
                .orElse(quiz);
    }

    private Quiz updateEveryStudentQuiz(Quiz quiz, List<User> students) {
        students.stream()
                .map(User::getId)
                .map(userId -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(userId, quiz.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(studentQuiz -> {
                    studentQuiz.setTrials(quiz.getTrials());
                    studentQuizRepository.save(studentQuiz);
                });
        return quiz;
    }

  private StudentQuiz reduceStudentQuizTrials(StudentQuiz studentQuiz) {
    studentQuiz.setTrials(studentQuiz.getTrials() - 1);
    return studentQuiz;
  }

  @Override
  public StudentQuizDetail answerQuestionsByStudentQuizId(String studentQuizId, String userId, List<StudentQuestion> answers) {
    StudentQuiz studentQuiz = this.findById(studentQuizId, userId);
    return Optional.of(userId)
        .map(userService::getUser)
            .filter(user -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentQuiz.getStudent().getId(), Role.STUDENT))
            .map(ignored -> studentQuizId)
            .map(id -> studentQuizDetailService.answerStudentQuiz(id, answers))
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #answerQuestionsByStudentQuizId #StudentQuizService"));
  }

  @Override
  public StudentQuiz createStudentQuizAndSave(User user, Quiz quiz) {
    return Optional.ofNullable(quiz)
            .map(quizObj -> toStudentQuizWithUserAndQuiz(user, quizObj))
        .map(studentQuizRepository::save)
        .map(this::createStudentQuizDetailAndSave)
        .orElseThrow(() -> new UnsupportedOperationException("Failed at #createStudentQuizAndSave #StudentQuizService"));
  }

  @Override
  public Quiz createStudentQuizByBatchCode(String batchCode, Quiz quiz) {
    return Optional.ofNullable(batchCode)
        .map(userService::getStudentsByBatchCode)
        .filter(userList -> !userList.isEmpty())
        .map(userList -> createStudentQuizFromUserList(quiz, userList))
        .map(ignored -> quiz)
        .orElseThrow(() -> new UnsupportedOperationException("Failed at#CreateStudentQuizByBatchCode #StudentQuizService"));
  }

  private StudentQuiz createStudentQuizDetailAndSave(StudentQuiz studentQuiz) {
    studentQuizDetailService.createStudentQuizDetail(studentQuiz, null);
    return studentQuiz;
  }

  @Override
  public Quiz copyQuizWithTargetBatch(Batch targetBatch, Quiz quiz) {
    return Optional.ofNullable(quiz)
        .map(this::createNewQuiz)
            .map(newQuiz -> setQuizTargetBatch(targetBatch, newQuiz))
            .map(newQuiz -> this.createStudentQuizByBatchCode(targetBatch.getCode(), newQuiz))
            .orElseThrow(() -> new UnsupportedOperationException("Failed at #copyQuizWithTargetBatch #StudentQuizService"));
  }

  private Quiz setQuizTargetBatch(Batch targetBatch, Quiz newQuiz) {
    newQuiz.setBatch(targetBatch);
    return newQuiz;
  }

  private Quiz createNewQuiz(Quiz quiz) {
    Quiz newQuiz = Quiz.builder().build();
    CopyHelper.copyProperties(quiz, newQuiz);
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
  public void deleteByBatchCodeAndQuiz(Quiz quiz) {
    userService.getStudentsByBatchCode(quiz.getBatch().getCode())
            .stream()
            .map(user -> this.findByStudentIdAndQuizIdAndDeletedFalse(user, quiz.getId()))
            .forEach(this::checkAndDeleteStudentQuiz);
  }

  private StudentQuiz findByStudentIdAndQuizIdAndDeletedFalse(User user, String quizId) {
    return Optional.ofNullable(user)
        .map(User::getId)
        .flatMap(userId -> studentQuizRepository.findByStudentIdAndQuizIdAndDeletedFalse(userId, quizId))
        .orElse(null);
  }

  private void checkAndDeleteStudentQuiz(StudentQuiz studentQuiz) {
    Optional.ofNullable(studentQuiz)
        .ifPresent(value -> {
          studentQuizDetailService.deleteByStudentQuiz(value);
          value.setDeleted(true);
          studentQuizRepository.save(value);
        });
  }

  private List<StudentQuiz> createStudentQuizFromUserList(Quiz quiz, List<User> userList) {
    return userList.stream()
            .map(user -> createStudentQuizAndSave(user, quiz))
        .collect(Collectors.toList());
  }

  private StudentQuiz toStudentQuizWithUserAndQuiz(User user, Quiz quiz) {
    return StudentQuiz
        .builder()
        .quiz(quiz)
            .student(user)
        .trials(quiz.getTrials())
        .done(false)
        .build();
  }

}
