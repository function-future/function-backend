package com.future.function.web.model.response.feature.communication.reminder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponse {

  private String id;

  private String title;

  private String description;

  private Boolean isRepeatedMonthly;

  private Integer monthlyDate;

  private List<String> repeatDays;

  private Integer memberCount;

  private String time;
}
