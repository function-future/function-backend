package com.future.function.web.mapper.response.communication;

import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderDetailResponse;
import com.future.function.web.model.response.feature.communication.reminder.ReminderResponse;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ReminderResponseMapperTest {

  private static final String REMINDER_ID_1 = "reminderId1";
  private static final String REMINDER_ID_2 = "reminderId2";
  private static final User MEMBER = User.builder().id("userId").build();
  private static final Reminder REMINDER_1 = Reminder.builder()
          .id(REMINDER_ID_1)
          .hour(10)
          .minute(10)
          .members(Collections.singletonList(MEMBER))
          .build();
  private static final Reminder REMINDER_2 = Reminder.builder()
          .id(REMINDER_ID_2)
          .hour(10)
          .minute(10)
          .members(Collections.singletonList(MEMBER))
          .build();
  public static final String URL_PREFIX = "localhost:8080";

  @Test
  public void testGivenReminderByCallingToPagingReminderReturnPagingResponse() {
    PagingResponse<ReminderResponse> response = ReminderResponseMapper.toPagingReminderResponse(
            new PageImpl<>(Arrays.asList(REMINDER_1, REMINDER_2), PageHelper.toPageable(1, 2), 2));
    assertThat(response.getData().get(0).getId()).isEqualTo(REMINDER_ID_1);
    assertThat(response.getData().get(1).getId()).isEqualTo(REMINDER_ID_2);
  }

  @Test
  public void testGivenReminderByCallingToSingleReminderReturnDataResponse() {
    DataResponse<ReminderDetailResponse> response = ReminderResponseMapper.toSingleReminderDataResponse(REMINDER_1, URL_PREFIX);
    assertThat(response.getData().getId()).isEqualTo(REMINDER_ID_1);
  }

}
