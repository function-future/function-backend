package com.future.function.validation.annotation.communication;

import com.future.function.validation.validator.communication.NotificationContentValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author: PriagungSatyagama
 * Created At: 18:56 06/07/2019
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
@Constraint(validatedBy = NotificationContentValidator.class)
public @interface NotificationContent {
  /**
   * Default message returned when a String field does not fulfill the
   * required values.
   *
   * @return {@code String} - The name of this annotation.
   */
  String message() default "NotificationContent";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
