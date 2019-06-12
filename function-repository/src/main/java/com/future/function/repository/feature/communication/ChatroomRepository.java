package com.future.function.repository.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: PriagungSatyagama
 * Created At: 8:26 01/06/2019
 */
@Repository
public interface ChatroomRepository extends MongoRepository<Chatroom, String> {

  /**
   * Find all paged chatting by chatting type and member
   *
   * @param type enum of chatting type (private, public, group)
   * @param member user object who has chatrooms
   * @param pageable pageable object for paging
   *
   * @return {@code Page<Chatroom>} - Paged chatting list from database
   */
  Page<Chatroom> findAllByTypeAndMembersOrderByCreatedAtDesc(ChatroomType type, User member, Pageable pageable);

  /**
   * Find all paged chatting by title keyword and member
   *
   * @param titleKeyword title keyword for searching chatrooms
   * @param member user object who has chatrooms
   * @param pageable pageable object for paging
   *
   * @return {@code Page<Chatroom} - Paged chatting list from database
   */
  Page<Chatroom> findAllByTitleContainingIgnoreCaseAndMembersOrderByCreatedAtDesc(String titleKeyword, User member, Pageable pageable);

}
