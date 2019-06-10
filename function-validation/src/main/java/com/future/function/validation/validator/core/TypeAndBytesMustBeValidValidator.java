package com.future.function.validation.validator.core;

import com.future.function.common.data.core.FileData;
import com.future.function.common.enumeration.core.FileType;
import com.future.function.validation.annotation.core.TypeAndBytesMustBeValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TypeAndBytesMustBeValidValidator
  implements ConstraintValidator<TypeAndBytesMustBeValid, FileData> {
  
  @Override
  public void initialize(TypeAndBytesMustBeValid constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(FileData data, ConstraintValidatorContext context) {
    
    boolean nonEmptyBytes = data.getBytes().length != 0;
    
    if (nonEmptyBytes) {
      return FileType.FILE.name()
        .equals(data.getType());
    } else {
      return FileType.isAnyMatch(data.getType());
    }
  }
  
}
