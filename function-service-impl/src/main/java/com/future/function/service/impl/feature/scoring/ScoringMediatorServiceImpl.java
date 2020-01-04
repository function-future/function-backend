package com.future.function.service.impl.feature.scoring;

import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.ScoringMediatorService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ScoringMediatorServiceImpl implements ScoringMediatorService, Observer {

  private final StudentQuizService studentQuizService;

  private final RoomService roomService;

  private final UserService userService;

  private final ExecutorService executorService;

  @Autowired
  public ScoringMediatorServiceImpl(StudentQuizService studentQuizService, RoomService roomService,
      UserService userService, ExecutorService executorService
  ) {
    this.studentQuizService = studentQuizService;
    this.roomService = roomService;
    this.userService = userService;
    this.executorService = executorService;
    this.userService.addObserver(this);
  }

  @Override
  public User deleteQuizAndAssignmentsByStudent(User user) {

    return Optional.ofNullable(user)
      .map(this::deleteStudentQuizzesAndRooms)
      .orElse(user);
  }

  private User deleteStudentQuizzesAndRooms(User user) {

    CompletableFuture.runAsync(() -> deleteEveryQuiz(user), executorService);
    CompletableFuture.runAsync(() -> deleteEveryAssignment(user), executorService);
    return user;
  }

  private void deleteEveryAssignment(User user) {

    roomService.findAllByStudentId(user.getId(), new PageRequest(0, Integer.MAX_VALUE))
      .getContent()
      .stream()
      .map(Room::getId)
      .forEach(roomService::deleteRoomById);
  }

  private void deleteEveryQuiz(User user) {

    studentQuizService.findAllByStudentId(user.getId())
      .stream()
      .map(StudentQuiz::getId)
      .forEach(studentQuizService::deleteById);
  }

  @Override
  public void update(Observable o, Object arg)
  {
      if(arg instanceof User) {
        this.deleteQuizAndAssignmentsByStudent((User) arg);
      }
  }
}
