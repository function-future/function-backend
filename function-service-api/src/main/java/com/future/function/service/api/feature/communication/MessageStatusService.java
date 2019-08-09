package com.future.function.service.api.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.session.model.Session;

import java.util.List;

/**
 * Author: PriagungSatyagama
 * Created At: 13:55 03/06/2019
 */
public interface MessageStatusService {

  List<MessageStatus> getUnseenMessageStatus(String chatroomId, String userId, Session session);

  List<MessageStatus> getUnseenMessageStatusBeforeTimestamp(String chatroomId, String userId, Long timestamp, Session session);

  boolean getSeenStatus(String chatroomId, String userId, Session session);

  MessageStatus createMessageStatus(MessageStatus messageStatus, Session session);

  void updateSeenStatus(String chatroomId, String messageId, String userId, Session session);

  MessageStatus updateMessageStatus(MessageStatus messageStatus, Session session);

}
