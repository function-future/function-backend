package com.future.function.validation;

import com.future.function.common.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Validator class which functions to validate objects (requests) going in to
 * the application, especially those going to the service layer.
 */
@Component
public class RequestValidator {
  
  private final Validator validator;
  
  @Autowired
  public RequestValidator(Validator validator) {
    
    this.validator = validator;
  }
  
  /**
   * Generic method for validating incoming data of any class type which has
   * validation in/on it. If valid, the sent data is returned; otherwise
   * {@link BadRequestException} is thrown.
   * <p>
   *
   * @param data Data of the specified class type ({@code T}).
   * @param <T>  Type of class of the specified data.
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
