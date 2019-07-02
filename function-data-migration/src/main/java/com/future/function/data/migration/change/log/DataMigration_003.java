package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Author: priagung.satyagama
 * Created At: 10:13 AM 6/17/2019
 */
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

    @ChangeSet(author = "priagung",
            id = "messageMigration",
            order = "0002")
    public void insertMessage(MongoTemplate mongoTemplate) {
        User sender = mongoTemplate.findOne(query(where(FieldName.User.EMAIL).is("admin@admin.com")), User.class);
        Chatroom chatroom = mongoTemplate
                .findOne(query(where(FieldName.Chatroom.TYPE).is(ChatroomType.PUBLIC)), Chatroom.class);
        BasicDBObject message = new BasicDBObject();
        message.append(FieldName.Message.SENDER, new DBRef(DocumentName.USER, new ObjectId(sender.getId())));
        message.append(FieldName.Message.TEXT, "Lorem ipsum dolor sit amet");
        message.append(FieldName.Message.CHATROOM, new DBRef(DocumentName.CHATROOM, new ObjectId(chatroom.getId())));
        message.append(
                FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
        message.append(
                FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
        mongoTemplate.insert(message, DocumentName.MESSAGE);
    }

    @ChangeSet(author = "priagung", id = "messageStatusMigration", order = "0003")
    public void insertMessageStatus(MongoTemplate mongoTemplate) {
        Message message = mongoTemplate.findAll(Message.class).get(0);
        List<User> users = mongoTemplate.findAll(User.class);
        Chatroom chatroom = mongoTemplate.findOne(query(where(FieldName.Chatroom.TYPE)
                .is(ChatroomType.PUBLIC)), Chatroom.class);
        users.forEach(user -> {
            BasicDBObject messageStatus = new BasicDBObject();
            messageStatus.append(FieldName.MessageStatus.IS_SEEN, user.getEmail().equals("admin@admin.com"));
            messageStatus.append(FieldName.MessageStatus.MEMBER, new DBRef(DocumentName.USER, new ObjectId(user.getId())));
            messageStatus.append(FieldName.MessageStatus.MESSAGE, new DBRef(DocumentName.MESSAGE, new ObjectId(message.getId())));
            messageStatus.append(FieldName.MessageStatus.CHATROOM, new DBRef(DocumentName.CHATROOM, new ObjectId(chatroom.getId())));
            mongoTemplate.insert(messageStatus, DocumentName.MESSAGE_STATUS);
        });
    }
}
