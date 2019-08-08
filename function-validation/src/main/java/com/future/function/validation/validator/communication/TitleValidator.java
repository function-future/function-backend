package com.future.function.validation.validator.communication;

import com.future.function.validation.annotation.communication.Title;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<Title, String> {
  @Override
  public void initialize(Title constraintAnnotation) {
    // No initialization needed.
  }

  @Override
  public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
    return title == null || title.matches("^([A-Za-z0-9]+(( )[A-Za-z0-9]+)*)+$");
  }
}
