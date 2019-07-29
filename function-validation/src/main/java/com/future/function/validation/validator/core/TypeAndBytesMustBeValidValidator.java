package com.future.function.validation.validator.core;

import com.future.function.common.data.core.FileData;
import com.future.function.common.enumeration.core.FileType;
import com.future.function.validation.annotation.core.TypeAndBytesMustBeValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class TypeAndBytesMustBeValidValidator
  implements ConstraintValidator<TypeAndBytesMustBeValid, FileData> {
  
  @Override
  public void initialize(TypeAndBytesMustBeValid constraintAnnotation) {
    // No initialization needed.
  }
  
  @Override
  public boolean isValid(FileData data, ConstraintValidatorContext context) {
    
    String type = Optional.ofNullable(data.getType())
      .orElse("");
    
    return Optional.of(type)
      .filter(t -> t.equals(FileType.FOLDER.name()))
      .map(ignored -> this.isByteArrayExist(data))
      .orElseGet(() -> FileType.isAnyMatch(type));
  }
  
  private boolean isByteArrayExist(FileData data) {
    
    return Optional.ofNullable(data.getBytes())
      .map(bytes -> bytes.length == 0)
      .orElse(true);
  }
  
}
