package com.future.function.service.impl.feature.communication.chatroom;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.repository.feature.communication.chatting.MessageRepository;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.mq.MessagePublisherService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;

  private final ChatroomService chatroomService;

  private final UserService userService;

  @Autowired
  public MessageServiceImpl(
          MessageRepository messageRepository, ChatroomService chatroomService,
          UserService userService) {

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
  public Page<Message> getMessages(
    String chatroomId, Pageable pageable, String userId
  ) {

    if (chatroomId.equalsIgnoreCase("public")) {
      Chatroom publicChatroom = chatroomService.getPublicChatroom();
      chatroomId = publicChatroom.getId();
    }
    return Optional.of(chatroomId)
      .map(id -> chatroomService.getChatroom(id, userId))
      .map(chatroom -> messageRepository.findAllByChatroomOrderByCreatedAtDesc(
        chatroom, pageable))
      .orElse(PageHelper.empty(pageable));
  }

  @Override
  public Page<Message> getMessagesAfterPivot(
    String chatroomId, String messageId, Pageable pageable, String userId
  ) {

    if (chatroomId.equalsIgnoreCase("public")) {
      Chatroom publicChatroom = chatroomService.getPublicChatroom();
      chatroomId = publicChatroom.getId();
    }
    return Optional.of(chatroomId)
      .map(id -> chatroomService.getChatroom(id, userId))
      .map(
        chatroom -> messageRepository.findAllByChatroomAndIdGreaterThanOrderByCreatedAtDesc(
          chatroom, new ObjectId(messageId), pageable))
      .orElse(PageHelper.empty(pageable));
  }

  @Override
  public Page<Message> getMessagesBeforePivot(
    String chatroomId, String messageId, Pageable pageable, String userId
  ) {

    if (chatroomId.equalsIgnoreCase("public")) {
      Chatroom publicChatroom = chatroomService.getPublicChatroom();
      chatroomId = publicChatroom.getId();
    }
    return Optional.of(chatroomId)
      .map(id -> chatroomService.getChatroom(id, userId))
      .map(
        chatroom -> messageRepository.findAllByChatroomAndIdLessThanOrderByCreatedAtDesc(
          chatroom, new ObjectId(messageId), pageable))
      .orElse(PageHelper.empty(pageable));
  }

  @Override
  public Message getLastMessage(String chatroomId, String userId) {

    return Optional.of(chatroomId)
      .map(id -> chatroomService.getChatroom(id, userId))
      .map(messageRepository::findFirstByChatroomOrderByCreatedAtDesc)
      .orElse(null);
  }

  @Override
  public Message createMessage(Message message, String userId) {

    return Optional.of(message)
      .map(this::setSender)
      .map(msg -> this.setChatroom(msg, userId))
      .map(messageRepository::save)
      .orElseThrow(UnsupportedOperationException::new);
  }

  private Message setSender(Message message) {

    message.setSender(userService.getUser(message.getSender()
                                            .getId()));
    return message;
  }

  private Message setChatroom(Message message, String userId) {

    Chatroom chatroom = chatroomService.getChatroom(message.getChatroom()
                                                      .getId(), userId);
    message.setChatroom(chatroomService.updateDate(chatroom));
    return message;
  }

}
