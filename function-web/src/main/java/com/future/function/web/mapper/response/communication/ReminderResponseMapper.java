package com.future.function.web.mapper.response.communication;

import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderDetailResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

import static com.future.function.web.mapper.response.communication.ParticipantResponseMapper.toParticipantDetailResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReminderResponseMapper {

  public static PagingResponse<ReminderResponse> toPagingReminderResponse(
    Page<Reminder> reminders
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toListReminderResponse(reminders),
                                           PageHelper.toPaging(reminders)
    );
  }

  private static List<ReminderResponse> toListReminderResponse(
    Page<Reminder> reminders
  ) {

    return reminders.getContent()
      .stream()
      .map(ReminderResponseMapper::toReminderResponse)
      .collect(Collectors.toList());
  }

  private static ReminderResponse toReminderResponse(Reminder reminder) {

    return ReminderResponse.builder()
      .id(reminder.getId())
      .title(reminder.getTitle())
      .description(reminder.getContent())
      .isRepeatedMonthly(reminder.getIsRepeatedMonthly())
      .monthlyDate(reminder.getMonthlyDate())
      .repeatDays(reminder.getDays() != null ? reminder.getDays()
        .stream()
        .map(DayOfWeek::name)
        .collect(Collectors.toList()) : null)
      .memberCount(reminder.getMembers()
                     .size())
      .time(timeToString(reminder.getHour(), reminder.getMinute()))
      .build();
  }

  private static String timeToString(Integer hour, Integer minute) {

    return String.format("%02d:%02d", hour, minute);
  }

  public static DataResponse<ReminderDetailResponse> toSingleReminderDataResponse(
    Reminder reminder, String urlPrefix
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toReminderDetailResponse(reminder, urlPrefix));
  }

  private static ReminderDetailResponse toReminderDetailResponse(
    Reminder reminder, String urlPrefix
  ) {

    return ReminderDetailResponse.builder()
      .id(reminder.getId())
      .title(reminder.getTitle())
      .description(reminder.getContent())
      .isRepeatedMonthly(reminder.getIsRepeatedMonthly())
      .monthlyDate(reminder.getMonthlyDate())
      .repeatDays(reminder.getDays() != null ? reminder.getDays()
        .stream()
        .map(DayOfWeek::name)
        .collect(Collectors.toList()) : null)
      .time(timeToString(reminder.getHour(), reminder.getMinute()))
      .members(reminder.getMembers()
                 .stream()
                 .map(member -> toParticipantDetailResponse(member, urlPrefix))
                 .collect(Collectors.toList()))
      .build();
  }

}
