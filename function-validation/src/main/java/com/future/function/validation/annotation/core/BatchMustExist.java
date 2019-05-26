package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.BatchMustExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if an object of implementation class of {@code
 * CourseData} has batch representation in database.
 */
@Documented
@Constraint(validatedBy = BatchMustExistValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchMustExist {
  
  /**
   * Default message returned when any of the specified batch numbers in
   * implementation class of {@code CourseData} could not be found in database.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "BatchMustExist";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  /**
   * Declares which field of an object that causes this annotation's
   * validator to mark the object as invalid.
   *
   * @return {@code String} - The name of the field.
   */
  String field() default "batchCodes";
  
}
