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

  Message getMessage(String messageId);

  Page<Message> getMessages(String chatroomId, Pageable pageable, String userId);

  Page<Message> getMessagesAfterPivot(String chatroomId, String messageId, Pageable pageable, String userId);

  Page<Message> getMessagesBeforePivot(String chatroomId, String messageId, Pageable pageable, String userId);

  Message getLastMessage(String chatroomId, String userId);

  Message createMessage(Message message, String userId);

}
