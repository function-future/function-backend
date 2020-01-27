package com.future.function.validation.validator.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.validation.annotation.scoring.StudentMustExist;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentMustExistValidator implements ConstraintValidator<StudentMustExist, String> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void initialize(StudentMustExist constraintAnnotation) {
    //No Initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return userRepository.existsByIdAndRoleAndDeletedFalse(value, Role.STUDENT);
  }
}
