package com.future.function.validation;

import com.future.function.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class RequestValidator {

  private final Validator validator;

  @Autowired
  public RequestValidator(Validator validator) {

    this.validator = validator;
  }

  public <T> T validate(T data) {

    Set<ConstraintViolation<T>> violations = validator.validate(data);

    if (!violations.isEmpty()) {
      throw new BadRequestException(violations.toString(), violations);
    } else {
      return data;
    }
  }

}
