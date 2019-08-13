package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

@SuppressWarnings("squid:S00101")
@ChangeLog(order = "003")
public class DataMigration_003 {

    @ChangeSet(author = "priagung",
            id ="chatroomMigration",
            order = "0001")
    public void insertChatroom(MongoTemplate mongoTemplate) {
        BasicDBObject chatroom = new BasicDBObject();
        chatroom.append(FieldName.Chatroom.CHATROOM_TITLE, "Public Chatroom");
        chatroom.append(FieldName.Chatroom.TYPE, ChatroomType.PUBLIC);
        chatroom.append("_class", "com.future.function.model.entity.feature.communication.chatting.Chatroom");
        chatroom.append(
                FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
        chatroom.append(
                FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
        chatroom.append("version", 1L);
        chatroom.append("deleted", false);
        mongoTemplate.insert(chatroom, DocumentName.CHATROOM);
    }
}
