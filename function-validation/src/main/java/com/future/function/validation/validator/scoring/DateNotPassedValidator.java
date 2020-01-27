package com.future.function.validation.validator.scoring;

import com.future.function.validation.annotation.scoring.DateNotPassed;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class DateNotPassedValidator implements ConstraintValidator<DateNotPassed, Long> {

  @Override
  public void initialize(DateNotPassed constraintAnnotation) {
    // No initialization needed
  }

  @Override
  public boolean isValid(Long value, ConstraintValidatorContext context) {
    return new Date().getTime() <= value;
  }
}
