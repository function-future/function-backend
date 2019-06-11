package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.EmailMustBeUniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmailMustBeUniqueValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailMustBeUnique {
  
  /**
   * Default message returned when a String field (email) is not unique in
   * database.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "EmailMustBeUnique";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
}
