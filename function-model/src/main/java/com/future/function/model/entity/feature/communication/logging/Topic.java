package com.future.function.model.entity.feature.communication.logging;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Author: RickyKennedy
 * Created At:11:43 PM 7/26/2019
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.TOPIC)
public class Topic extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.Topic.TITLE)
  private String title;

  @Field(FieldName.Topic.LOGGING_ROOM)
  @DBRef(lazy = true)
  private LoggingRoom loggingRoom;

}
