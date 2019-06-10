package com.future.function.repository.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 13:37 01/06/2019
 */
@Repository
public interface MessageStatusRepository extends MongoRepository<MessageStatus, String> {

  /**
   * find all message status by chatting, member, and unseen
   *
   * @param chatroom chatting where to find message's message status
   * @param member member who has message status
   *
   * @return {@code List<MessageStatus} - all message status filtered with the query
   */
  List<MessageStatus> findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(
          Chatroom chatroom, User member);

  /**
   * find all message status by chatting, member, timestamp, and unseen
   *
   * @param chatroom chatting where to find message's message status
   * @param member member who has message status
   * @param timestamp timestamp as a pivot to find message status less than equal this
   * @return {@code List<MessageStatus>} - all message status filtered with the query
   */
  List<MessageStatus> findAllByChatroomAndMemberAndCreatedAtLessThanEqualAndSeenIsFalseOrderByCreatedAtDesc(
          Chatroom chatroom, User member, Long timestamp);

}
