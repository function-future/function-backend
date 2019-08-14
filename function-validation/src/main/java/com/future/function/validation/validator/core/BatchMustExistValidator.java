package com.future.function.validation.validator.core;

import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.BatchMustExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class BatchMustExistValidator
  implements ConstraintValidator<BatchMustExist, String> {
  
  @Autowired
  private BatchRepository batchRepository;
  
  @Override
  public void initialize(BatchMustExist constraintAnnotation) {
    // No initialization needed for this validator.
  }
  
  @Override
  public boolean isValid(String batchCode, ConstraintValidatorContext context) {
    
    if (batchCode == null || StringUtils.isEmpty(batchCode)) {
      return true;
    }
    
    return Optional.of(batchCode)
      .filter(this::isBatchWithBatchCodeExist)
      .isPresent();
  }
  
  private boolean isBatchWithBatchCodeExist(String batchCode) {
    
    return !batchRepository.findByCodeAndDeletedFalse(batchCode)
      .equals(Optional.empty());
  }
  
}
