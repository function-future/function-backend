package com.future.function.service.impl.feature.communication;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.Message;
import com.future.function.repository.feature.communication.MessageRepository;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Author: PriagungSatyagama
 * Created At: 17:06 04/06/2019
 */
@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  private final ChatroomService chatroomService;

  private final UserService userService;

  @Autowired
  public MessageServiceImpl(MessageRepository messageRepository, ChatroomService chatroomService, UserService userService) {
    this.messageRepository = messageRepository;
    this.chatroomService = chatroomService;
    this.userService = userService;
  }

  @Override
  public Message getMessage(String messageId) {
    return Optional.of(messageId)
            .map(messageRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Message not found"));
  }

  @Override
  public Page<Message> getMessages(String chatroomId, Pageable pageable) {
    return Optional.of(chatroomId)
            .map(chatroomService::getChatroom)
            .map(chatroom -> messageRepository.findAllByChatroomOrderByCreatedAtDesc(chatroom, pageable))
            .get();
  }

  @Override
  public Message getLastMessage(String chatroomId) {
    return Optional.of(chatroomId)
            .map(chatroomService::getChatroom)
            .map(messageRepository::findFirstByChatroomOrderByCreatedAtDesc)
            .orElseThrow(() -> new NotFoundException("No message was found"));
  }

  @Override
  public Message createMessage(Message message) {
    return Optional.of(message)
            .map(this::setSender)
            .map(this::setChatroom)
            .map(messageRepository::save)
            .get();
  }

  private Message setSender(Message message) {
    message.setSender(userService.getUser(message.getSender().getId()));
    return message;
  }

  private Message setChatroom(Message message) {
    message.setChatroom(chatroomService.getChatroom(message.getChatroom().getId()));
    return message;
  }

}
