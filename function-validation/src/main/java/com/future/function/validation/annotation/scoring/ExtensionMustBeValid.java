package com.future.function.validation.annotation.scoring;

import com.future.function.validation.validator.scoring.ExtensionMustBeValidValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Constraint(validatedBy = ExtensionMustBeValidValidator.class)
public @interface ExtensionMustBeValid {

  String message() default "ExtensionMustBeValid";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] extensions() default {};

}
