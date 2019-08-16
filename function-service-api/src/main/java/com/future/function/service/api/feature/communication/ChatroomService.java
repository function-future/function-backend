package com.future.function.service.api.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatroomService {

  Page<Chatroom> getChatrooms(String type, String userId, Pageable pageable);

  Page<Chatroom> getChatroomsWithKeyword(
    String keyword, String userId, Pageable pageable
  );

  Chatroom getChatroom(String chatroomId, String userId);

  Chatroom createChatroom(Chatroom chatroom);

  Chatroom updateChatroom(Chatroom chatroom, String userId);

  Chatroom getPublicChatroom();

}
