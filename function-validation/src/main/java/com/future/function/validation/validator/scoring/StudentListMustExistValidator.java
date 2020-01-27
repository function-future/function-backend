package com.future.function.validation.validator.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.validation.annotation.scoring.StudentListMustExist;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentListMustExistValidator implements ConstraintValidator<StudentListMustExist, List<String>> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void initialize(StudentListMustExist constraintAnnotation) {
    //No initialization needed
  }

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    return value.stream()
        .allMatch(studentId -> userRepository.existsByIdAndRoleAndDeletedFalse(studentId, Role.STUDENT));
  }
}
