package com.future.function.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import com.future.function.validation.validator.InitialValidationAnnotationValidator;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = {InitialValidationAnnotationValidator.class})
public @interface InitialValidationAnnotation {

  Class<?>[] groups() default {};

  String message() default "InitialValidationAnnotation";

  String value() default "";

}
