package com.future.function.validation.annotation.communication;

import com.future.function.validation.validator.communication.QuestionnaireTitleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD )
@Constraint(validatedBy = QuestionnaireTitleValidator.class)
public @interface LoggingRoomTitle {
  /**
   * Default message returned when a String field does not fulfill the
   * required values.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "LoggingRoomTitle";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}