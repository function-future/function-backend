package com.future.function.web.controller.scoring;

import com.future.function.service.api.feature.scoring.AssignmentService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.AssignmentRequestMapper;
import com.future.function.web.mapper.response.scoring.AssignmentResponseMapper;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.scoring.AssignmentWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/scoring/assignments")
public class AssignmentController {

  private AssignmentService assignmentService;

  private AssignmentRequestMapper assignmentRequestMapper;

  @Autowired
  public AssignmentController(AssignmentService assignmentService, AssignmentRequestMapper assignmentRequestMapper) {
    this.assignmentService = assignmentService;
    this.assignmentRequestMapper = assignmentRequestMapper;
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<AssignmentWebResponse> findAllAssignment(
          @RequestParam int page,
          @RequestParam int size,
          @RequestParam(required = false) String filter,
          @RequestParam(required = false) String search
  ) {
    return AssignmentResponseMapper
            .toAssignmentsPagingResponse(
                    assignmentService
                            .findAllBuPageable(
                                    PageHelper
                                            .toPage(page, size)
                            )
            );
  }

  @ResponseStatus(value = HttpStatus.OK)
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AssignmentWebResponse> findAssignmentById(
          @PathVariable String id
  ) {
    return AssignmentResponseMapper
            .toAssignmentDataResponse(
                    assignmentService
                            .findById(id)
            );
  }

  @ResponseStatus(value = HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AssignmentWebResponse> createAssignment(
          @RequestParam String data,
          @RequestParam MultipartFile file
  ) {
    return AssignmentResponseMapper
            .toAssignmentDataResponse(
                    HttpStatus.CREATED,
                    assignmentService
                            .createAssignment(
                                    assignmentRequestMapper
                                            .toAssignment(data),
                                    file
                            )
            );
  }

  @ResponseStatus(value = HttpStatus.OK)
  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<AssignmentWebResponse> updateAssignment(
          @RequestParam String data,
          @RequestParam MultipartFile file
  ) {
    return AssignmentResponseMapper
            .toAssignmentDataResponse(
                    assignmentService
                            .updateAssignment(
                                    assignmentRequestMapper
                                            .toAssignment(data),
                                    file
                            )
            );
  }

  @ResponseStatus(value = HttpStatus.OK)
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteAssignmentById(
          @PathVariable String id
  ) {
    assignmentService.deleteById(id);
    return ResponseHelper
            .toBaseResponse(HttpStatus.OK);
  }

}
