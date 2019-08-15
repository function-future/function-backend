package com.future.function.service.impl.feature.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Lazy
@Slf4j
public class ScoringMediatorServiceImpl implements ScoringMediatorService {

  private static final Pageable MAX_PAGEABLE = new PageRequest(
    0, Integer.MAX_VALUE);

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
      .filter(student -> student.getRole()
        .equals(Role.STUDENT))
      .map(User::getBatch)
      .map(Batch::getCode)
      .map(code -> this.findQuizAndAssignmentAndCreateForStudent(user, code,
                                                                 MAX_PAGEABLE
      ))
      .orElse(user);
  }

  private User findQuizAndAssignmentAndCreateForStudent(
    User user, String batchCode, Pageable pageable
  ) {

    quizService.findAllByBatchCodeAndPageable(batchCode, pageable)
      .getContent()
      .forEach(quiz -> {
        try {
          studentQuizService.createStudentQuizAndSave(user, quiz);
        } catch (Exception e) {
          log.info("ScoringMediatorException: {}", e.getMessage(), e);
        }
      });
    assignmentService.findAllByBatchCodeAndPageable(batchCode, pageable)
      .getContent()
      .forEach(
        assignment -> roomService.createRoomForUserAndSave(user, assignment));
    return user;
  }

}
