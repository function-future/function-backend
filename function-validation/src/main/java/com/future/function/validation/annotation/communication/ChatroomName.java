package com.future.function.validation.annotation.communication;

import com.future.function.validation.validator.communication.ChatroomNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Author: PriagungSatyagama
 * Created At: 15:01 04/06/2019
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.TYPE_USE })
@Constraint(validatedBy = ChatroomNameValidator.class)
public @interface ChatroomName {

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
