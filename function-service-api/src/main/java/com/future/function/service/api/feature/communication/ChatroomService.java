package com.future.function.service.api.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: PriagungSatyagama
 * Created At: 13:35 03/06/2019
 */
public interface ChatroomService {

  Page<Chatroom> getChatrooms(String type, String userId, Pageable pageable);

  Page<Chatroom> getChatroomsWithKeyword(String keyword, String userId, Pageable pageable);

  Chatroom getChatroom(String chatroomId);

  Chatroom createChatroom(Chatroom chatroom);

  Chatroom updateChatroom(Chatroom chatroom);

  Chatroom getPublicChatroom();

}
