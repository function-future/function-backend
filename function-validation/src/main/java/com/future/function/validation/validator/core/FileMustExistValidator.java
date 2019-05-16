package com.future.function.validation.validator.core;

import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.validation.annotation.core.FileMustExist;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FileMustExistValidator
  implements ConstraintValidator<FileMustExist, List<String>> {
  
  @Autowired
  private ResourceService resourceService;
  
  @Override
  public void initialize(FileMustExist constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(
    List<String> value, ConstraintValidatorContext context
  ) {
    
    return value.stream()
      .allMatch(s -> resourceService.getFile(s) != null);
  }
  
}
