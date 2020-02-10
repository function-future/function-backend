package com.future.function.service.impl.feature.scoring;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Room;
import com.future.function.model.entity.feature.scoring.StudentQuizDetail;
import com.future.function.model.enums.scoring.ScoringType;
import com.future.function.model.vo.scoring.StudentSummaryVO;
import com.future.function.model.vo.scoring.SummaryVO;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ReportDetailService;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.service.api.feature.scoring.StudentQuizService;
import com.future.function.service.api.feature.scoring.SummaryService;
import com.future.function.service.impl.helper.AuthorizationHelper;
import com.future.function.service.impl.helper.PageHelper;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SummaryServiceImpl implements SummaryService {

  private RoomService roomService;

  private StudentQuizService studentQuizService;

  private UserService userService;

  @Autowired
  public SummaryServiceImpl(
    RoomService roomService, StudentQuizService studentQuizService,
    UserService userService
  ) {

    this.roomService = roomService;
    this.studentQuizService = studentQuizService;
    this.userService = userService;
  }

  @Override
  public StudentSummaryVO findAllPointSummaryByStudentId(
    String studentId, Pageable pageable, String userId, String type
  ) {

    return Optional.ofNullable(userId)
      .map(userService::getUser)
      .filter(
        user -> AuthorizationHelper.isUserAuthorizedForAccess(user, studentId,
                                                              AuthorizationHelper.getScoringAllowedRoles()
        ))
      .map(ignored -> studentId)
      .map(userService::getUser)
      .map(student -> this.getAllSummaryBasedOnType(student, pageable, type))
      .map(this::mapToStudentSummaryDTO)
      .orElseThrow(() -> new NotFoundException(
        "Failed at #findAllPointSummaryByStudentId #SummaryService"));
  }

  private StudentSummaryVO mapToStudentSummaryDTO(
    Pair<User, Page<SummaryVO>> pair
  ) {

    return StudentSummaryVO.builder()
      .studentId(pair.getFirst()
                   .getId())
      .studentName(pair.getFirst()
                     .getName())
      .batchCode(pair.getFirst()
                   .getBatch()
                   .getCode())
      .university(pair.getFirst()
                    .getUniversity())
      .avatar(getNullableUserPicture(pair.getFirst()))
      .scores(pair.getSecond())
      .totalPoint(getTotalPointFromSummaryVOList(pair.getSecond().getContent()))
      .build();
  }

  private Integer getTotalPointFromSummaryVOList(
    List<SummaryVO> summaryVOList
  ) {

    return summaryVOList.stream()
      .map(SummaryVO::getPoint)
      .reduce(0, Integer::sum);
  }

  private String getNullableUserPicture(User user) {

    return Optional.ofNullable(user)
      .map(User::getPictureV2)
      .map(FileV2::getFileUrl)
      .orElseGet(String::new);
  }

  private Pair<User, Page<SummaryVO>> getAllSummaryBasedOnType(
    User student, Pageable pageable, String type
  ) {
    Page<SummaryVO> summaryPage = PageHelper.empty(pageable);

    if(type.toUpperCase().equals(ScoringType.QUIZ.name())) {

      summaryPage = getStudentQuizzes(student, pageable);

    } else if(type.toUpperCase().equals(ScoringType.ASSIGNMENT.name())) {

      summaryPage = getStudentRooms(student, pageable);
    }
    return Pair.of(student, summaryPage);
  }

  private Page<SummaryVO> getStudentQuizzes(
    User user, Pageable pageable
  ) {
    List<SummaryVO> resultList = studentQuizService.findAllQuizByStudentId(user.getId(), pageable)
      .getContent()
      .stream()
      .map(this::mapQuizToSummaryDTO)
      .collect(Collectors.toList());
    return PageHelper.toPage(resultList, pageable);
  }

  private SummaryVO mapQuizToSummaryDTO(StudentQuizDetail studentQuizDetail) {

    return SummaryVO.builder()
      .title(studentQuizDetail.getStudentQuiz()
               .getQuiz()
               .getTitle())
      .point(studentQuizDetail.getPoint())
      .type(ScoringType.QUIZ.getType())
      .build();
  }

  private Page<SummaryVO> getStudentRooms(
    User user, Pageable pageable
  ) {
    List<SummaryVO> resultList = roomService.findAllByStudentId(user.getId(), pageable)
      .getContent().stream()
      .map(this::mapRoomToSummaryDTO)
      .collect(Collectors.toList());
    return PageHelper.toPage(resultList, pageable);
  }

  private SummaryVO mapRoomToSummaryDTO(Room room) {

    return SummaryVO.builder()
      .title(room.getAssignment()
               .getTitle())
      .point(room.getPoint())
      .type(ScoringType.ASSIGNMENT.getType())
      .build();
  }

}
