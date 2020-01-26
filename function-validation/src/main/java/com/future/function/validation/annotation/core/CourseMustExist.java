package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.CourseMustExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CourseMustExistValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CourseMustExist {

  String message() default "CourseMustExist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String field() default "courses";

}
