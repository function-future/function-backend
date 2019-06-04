package com.future.function.service.api.feature.communication;

import com.future.function.model.entity.feature.communication.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: PriagungSatyagama
 * Created At: 13:49 03/06/2019
 */
public interface MessageService {

  Page<Message> getMessages(String chatroomId, Pageable pageable);

  Message getLastMessage(String chatroomId);

  Message createMessage(Message message);

}
