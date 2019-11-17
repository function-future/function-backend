package com.future.function.service.api.feature.communication.chatroom;

import com.future.function.model.entity.feature.communication.chatting.MessageStatus;

import java.util.List;

public interface MessageStatusService {

  List<MessageStatus> getUnseenMessageStatus(String chatroomId, String userId);

  List<MessageStatus> getUnseenMessageStatusBeforeTimestamp(
    String chatroomId, String userId, Long timestamp
  );

  boolean getSeenStatus(String chatroomId, String userId);

  MessageStatus createMessageStatus(MessageStatus messageStatus, String userId);

  void updateSeenStatus(String chatroomId, String messageId, String userId, Boolean isAll);

  MessageStatus updateMessageStatus(MessageStatus messageStatus, String userId);

  void enterChatroom(String chatroomId, String userId);

  void leaveChatroom(String chatroomId, String userId);

}
