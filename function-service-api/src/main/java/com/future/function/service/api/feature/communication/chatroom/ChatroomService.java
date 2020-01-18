package com.future.function.service.api.feature.communication.chatroom;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatroomService {

  Page<Chatroom> getChatrooms(String userId, Pageable pageable);

  Page<Chatroom> getChatroomsWithKeyword(
    String keyword, String userId, Pageable pageable
  );

  Chatroom getChatroom(String chatroomId, String userId);

  Chatroom createChatroom(Chatroom chatroom);

  Chatroom updateChatroom(Chatroom chatroom, String userId);

  Chatroom getPublicChatroom();

  void authorizeSubscription(String userId, String chatroomId);

  void setLimitChatrooms(String userId, long limit);

  void unsetLimitChatrooms(String userId);

  void syncChatroomList(String userId);

  Chatroom syncChatroomList(Chatroom chatroom);

  Chatroom updateDate(Chatroom chatroom);
}
