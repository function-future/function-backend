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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.NOTIFICATION)
public class Notification extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.Notification.TITLE)
  private String title;

  @Field(FieldName.Notification.CONTENT)
  private String content;

  @Field(FieldName.Notification.IS_SEEN)
  private Boolean seen;

  @Field(FieldName.Notification.MEMBER)
  @DBRef(lazy = true)
  private User member;

}
