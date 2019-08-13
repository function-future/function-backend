package com.future.function.validation.annotation.core;

import com.future.function.validation.validator.core.TypeAndBytesMustBeValidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.TYPE_USE })
@Constraint(validatedBy = TypeAndBytesMustBeValidValidator.class)
public @interface TypeAndBytesMustBeValid {

  String message() default "TypeAndBytesMustBeValid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String field() default "type";

}
