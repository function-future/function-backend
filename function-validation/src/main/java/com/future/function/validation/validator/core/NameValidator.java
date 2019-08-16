package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<Name, String> {

  @Override
  public void initialize(Name constraintAnnotation) {
    // No initialization needed for this validator.
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    return (value != null) && (value.matches("^([A-Za-z]+(( )[A-Za-z]+)*)+$"));
  }

}
