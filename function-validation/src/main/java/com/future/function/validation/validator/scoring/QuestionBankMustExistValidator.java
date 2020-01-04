package com.future.function.validation.validator.scoring;

import com.future.function.repository.feature.scoring.QuestionBankRepository;
import com.future.function.validation.annotation.scoring.QuestionBankMustExist;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionBankMustExistValidator implements ConstraintValidator<QuestionBankMustExist, List<String>> {

  @Autowired
  private QuestionBankRepository questionBankRepository;

  @Override
  public void initialize(QuestionBankMustExist constraintAnnotation) {
    //No Initialization needed
  }

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    return value.stream()
        .allMatch(questionBankRepository::existsByIdAndDeletedFalse);
  }
}
