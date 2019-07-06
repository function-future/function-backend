package com.future.function.validation.validator.communication;

import com.future.function.validation.annotation.communication.NotificationContent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Author: PriagungSatyagama
 * Created At: 19:00 06/07/2019
 */
public class NotificationContentValidator implements ConstraintValidator<NotificationContent, String> {

  @Override
  public void initialize(NotificationContent notificationContent) {
    // No initialization needed.
  }

  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return s.length() <= 140;
  }
}
