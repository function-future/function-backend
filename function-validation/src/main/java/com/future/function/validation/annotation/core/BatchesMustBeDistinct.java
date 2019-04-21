package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.BatchesMustBeDistinctValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if an object of implementation class of {@code
 * CourseData} has duplicate in the object.
 */
@Documented
@Constraint(validatedBy = BatchesMustBeDistinctValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchesMustBeDistinct {
  
  /**
   * Default message returned when any of the specified batch numbers in
   * implementation class of {@code CourseData} has duplicate in
   * the request object.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "BatchesMustBeDistinct";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
