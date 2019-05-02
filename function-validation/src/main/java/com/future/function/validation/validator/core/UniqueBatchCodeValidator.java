package com.future.function.validation.validator.core;

import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.UniqueBatchCode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class UniqueBatchCodeValidator
  implements ConstraintValidator<UniqueBatchCode, Long> {
  
  @Autowired
  private BatchRepository batchRepository;
  
  @Override
  public void initialize(UniqueBatchCode constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(Long value, ConstraintValidatorContext context) {
    
    return !Optional.ofNullable(value)
      .flatMap(batchRepository::findByCode)
      .isPresent();
  }
  
}
