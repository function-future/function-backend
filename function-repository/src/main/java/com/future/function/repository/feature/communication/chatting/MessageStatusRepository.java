package com.future.function.repository.feature.communication.chatting;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageStatusRepository extends MongoRepository<MessageStatus, String> {

  List<MessageStatus> findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(
          Chatroom chatroom, User member);

  List<MessageStatus> findAllByChatroomAndMemberAndCreatedAtLessThanEqualAndSeenIsFalseOrderByCreatedAtDesc(
          Chatroom chatroom, User member, Long timestamp);

}
