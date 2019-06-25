package com.future.function.validation.validator.core;

import com.future.function.repository.feature.core.UserRepository;
import com.future.function.validation.annotation.core.EmailMustBeUnique;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class EmailMustBeUniqueValidator
  implements ConstraintValidator<EmailMustBeUnique, String> {
  
  @Autowired
  private UserRepository userRepository;
  
  @Override
  public void initialize(EmailMustBeUnique constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    
    return !Optional.ofNullable(email)
      .flatMap(userRepository::findByEmailAndDeletedFalse)
      .isPresent();
  }
  
}
