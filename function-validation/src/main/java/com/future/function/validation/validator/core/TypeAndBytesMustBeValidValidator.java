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
    
    return Optional.of(data)
      .map(FileData::getId)
      .map(ignored -> this.isValidForUpdate(data))
      .orElseGet(() -> this.isValidForCreate(data));
  }
  
  private boolean isValidForUpdate(FileData data) {
    
    String type = this.getOrEmpty(data.getType());
    
    return Optional.of(type)
      .filter(t -> t.equals(FileType.FOLDER.name()))
      .map(ignored -> data.getBytes().length == 0)
      .orElseGet(() -> FileType.isAnyMatch(type));
  }
  
  private String getOrEmpty(String type) {
    
    return Optional.ofNullable(type)
      .orElse("");
  }
  
  private boolean isValidForCreate(FileData data) {
    
    boolean nonEmptyBytes = data.getBytes().length != 0;
    String type = this.getOrEmpty(data.getType());
    
    if (nonEmptyBytes) {
      return type.equals(FileType.FILE.name());
    } else {
      return FileType.isAnyMatch(type);
    }
  }
  
}
