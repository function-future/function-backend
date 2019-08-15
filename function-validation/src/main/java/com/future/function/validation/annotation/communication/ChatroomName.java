package com.future.function.validation.annotation.communication;

import com.future.function.validation.validator.communication.ChatroomNameValidator;

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
@Constraint(validatedBy = ChatroomNameValidator.class)
public @interface ChatroomName {

  String message() default "ChatroomName";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
