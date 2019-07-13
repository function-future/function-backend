package com.future.function.validation.validator.core;

import com.future.function.common.data.core.BatchData;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.UniqueBatchCode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueBatchCodeValidator
  implements ConstraintValidator<UniqueBatchCode, BatchData> {
  
  @Autowired
  private BatchRepository batchRepository;
  
  @Override
  public void initialize(UniqueBatchCode constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(BatchData data, ConstraintValidatorContext context) {
    
    return batchRepository.findByCodeAndDeletedFalse(data.getCode())
      .map(Batch::getId)
      .map(id -> id.equals(data.getId()))
      .orElse(true);
  }
  
}
