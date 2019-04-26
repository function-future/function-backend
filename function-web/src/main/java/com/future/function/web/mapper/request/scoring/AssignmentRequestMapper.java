package com.future.function.web.mapper.request.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.web.model.request.scoring.AssignmentWebRequest;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Bean class used to map AssignmentWebRequest from json string
 */
@Slf4j
@Component
public class AssignmentRequestMapper {

  private ObjectMapper objectMapper;

  private ObjectValidator validator;

  @Autowired
  public AssignmentRequestMapper(ObjectMapper objectMapper, ObjectValidator validator) {
    this.objectMapper = objectMapper;
    this.validator = validator;
  }

  /**
   * used to convert json in string format into a proper Assignment Object
   *
   * @param data (JSON)
   * @return Assignment Object
   */
  public Assignment toAssignment(String data) {
    AssignmentWebRequest request = toAssignmentWebRequest(data);
    return toValidatedAssignment(request);
  }

  /**
   * Used to convert json in string format and assignment id into a proper Assignment Object
   *
   * @param assignmentId (String)
   * @param data         (JSON)
   * @return Assignment Object
   */
  public Assignment toAssignmentWithId(String assignmentId, String data) {
    AssignmentWebRequest request = toAssignmentWebRequest(data);
    request.setId(assignmentId);
    return toValidatedAssignment(request);
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
              Assignment assignment = new Assignment();
              BeanUtils.copyProperties(val, assignment);
              return assignment;
            })
            .orElse(new Assignment());
  }

  /**
   * used to convert json in string format to AssignmentWebRequest object with the help of ObjectMapper
   *
   * @param data (JSON)
   * @return AssignmentWebRequest object
   */
  private AssignmentWebRequest toAssignmentWebRequest(String data) {
    try {
      return objectMapper.readValue(data, AssignmentWebRequest.class);
    } catch (IOException e) {
      log.error("IOException occurred on parsing request, exception: '{}'", e);
      throw new BadRequestException("Bad Request");
    }
  }
}
