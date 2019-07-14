package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.BatchCodesMustBeDistinctValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = BatchCodesMustBeDistinctValidator.class)
@Target({ ElementType.TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchCodesMustBeDistinct {
  
  String message() default "BatchCodesMustBeDistinct";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  String field() default "batch";
  
}
