package com.future.function.service.impl.feature.communication;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.repository.feature.communication.MessageRepository;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
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

  private final MessageStatusService messageStatusService;

  @Autowired
  public MessageServiceImpl(MessageRepository messageRepository, MessageStatusService messageStatusService, ChatroomService chatroomService, UserService userService) {
    this.messageRepository = messageRepository;
    this.chatroomService = chatroomService;
    this.userService = userService;
    this.messageStatusService = messageStatusService;
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
            .orElse(PageHelper.empty(pageable));
  }

  @Override
  public Message getLastMessage(String chatroomId) {
    return Optional.of(chatroomId)
            .map(chatroomService::getChatroom)
            .map(messageRepository::findFirstByChatroomOrderByCreatedAtDesc)
            .orElse(null);
  }

  @Override
  public Message createMessage(Message message) {
    return Optional.of(message)
            .map(this::setSender)
            .map(this::setChatroom)
            .map(messageRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
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
