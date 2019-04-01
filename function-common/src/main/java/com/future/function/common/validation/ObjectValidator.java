package com.future.function.common.validation;

import com.future.function.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ObjectValidator {
  
  private final Validator validator;
  
  @Autowired
  public ObjectValidator(Validator validator) {
    
    this.validator = validator;
  }
  
  /**
   * Generic method for validating incoming data of any class type which has
   * validation in/or it. If valid, the sent data is returned; otherwise
   * BadRequestException is thrown.
   * <p>
   *
   * @param data - Data of the specified class type ({@code T}).
   * @param <T>  - Type of class of the specified data.
   *
   * @return T - The sent data, as passed in the parameters.
   */
  public <T> T validate(T data) {
    
    Set<ConstraintViolation<T>> violations = validator.validate(data);
    
    if (!violations.isEmpty()) {
      throw new BadRequestException(violations.toString(), violations);
    } else {
      return data;
    }
  }
  
}
