package com.future.function.repository.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: PriagungSatyagama
 * Created At: 13:10 01/06/2019
 */
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

  /**
   * Find all paged message in a specific chatting
   *
   * @param chatroom chatting where messages are attached
   * @param pageable pageable object for paging
   * @return {@code Page<Message>} -  paged messages from database
   */
  Page<Message> findAllByChatroomOrderByCreatedAtDesc(Chatroom chatroom, Pageable pageable);

  Message findFirstByChatroomOrderByCreatedAtDesc(Chatroom chatroom);

}
