package com.future.function.validation.validator.core;

import com.future.function.repository.feature.core.FileRepositoryV2;
import com.future.function.validation.annotation.core.FileMustExist;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FileMustExistValidator
  implements ConstraintValidator<FileMustExist, List<String>> {
  
  @Autowired
  private FileRepositoryV2 fileRepositoryV2;
  
  @Override
  public void initialize(FileMustExist constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(
    List<String> value, ConstraintValidatorContext context
  ) {
    
    return Optional.ofNullable(value)
      .orElseGet(Collections::emptyList)
      .stream()
      .map(fileRepositoryV2::findOne)
      .allMatch(Objects::nonNull);
  }
  
}
