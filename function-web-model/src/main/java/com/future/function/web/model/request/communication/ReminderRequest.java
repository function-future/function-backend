package com.future.function.web.model.request.communication;

import com.future.function.validation.annotation.communication.NotificationContent;
import com.future.function.validation.annotation.communication.NotificationTitle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 18:44 06/07/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequest {

  @NotificationTitle
  private String title;

  @NotificationContent
  private String description;

  private Boolean isRepeatedMonthly;

  private Integer monthlyDate;

  private List<String> repeatDays;

  private Integer hour;

  private Integer minute;

  @NotEmpty
  private List<String> members;

}
