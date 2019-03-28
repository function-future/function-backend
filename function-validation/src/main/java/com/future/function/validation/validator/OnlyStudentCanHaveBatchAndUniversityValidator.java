package com.future.function.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.future.function.common.UserData;
import com.future.function.validation.annotation.OnlyStudentCanHaveBatchAndUniversity;

public class OnlyStudentCanHaveBatchAndUniversityValidator implements ConstraintValidator<OnlyStudentCanHaveBatchAndUniversity, UserData> {

  @Override
  public void initialize(OnlyStudentCanHaveBatchAndUniversity constraintAnnotation) {}

  @Override
  public boolean isValid(UserData value, ConstraintValidatorContext context) {

    String role = value.getRoleAsString();
    Long batchNumber = value.getBatchNumber();
    String address = value.getUniversity();

    if (role.equals("STUDENT")) {
      return batchNumber != null && address != null;
    } else {
      return batchNumber == null && address == null;
    }
  }

}
