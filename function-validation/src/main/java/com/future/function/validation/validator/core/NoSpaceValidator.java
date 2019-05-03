package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.NoSpace;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class NoSpaceValidator implements ConstraintValidator<NoSpace, String> {
  
  @Override
  public void initialize(NoSpace constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    
    return Optional.ofNullable(value)
      .filter(s -> !s.contains(" "))
      .isPresent();
  }
  
}
