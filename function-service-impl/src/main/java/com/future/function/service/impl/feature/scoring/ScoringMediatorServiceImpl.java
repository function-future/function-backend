package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.service.api.feature.scoring.QuizService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.ScoringMediatorService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Lazy
@Slf4j
public class ScoringMediatorServiceImpl implements ScoringMediatorService {

  private static final Pageable MAX_PAGEABLE = new PageRequest(
    0, Integer.MAX_VALUE);

  private final QuizService quizService;

  private final StudentQuizService studentQuizService;

  private final AssignmentService assignmentService;

  private final RoomService roomService;

  @Autowired
  public ScoringMediatorServiceImpl(
      QuizService quizService, StudentQuizService studentQuizService,
      AssignmentService assignmentService, RoomService roomService
  ) {

    this.quizService = quizService;
    this.studentQuizService = studentQuizService;
    this.assignmentService = assignmentService;
    this.roomService = roomService;
  }

  @Override
  public User createQuizAndAssignmentsByStudent(User user) {

    return Optional.ofNullable(user)
      .filter(student -> student.getRole()
        .equals(Role.STUDENT))
      .map(User::getBatch)
      .map(Batch::getCode)
      .map(this::findQuizAndAssignmentAndCreateForStudent)
      .map(pair -> this.createStudentQuizzesAndRooms(pair, user))
      .orElse(user);
  }

  private Pair<List<Quiz>, List<Assignment>> findQuizAndAssignmentAndCreateForStudent(
    String batchCode
  ) {

    List<Quiz> first = quizService.findAllByBatchCodeAndPageable(
      batchCode, MAX_PAGEABLE)
      .getContent();
    List<Assignment> second = assignmentService.findAllByBatchCodeAndPageable(
      batchCode, MAX_PAGEABLE)
      .getContent();
    return Pair.of(first, second);
  }

  private User createStudentQuizzesAndRooms(
    Pair<List<Quiz>, List<Assignment>> pair, User user
  ) {

    pair.getFirst()
      .forEach(quiz -> {
        try {
          studentQuizService.createStudentQuizAndSave(user, quiz);
        } catch (Exception e) {
          log.info("ScoringMediatorException: {}", e.getMessage(), e);
        }
      });
    return user;
  }

  @Override
  public User deleteQuizAndAssignmentsByStudent(User user) {

    return Optional.ofNullable(user)
      .map(this::deleteStudentQuizzesAndRooms)
      .orElse(user);
  }

  private User deleteStudentQuizzesAndRooms(User user) {

    studentQuizService.findAllByStudentId(
      user.getId(), MAX_PAGEABLE, user.getId())
      .getContent()
      .stream()
      .map(StudentQuiz::getId)
      .forEach(studentQuizService::deleteById);
    roomService.findAllByStudentId(user.getId())
      .stream()
      .map(Room::getId)
      .forEach(roomService::deleteRoomById);
    return user;
  }

}
