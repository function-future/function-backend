package com.future.function.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.future.function.validation.annotation.InitialValidationAnnotation;

/**
 * Must implement {@code ConstraintValidator<Annotation, EvaluatedObject>} interface
 */
public class InitialValidationAnnotationValidator implements ConstraintValidator<InitialValidationAnnotation, String> {

  private String string;

  /**
   * Do initialization related to the annotation here
   *
   * @param constraintAnnotation - The annotation that uses this validator
   */
  @Override
  public void initialize(InitialValidationAnnotation constraintAnnotation) {

    // An example, get value before begin call isValid()
    string = constraintAnnotation.value();
  }

  /**
   * Do logic for validation here
   *
   * @param value   - Value of data to be validated
   * @param context - Context of annotation
   * @return boolean result of validation
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // An example, evaluate if given value is empty
    return string.isEmpty();
  }
}
