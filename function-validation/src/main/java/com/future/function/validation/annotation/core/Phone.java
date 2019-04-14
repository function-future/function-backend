package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if a String field is a phone, which can contain
 * only numerical characters or with one + sign.
 */
@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Phone {
  
  /**
   * Default message returned when a String field does not fulfill the
   * required values.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "Phone";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
