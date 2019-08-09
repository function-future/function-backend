package com.future.function.validation.annotation.communication;

import com.future.function.validation.validator.communication.TitleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Constraint(validatedBy = TitleValidator.class)
public @interface Title {
  /**
   * Default message returned when a String field does not fulfill the
   * required values.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "QuestionnaireTitle";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}