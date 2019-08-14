package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.AssignmentRequestMapper;
import com.future.function.web.mapper.response.scoring.AssignmentResponseMapper;
import com.future.function.web.model.request.scoring.AssignmentWebRequest;
import com.future.function.web.model.request.scoring.CopyAssignmentWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.AssignmentWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/scoring/batches/{batchCode}/assignments")
public class AssignmentController {

  private AssignmentService assignmentService;
  private AssignmentRequestMapper assignmentRequestMapper;
  private FileProperties fileProperties;

  @Autowired
  public AssignmentController(AssignmentService assignmentService, AssignmentRequestMapper assignmentRequestMapper, FileProperties fileProperties) {
    this.assignmentService = assignmentService;
    this.assignmentRequestMapper = assignmentRequestMapper;
    this.fileProperties = fileProperties;
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<AssignmentWebResponse> findAllAssignment(
          @PathVariable String batchCode,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size,
          @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT}) Session session) {
    return AssignmentResponseMapper
        .toAssignmentsPagingResponse(
            assignmentService.findAllByBatchCodeAndPageable(batchCode, PageHelper.toPageable(page, size)), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AssignmentWebResponse> findAssignmentById(@PathVariable String id,
      @WithAnyRole(roles = {Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT})Session session) {
    return AssignmentResponseMapper.toAssignmentDataResponse(assignmentService.findById(id), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AssignmentWebResponse> createAssignment(@PathVariable String batchCode,
                                                              @RequestBody AssignmentWebRequest data,
                                                              @WithAnyRole(roles = Role.ADMIN) Session session) {
    return AssignmentResponseMapper
        .toAssignmentDataResponse(HttpStatus.CREATED, assignmentService
                .createAssignment(assignmentRequestMapper.toAssignment(data, batchCode)), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(path = "/copy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AssignmentWebResponse> copyAssignment(@RequestBody CopyAssignmentWebRequest request,
    @WithAnyRole(roles = Role.ADMIN) Session session) {
    request = assignmentRequestMapper.validateCopyAssignmentWebRequest(request);
    return AssignmentResponseMapper
        .toAssignmentDataResponse(HttpStatus.CREATED, assignmentService
                .copyAssignment(request.getAssignmentId(), request.getBatchCode()), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(value = HttpStatus.OK)
  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AssignmentWebResponse> updateAssignment(@PathVariable String batchCode, @PathVariable String id,
                                                              @RequestBody AssignmentWebRequest data,
                                                              @WithAnyRole(roles = Role.ADMIN) Session session) {
    return AssignmentResponseMapper.toAssignmentDataResponse(assignmentService
            .updateAssignment(assignmentRequestMapper.toAssignmentWithId(id, data, batchCode)), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteAssignmentById(@PathVariable String id, @WithAnyRole(roles = Role.ADMIN) Session session) {
    assignmentService.deleteById(id);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
