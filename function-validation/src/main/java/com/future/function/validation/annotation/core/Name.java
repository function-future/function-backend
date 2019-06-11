package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for checking if a String field is a name, which can contain
 * only alphabetical characters.
 */
@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {

  /**
   * Default message returned when a String field does not fulfill the
   * required values.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "Name";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
