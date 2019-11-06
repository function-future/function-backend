package com.future.function.validation.annotation.scoring;

import com.future.function.validation.validator.scoring.DateNotPassedValidator;
import com.future.function.validation.validator.scoring.StudentMustExistValidator;
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
@Constraint(validatedBy = StudentMustExistValidator.class)
public @interface StudentMustExist {

  String message() default "StudentMustExist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
