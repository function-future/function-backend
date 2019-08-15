package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.model.entity.feature.scoring.Quiz;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuiz;
import com.future.function.service.api.feature.scoring.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Lazy
@Slf4j
public class ScoringMediatorServiceImpl implements ScoringMediatorService {

    private static final Pageable MAX_PAGEABLE = new PageRequest(0, Integer.MAX_VALUE);

    @Autowired
    private QuizService quizService;

    @Autowired
    private StudentQuizService studentQuizService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private RoomService roomService;

    @Override
    public User createQuizAndAssignmentsByStudent(User user) {
        return Optional.ofNullable(user)
                .filter(student -> student.getRole().equals(Role.STUDENT))
                .map(User::getBatch)
                .map(Batch::getCode)
                .map(code -> this.findQuizAndAssignmentAndCreateForStudent(user, code, MAX_PAGEABLE))
                .map(pair -> this.createStudentQuizzesAndRooms(pair, user))
                .orElse(user);
    }

    private Pair<List<Quiz>, List<Assignment>> findQuizAndAssignmentAndCreateForStudent(User user, String batchCode, Pageable pageable) {
        List<Quiz> first = quizService.findAllByBatchCodeAndPageable(batchCode, pageable).getContent();
        List<Assignment> second = assignmentService
            .findAllByBatchCodeAndPageable(batchCode, pageable).getContent();
        return Pair.of(first, second);
    }

    private User createStudentQuizzesAndRooms(Pair<List<Quiz>, List<Assignment>> pair, User user) {
      pair.getFirst().forEach(quiz -> {
        try {
          studentQuizService.createStudentQuizAndSave(user, quiz);
        } catch (Exception e) {
          log.info("ScoringMediatorException: {}", e.getMessage(), e);
        }
      });
      pair.getSecond()
          .forEach(assignment -> roomService.createRoomForUserAndSave(user, assignment));
      return user;
    }

    private User deleteStudentQuizzesAndRooms(User user) {
      studentQuizService.findAllByStudentId(user.getId(), MAX_PAGEABLE, user.getId())
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

  @Override
  public User deleteQuizAndAssignmentsByStudent(User user) {
      return Optional.ofNullable(user)
          .map(this::deleteStudentQuizzesAndRooms)
          .orElse(user);
  }
}
