package com.future.function.validation.validator.core;

import com.future.function.common.enumeration.core.FileType;
import com.future.function.validation.annotation.core.TypeMustExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class TypeMustExistValidator
  implements ConstraintValidator<TypeMustExist, String> {

  @Override
  public void initialize(TypeMustExist constraintAnnotation) {
    // No annotation needed.
  }

  @Override
  public boolean isValid(String type, ConstraintValidatorContext context) {

    return Optional.ofNullable(type)
      .map(FileType::isAnyMatch)
      .orElse(false);
  }

}
