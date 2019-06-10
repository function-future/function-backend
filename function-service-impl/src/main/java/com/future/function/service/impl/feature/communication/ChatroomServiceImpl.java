package com.future.function.service.impl.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.ChatroomRepository;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: PriagungSatyagama
 * Created At: 14:21 03/06/2019
 */
@Service
public class ChatroomServiceImpl implements ChatroomService {

  private final UserService userService;

  private final ChatroomRepository chatroomRepository;

  @Autowired
  public ChatroomServiceImpl(UserService userService, ChatroomRepository chatroomRepository) {
    this.userService = userService;
    this.chatroomRepository = chatroomRepository;
  }

  @Override
  public Page<Chatroom> getChatrooms(String type, String userId, Pageable pageable) {
    ChatroomType chatroomType = ChatroomType.fromString(type);
    User user = userService.getUser(userId);
    return chatroomRepository.findAllByTypeAndMembersOrderByCreatedAtDesc(
            chatroomType, user, pageable);
  }

  @Override
  public Page<Chatroom> getChatroomsWithKeyword(String keyword, String userId, Pageable pageable) {
    return Optional.of(userId)
            .map(userService::getUser)
            .map(user -> chatroomRepository.findAllByTitleContainingIgnoreCaseAndMembersOrderByCreatedAtDesc(
                    keyword, user, pageable))
            .get();
  }

  @Override
  public Chatroom getChatroom(String chatroomId) {
    return Optional.ofNullable(chatroomId)
            .map(chatroomRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Get Chatroom Not Found"));
  }

  @Override
  public Chatroom createChatroom(Chatroom chatroom) {
    return Optional.of(chatroom)
            .map(this::setMembers)
            .map(chatroomRepository::save)
            .get();
  }

  @Override
  public Chatroom updateChatroom(Chatroom chatroom) {
    Chatroom existing = Optional.of(chatroomRepository.findOne(chatroom.getId()))
            .orElseThrow(() -> new NotFoundException("Chatroom not found"));
    return Optional.of(existing)
            .map(room -> this.updateMember(room, chatroom))
            .map(room -> this.updateTitle(room, chatroom))
            .map(room -> this.updateType(room, chatroom))
            .map(chatroomRepository::save)
            .get();
  }

  private Chatroom updateMember(Chatroom existing, Chatroom newChatroom) {
    existing.setMembers(newChatroom.getMembers());
    this.setMembers(existing);
    return existing;
  }

  private Chatroom updateTitle(Chatroom existing, Chatroom newChatroom) {
    existing.setTitle(newChatroom.getTitle());
    return existing;
  }

  private Chatroom updateType(Chatroom existing, Chatroom newChatroom) {
    existing.setType(newChatroom.getType());
    return existing;
  }

  private Chatroom setMembers(Chatroom chatroom) {
    List<User> members = new ArrayList<>();
    chatroom.getMembers().forEach(member -> {
      members.add(userService.getUser(member.getId()));
    });
    chatroom.setMembers(members);
    return chatroom;
  }
}
