package com.future.function.web.model.request.communication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

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

  @Length(min = 1, max = 30)
  private String title;

  @Length(max = 140)
  private String description;

  private Boolean isRepeatedMonthly;

  @Range(min = 1, max = 31)
  private Integer monthlyDate;

  private List<String> repeatDays;

  @Range(min = 0, max = 23)
  private Integer hour;

  @Range(min = 0, max = 59)
  private Integer minute;

  @NotEmpty
  private List<String> members;

}
