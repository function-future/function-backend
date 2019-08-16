package com.future.function.model.entity.feature.communication.reminder;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.DayOfWeek;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.REMINDER)
public class Reminder extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.Reminder.CONTENT)
  private String content;

  @Field(FieldName.Reminder.TITLE)
  private String title;

  @Field(FieldName.Reminder.IS_REPEATED_MONTHLY)
  private Boolean isRepeatedMonthly;

  @Field(FieldName.Reminder.DAYS)
  private List<DayOfWeek> days;

  @Field(FieldName.Reminder.LAST_REMINDER_SENT)
  private Long lastReminderSent;

  @Field(FieldName.Reminder.MONTHLY_DATE)
  private Integer monthlyDate;

  @Field(FieldName.Reminder.REMINDER_HOUR)
  private Integer hour;

  @Field(FieldName.Reminder.REMINDER_MINUTE)
  private Integer minute;

  @Field(FieldName.Reminder.MEMBERS)
  @DBRef(lazy = true)
  private List<User> members;

}
