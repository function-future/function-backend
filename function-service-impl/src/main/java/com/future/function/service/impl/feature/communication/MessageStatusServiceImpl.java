package com.future.function.service.impl.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.chatting.MessageStatusRepository;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageStatusServiceImpl implements MessageStatusService {

  private final UserService userService;

  private final MessageStatusRepository messageStatusRepository;

  private final ChatroomService chatroomService;

  private final MessageService messageService;

  @Autowired
  public MessageStatusServiceImpl(
          UserService userService,
          MessageStatusRepository messageStatusRepository,
          ChatroomService chatroomService,
          MessageService messageService) {
    this.userService = userService;
    this.chatroomService = chatroomService;
    this.messageStatusRepository = messageStatusRepository;
    this.messageService = messageService;
  }

  @Override
  public List<MessageStatus> getUnseenMessageStatus(String chatroomId, String userId) {
    User user = userService.getUser(userId);
    Chatroom chatroom = chatroomService.getChatroom(chatroomId, userId);
    return messageStatusRepository.findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(chatroom, user);
  }

  @Override
  public List<MessageStatus> getUnseenMessageStatusBeforeTimestamp(String chatroomId, String userId, Long timestamp) {
    User user = userService.getUser(userId);
    Chatroom chatroom = chatroomService.getChatroom(chatroomId, userId);
    return messageStatusRepository.findAllByChatroomAndMemberAndCreatedAtLessThanEqualAndSeenIsFalseOrderByCreatedAtDesc(
            chatroom, user, timestamp);
  }

  @Override
  public boolean getSeenStatus(String chatroomId, String userId) {
    return this.getUnseenMessageStatus(chatroomId, userId).size() == 0;
  }

  @Override
  public MessageStatus createMessageStatus(MessageStatus messageStatus, String userId) {
    return Optional.of(messageStatus)
            .map(msgStatus -> this.setChatroom(msgStatus, userId))
            .map(this::setMember)
            .map(this::setMessage)
            .map(messageStatusRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  @Override
  public void updateSeenStatus(String chatroomId, String messageId, String userId) {
    Long timestamp = Optional.of(messageId)
            .map(messageService::getMessage)
            .map(Message::getCreatedAt)
            .orElse(0L);
    this.getUnseenMessageStatus(chatroomId, userId).forEach(messageStatus -> {
      if (messageStatus.getMessage().getCreatedAt() <= timestamp) {
        messageStatus.setSeen(true);
        this.updateMessageStatus(messageStatus, userId);
      }
    });
  }

  @Override
  public MessageStatus updateMessageStatus(MessageStatus messageStatus, String userId) {
    return Optional.of(messageStatus)
            .map(this::setMember)
            .map(this::setMessage)
            .map(msgStatus -> this.setChatroom(msgStatus, userId))
            .map(messageStatusRepository::save)
            .orElse(messageStatus);
  }

  private MessageStatus setMember(MessageStatus messageStatus) {
    messageStatus.setMember(userService.getUser(messageStatus.getMember().getId()));
    return messageStatus;
  }

  private MessageStatus setMessage(MessageStatus messageStatus) {
    messageStatus.setMessage(
            messageService.getMessage(messageStatus.getMessage().getId()));
    return messageStatus;
  }

  private MessageStatus setChatroom(MessageStatus messageStatus, String userId) {
    messageStatus.setChatroom(chatroomService.getChatroom(
            messageStatus.getChatroom().getId(), userId));
    return messageStatus;
  }


}
