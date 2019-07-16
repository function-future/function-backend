package com.future.function.validation.validator.core;

import com.future.function.common.data.core.UserData;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.validation.annotation.core.EmailMustBeUnique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;

public class EmailMustBeUniqueValidator
  implements ConstraintValidator<EmailMustBeUnique, UserData> {
  
  @Autowired
  private UserRepository userRepository;
  
  @Override
  public void initialize(EmailMustBeUnique constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(UserData data, ConstraintValidatorContext context) {
    
    String email = Optional.ofNullable(data.getEmail())
      .orElse("");
    
    return Optional.of(data)
      .filter(userData -> Objects.nonNull(userData.getId()))
      .flatMap(ignored -> userRepository.findByEmailAndDeletedFalse(email))
      .map(User::getId)
      .map(foundUserId -> foundUserId.equals(data.getId()))
      .orElseGet(() -> !userRepository.findByEmailAndDeletedFalse(email).isPresent());
  }
  
}
