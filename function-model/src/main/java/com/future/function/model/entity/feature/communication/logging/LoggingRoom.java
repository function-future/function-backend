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

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.LOGGING_ROOM)
public class LoggingRoom extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.LoggingRoom.TITLE)
  private String title;

  @Field(FieldName.LoggingRoom.DESCRIPTION)
  private String description;

  @Field(FieldName.LoggingRoom.MEMBERS)
  @DBRef(lazy= true)
  private List<User> members;
}
