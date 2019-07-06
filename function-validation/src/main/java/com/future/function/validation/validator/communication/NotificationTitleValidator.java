package com.future.function.validation.validator.communication;

import com.future.function.validation.annotation.communication.NotificationTitle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Author: PriagungSatyagama
 * Created At: 18:50 06/07/2019
 */
public class NotificationTitleValidator implements ConstraintValidator<NotificationTitle, String> {

  @Override
  public void initialize(NotificationTitle notificationTitle) {
    // No initialization needed.
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    return value.length() <= 50;
  }
}
