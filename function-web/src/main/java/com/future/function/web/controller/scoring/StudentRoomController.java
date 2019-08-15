package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.scoring.RoomResponseMapper;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.RoomWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scoring/batches/{batchCode}/assignments/{assignmentId" +
                "}/students/{studentId}/rooms")
public class StudentRoomController {

  private final FileProperties fileProperties;

  private AssignmentService assignmentService;

  @Autowired
  public StudentRoomController(
    AssignmentService assignmentService, FileProperties fileProperties
  ) {

    this.assignmentService = assignmentService;
    this.fileProperties = fileProperties;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<RoomWebResponse> findAllRoomsByStudentId(
    @PathVariable
      String studentId,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "10")
      int size,
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session
  ) {

    return RoomResponseMapper.toPagingRoomWebResponse(
      assignmentService.findAllRoomsByStudentId(studentId,
                                                PageHelper.toPageable(page,
                                                                      size
                                                ), session.getUserId()
      ), fileProperties.getUrlPrefix());
  }

}
