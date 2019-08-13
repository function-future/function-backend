package com.future.function.validation.validator.core;

import com.future.function.common.data.core.SharedCourseData;
import com.future.function.validation.annotation.core.BatchCodesMustBeDistinct;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BatchCodesMustBeDistinctValidator
  implements ConstraintValidator<BatchCodesMustBeDistinct, SharedCourseData> {

  @Override
  public void initialize(BatchCodesMustBeDistinct constraintAnnotation) {
    // No initialization needed.
  }

  @Override
  public boolean isValid(
    SharedCourseData data, ConstraintValidatorContext context
  ) {

    String originBatch = data.getOriginBatch();
    String targetBatch = data.getTargetBatch();

    return (originBatch == null) || this.isOriginNotEqualWithTargetBatch(
      originBatch, targetBatch);
  }

  private boolean isOriginNotEqualWithTargetBatch(
    String originBatch, String targetBatch
  ) {

    return targetBatch != null && !originBatch.equalsIgnoreCase(targetBatch);
  }

}
