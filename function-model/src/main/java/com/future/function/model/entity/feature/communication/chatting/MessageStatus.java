package com.future.function.model.entity.feature.communication.chatting;

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
 * Created At: 22:38 30/05/2019
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.MESSAGE_STATUS)
public class MessageStatus extends BaseEntity {

  @Id
  private String id;

  @Field(FieldName.MessageStatus.IS_SEEN)
  private boolean seen;

  @Field(FieldName.MessageStatus.MEMBER)
  @DBRef
  private User member;

  @Field(FieldName.MessageStatus.MESSAGE)
  @DBRef
  private Message message;

  @Field(FieldName.MessageStatus.CHATROOM)
  @DBRef
  private Chatroom chatroom;

}
