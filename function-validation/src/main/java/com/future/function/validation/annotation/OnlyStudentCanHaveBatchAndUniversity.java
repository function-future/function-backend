package com.future.function.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import com.future.function.validation.validator.OnlyStudentCanHaveBatchAndUniversityValidator;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
@Constraint(validatedBy = {OnlyStudentCanHaveBatchAndUniversityValidator.class})
public @interface OnlyStudentCanHaveBatchAndUniversity {

  Class<?>[] groups() default {};

  String message() default "OnlyStudentCanHaveBatchAndUniversity";

  String value() default "";

}