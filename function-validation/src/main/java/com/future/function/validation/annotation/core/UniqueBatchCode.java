package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.UniqueBatchCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if a batch web request contains unique batch code
 * or not.
 */
@Documented
@Constraint(validatedBy = UniqueBatchCodeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueBatchCode {
  
  /**
   * Default message returned when a String field does not fulfill the
   * required values.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "UniqueBatchCode";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
