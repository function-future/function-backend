package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.AlphanumericValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AlphanumericValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Alphanumeric {

  String message() default "Alphanumeric";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
