package com.future.function.model.entity.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.base.BaseEntity;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 21:48 30/05/2019
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DocumentName.CHATROOM)
public class Chatroom extends BaseEntity {

    @Id
    private String id;

    @Field(FieldName.Chatroom.CHATROOM_TITLE)
    private String title;

    @Field(FieldName.Chatroom.TYPE)
    private ChatroomType type;

    @Field(FieldName.Chatroom.MEMBERS)
    @DBRef(lazy = true)
    private List<User> members;

}
