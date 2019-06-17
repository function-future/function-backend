package com.future.function.web.mapper.request.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.scoring.AssignmentWebRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Bean class used to map AssignmentWebRequest from json string
 */
@Slf4j
@Component
public class AssignmentRequestMapper {

  private WebRequestMapper requestMapper;

    private RequestValidator validator;

  @Autowired
  public AssignmentRequestMapper(WebRequestMapper requestMapper, RequestValidator validator) {
    this.requestMapper = requestMapper;
    this.validator = validator;
  }

  /**
   * used to convert json in string format into a proper Assignment Object
   *
   * @param request (AssignmentWebRequest)
   * @return Assignment Object
   */
  public Assignment toAssignment(AssignmentWebRequest request) {
    return toValidatedAssignment(request);
  }

  /**
   * Used to convert json in string format and assignment id into a proper Assignment Object
   *
   * @param assignmentId (String)
   * @param request         (AssignmentWebRequest)
   * @return Assignment Object
   */
  public Assignment toAssignmentWithId(String assignmentId, AssignmentWebRequest request) {
    Assignment assignment = toValidatedAssignment(request);
    assignment.setId(assignmentId);
    return assignment;
  }

  /**
   * used to convert AssignmentWebRequest into a proper Assignment Object with validation to validate some pre-requisition
   *
   * @param request (AssignmentWebRequest)
   * @return valid Assignment Object
   */
  private Assignment toValidatedAssignment(AssignmentWebRequest request) {
    return Optional.of(request)
            .map(validator::validate)
            .map(val -> {
              Assignment assignment = Assignment.builder().build();
              BeanUtils.copyProperties(val, assignment, "id");
              assignment.setBatch(Batch.builder().code(request.getBatchCode()).build());
              assignment.setFile(toFileV2(request));
              return assignment;
            })
            .orElseGet(Assignment::new);
  }

  private FileV2 toFileV2(AssignmentWebRequest request) {
    return Optional.ofNullable(request)
            .map(AssignmentWebRequest::getFiles)
            .filter(files -> !files.isEmpty())
            .map(files -> files.get(0))
            .map(this::buildFileV2)
            .orElseGet(FileV2::new);
  }

  private FileV2 buildFileV2(String file) {
    return FileV2.builder().id(file).build();
  }
}
