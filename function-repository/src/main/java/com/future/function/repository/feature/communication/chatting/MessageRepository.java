package com.future.function.repository.feature.communication.chatting;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

  Page<Message> findAllByChatroomOrderByCreatedAtDesc(
    Chatroom chatroom, Pageable pageable
  );

  Page<Message> findAllByChatroomAndIdGreaterThanOrderByCreatedAtDesc(
    Chatroom chatroom, ObjectId messageId, Pageable pageable
  );

  Page<Message> findAllByChatroomAndIdLessThanOrderByCreatedAtDesc(
    Chatroom chatroom, ObjectId messageId, Pageable pageable
  );

  Message findFirstByChatroomOrderByCreatedAtDesc(Chatroom chatroom);

}
