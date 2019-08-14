package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.BatchMustExistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = BatchMustExistValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BatchMustExist {
  
  String message() default "BatchMustExist";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
  
  String field() default "batch";
  
}
