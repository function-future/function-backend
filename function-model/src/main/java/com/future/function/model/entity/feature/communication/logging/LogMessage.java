package com.future.function.model.entity.feature.communication.logging;

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
@Document(collection = DocumentName.LOG_MESSAGE)
public class LogMessage extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.LogMessage.SENDER)
  @DBRef(lazy = true)
  private User sender;

  @Field(FieldName.LogMessage.TEXT)
  private String text;

  @Field(FieldName.LogMessage.TOPIC)
  @DBRef(lazy = true)
  private Topic topic;

}
