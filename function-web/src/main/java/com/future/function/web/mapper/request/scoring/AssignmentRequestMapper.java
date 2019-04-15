package com.future.function.web.mapper.request.scoring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.exception.BadRequestException;
import com.future.function.common.validation.ObjectValidator;
import com.future.function.model.entity.feature.scoring.Assignment;
import com.future.function.web.model.request.scoring.AssignmentWebRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

  public Assignment toAssignment(String data) {

    AssignmentWebRequest request = toAssignmentWebRequest(data);

    return toValidatedAssignment(request);
  }

  private Assignment toValidatedAssignment(AssignmentWebRequest request) {
    Assignment assignment = new Assignment();
    BeanUtils.copyProperties(request, assignment);
    return validator.validate(assignment);
  }

  private AssignmentWebRequest toAssignmentWebRequest(String data) {
    try {
      return objectMapper.readValue(data, AssignmentWebRequest.class);
    } catch (IOException e) {
      log.error("IOException occurred on parsing request, exception: '{}'", e);
      throw new BadRequestException("Bad Request");
    }
  }
}
