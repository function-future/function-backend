package com.future.function.service.impl.feature.communication;

import com.future.function.model.entity.feature.communication.Message;
import com.future.function.repository.feature.communication.MessageRepository;
import com.future.function.service.api.feature.communication.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Author: PriagungSatyagama
 * Created At: 17:06 04/06/2019
 */
@Service
public class MessageServiceImpl implements MessageService {

  private MessageRepository messageRepository;

  @Override
  public Page<Message> getMessages(String chatroomId) {
    return null;
  }

  @Override
  public Message getLastMessage(String chatroomId) {
    return null;
  }

  @Override
  public Message createMessage(Message message) {
    return null;
  }

}
