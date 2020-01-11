package com.future.function.validation.annotation.scoring;

import com.future.function.validation.validator.scoring.DateNotPassedValidator;
import com.future.function.validation.validator.scoring.QuizMustExistValidator;
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
@Constraint(validatedBy = QuizMustExistValidator.class)
public @interface QuizMustExist {

  String message() default "QuizMustExist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
