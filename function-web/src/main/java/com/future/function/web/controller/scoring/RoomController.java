package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.scoring.RoomService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.RoomRequestMapper;
import com.future.function.web.mapper.response.scoring.RoomResponseMapper;
import com.future.function.web.model.request.scoring.RoomPointWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.scoring.RoomWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/assignments/{assignmentId}/room/{studentId}")
public class RoomController {

  private final FileProperties fileProperties;

  private RoomService roomService;

  private RoomRequestMapper requestMapper;

  @Autowired
  public RoomController(
      RoomService roomService, RoomRequestMapper roomRequestMapper, FileProperties fileProperties
  ) {

    this.roomService = roomService;
    this.fileProperties = fileProperties;
    this.requestMapper = roomRequestMapper;
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<RoomWebResponse> findOrCreateRoomByStudentIdAndAssignmentId(
    @PathVariable
      String assignmentId,
    @PathVariable
      String studentId,
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session
  ) {

    return RoomResponseMapper.toDataRoomWebResponse(
      roomService.findOrCreateByStudentIdAndAssignmentId(studentId, session.getUserId(), assignmentId),
      fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<RoomWebResponse> updateRoomScore(
    @PathVariable
       String assignmentId,
    @PathVariable
       String studentId,
    @RequestBody
      RoomPointWebRequest request,
    @WithAnyRole(roles = Role.MENTOR)
      Session session
  ) {
    request = requestMapper.validate(request);
    return RoomResponseMapper.toDataRoomWebResponse(
      roomService.giveScoreToRoomByStudentIdAndAssignmentId(studentId, session.getUserId(), assignmentId, request.getPoint()
      ), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteRoomByStudentId(
    @PathVariable
      String assignmentId,
    @PathVariable
      String studentId,
    @WithAnyRole(roles = Role.ADMIN)
      Session session
  ) {

    roomService.deleteRoomByStudentIdAndAssignmentId(studentId, assignmentId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
