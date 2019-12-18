package com.future.function.validation.validator.scoring;

import com.future.function.repository.feature.scoring.QuizRepository;
import com.future.function.validation.annotation.scoring.QuizMustExist;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizMustExistValidator implements ConstraintValidator<QuizMustExist, String> {

  @Autowired
  private QuizRepository quizRepository;

  @Override
  public void initialize(QuizMustExist constraintAnnotation) {
    //No initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return quizRepository.existsByIdAndDeletedFalse(value);
  }
}
