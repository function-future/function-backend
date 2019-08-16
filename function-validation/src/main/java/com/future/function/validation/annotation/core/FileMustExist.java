package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.FileMustExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FileMustExistValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileMustExist {

  String message() default "FileMustExist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
