package com.future.function.web.mapper.request.communication;

import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.model.entity.feature.core.User;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.ReminderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Author: PriagungSatyagama
 * Created At: 21:20 06/07/2019
 */
@Slf4j
@Component
public class ReminderRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public ReminderRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public Reminder toReminder(ReminderRequest request, String reminderId) {
    validator.validate(request);

    return Reminder.builder()
            .id(reminderId)
            .content(request.getDescription())
            .title(request.getTitle())
            .isRepeatedMonthly(request.getIsRepeatedMonthly())
            .days(request.getRepeatDays().stream()
                    .map(DayOfWeek::valueOf)
                    .collect(Collectors.toList()))
            .monthlyDate(request.getMonthlyDate())
            .hour(request.getHour())
            .minute(request.getHour())
            .members(request.getMembers().stream()
                    .map(member -> User.builder().id(member).build())
                    .collect(Collectors.toList()))
            .build();
  }
}
