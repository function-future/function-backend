package com.future.function.validation.validator.scoring;

import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.validation.annotation.scoring.AssignmentMustExist;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMustExistValidator implements ConstraintValidator<AssignmentMustExist, String> {

  @Autowired
  private AssignmentRepository assignmentRepository;

  @Override
  public void initialize(AssignmentMustExist constraintAnnotation) {
    //No Initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return assignmentRepository.existsByIdAndDeletedFalse(value);
  }
}
