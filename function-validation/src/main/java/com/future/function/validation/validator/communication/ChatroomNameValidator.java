package com.future.function.validation.validator.communication;

import com.future.function.validation.annotation.communication.ChatroomName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ChatroomNameValidator implements ConstraintValidator<ChatroomName, String> {

  @Override
  public void initialize(ChatroomName constraintAnnotation) {
    // No initialization needed.
  }

  @Override
  public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
    return name == null || name.length() < 30;
  }
}
