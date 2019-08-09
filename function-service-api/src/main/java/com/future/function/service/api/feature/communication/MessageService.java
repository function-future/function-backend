package com.future.function.service.api.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.session.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: PriagungSatyagama
 * Created At: 13:49 03/06/2019
 */
public interface MessageService {

  Message getMessage(String messageId, Session session);

  Page<Message> getMessages(String chatroomId, Pageable pageable, Session session);

  Page<Message> getMessagesAfterPivot(String chatroomId, String messageId, Pageable pageable, Session session);

  Page<Message> getMessagesBeforePivot(String chatroomId, String messageId, Pageable pageable, Session session);

  Message getLastMessage(String chatroomId, Session session);

  Message createMessage(Message message, Session session);

}
