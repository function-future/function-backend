package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.TypeMustExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = TypeMustExistValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeMustExist {

  String message() default "TypeMustExist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
