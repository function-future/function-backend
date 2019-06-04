package com.future.function.model.entity.feature.communication;

import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Author: PriagungSatyagama
 * Created At: 22:11 30/05/2019
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.MESSAGE)
public class Message extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.Message.SENDER)
  @DBRef(lazy = true)
  private User sender;

  @Field(FieldName.Message.TEXT)
  private String text;

  @Field(FieldName.Message.CHATROOM)
  @DBRef(lazy = true)
  private Chatroom chatroom;

}
