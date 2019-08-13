package com.future.function.validation.validator.core;

import com.future.function.common.data.core.UserData;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OnlyStudentCanHaveBatchAndUniversityValidator implements
  ConstraintValidator<OnlyStudentCanHaveBatchAndUniversity, UserData> {
  
  @Override
  public void initialize(
    OnlyStudentCanHaveBatchAndUniversity constraintAnnotation
  ) {
    // No initialization needed for this validator.
  }
  
  @Override
  public boolean isValid(UserData value, ConstraintValidatorContext context) {
  
    String role = value.getRole();
    
    if (role.equals("UNKNOWN")) {
      return false;
    }
  
    String batchNumber = value.getBatch();
    String university = value.getUniversity();
    
    if (role.equals("STUDENT")) {
      return batchNumber != null && university != null;
    } else {
      return batchNumber == null && university == null;
    }
  }
  
}
