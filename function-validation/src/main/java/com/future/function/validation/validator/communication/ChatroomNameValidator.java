package com.future.function.validation.validator.communication;

import com.future.function.validation.annotation.communication.ChatroomName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Author: PriagungSatyagama
 * Created At: 15:02 04/06/2019
 */
public class ChatroomNameValidator implements ConstraintValidator<ChatroomName, String> {

  @Override
  public void initialize(ChatroomName chatroomName) {

  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return s == null || s.matches("^([A-Za-z0-9]+(( )[A-Za-z0-9]+)*)+$");
  }
}
