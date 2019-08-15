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
@Target({ ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailMustBeUnique {

  String message() default "EmailMustBeUnique";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String field() default "email";

}
