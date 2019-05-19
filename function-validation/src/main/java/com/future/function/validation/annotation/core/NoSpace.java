package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.NoSpaceValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if a String contains any space, which is not allowed.
 */
@Documented
@Constraint(validatedBy = NoSpaceValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSpace {
  
  /**
   * Default message returned when a String contains space.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "NoSpace";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
