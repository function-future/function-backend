package com.future.function.common;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectValidator {

  private final Validator validator;

  @Autowired
  public ObjectValidator(Validator validator) {

    this.validator = validator;
  }

  public <T> T validate(T data) {

    Set<ConstraintViolation<T>> violations = validator.validate(data);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<T> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
      }
      throw new ConstraintViolationException(sb.toString(), violations);
    } else {
      return data;
    }
  }

}
