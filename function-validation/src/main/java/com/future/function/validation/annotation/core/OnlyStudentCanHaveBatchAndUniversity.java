package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.OnlyStudentCanHaveBatchAndUniversityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if a class of type {@code User} has a batch and
 * university only when that user object's role is
 * {@link com.future.function.common.enumeration.core.Role#STUDENT}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.TYPE_USE })
@Constraint(validatedBy = OnlyStudentCanHaveBatchAndUniversityValidator.class)
public @interface OnlyStudentCanHaveBatchAndUniversity {
  
  /**
   * Default message returned when a String field does not fulfill the
   * required values.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "OnlyStudentCanHaveBatchAndUniversity";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  /**
   * Declares which field of an object that causes this annotation's
   * validator to mark the object as invalid.
   *
   * @return {@code String} - The name of the field.
   */
  String field() default "role";
  
}
