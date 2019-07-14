package com.future.function.service.impl.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.chatting.ChatroomRepository;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    return chatroomRepository.findAllByTypeAndMembersOrderByUpdatedAtDesc(
            chatroomType, user, pageable);
  }

  @Override
  public Page<Chatroom> getChatroomsWithKeyword(String keyword, String userId, Pageable pageable) {
    return Optional.of(userId)
            .map(userService::getUser)
            .map(user -> chatroomRepository.findAllByTitleContainingIgnoreCaseAndMembersOrderByUpdatedAtDesc(
                    keyword, user, pageable))
            .orElseGet(() -> PageHelper.empty(pageable));
  }

  @Override
  public Chatroom getChatroom(String chatroomId) {
    return Optional.ofNullable(chatroomId)
            .map(chatroomRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Get Chatroom Not Found"));
  }

  @Override
  public Chatroom createChatroom(Chatroom chatroom) {
    if (chatroom.getType().equals(ChatroomType.PRIVATE)) {
      List<String> memberIds = chatroom.getMembers().stream()
              .map(User::getId)
              .collect(Collectors.toList());

      List<User> members = chatroom.getMembers().stream()
              .map(member -> userService.getUser(member.getId()))
              .collect(Collectors.toList());

      List<Chatroom> filteredChatrooms = chatroomRepository.findAllByMembersContaining(members).stream()
              .filter(room -> room.getType().equals(ChatroomType.PRIVATE))
              .filter(room -> isMemberIdsInChatroom(memberIds, room))
              .collect(Collectors.toList());

      if (filteredChatrooms.size() > 0) {
        return filteredChatrooms.get(0);
      }
    }

    return Optional.of(chatroom)
            .map(this::setMembers)
            .map(this::setChatroomName)
            .map(chatroomRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  @Override
  public Chatroom updateChatroom(Chatroom chatroom) {
    return Optional.of(chatroom)
            .map(Chatroom::getId)
            .map(chatroomRepository::findOne)
            .map(room -> this.updateMember(room, chatroom))
            .map(room -> this.updateTitle(room, chatroom))
            .map(chatroomRepository::save)
            .orElse(chatroom);
  }

  @Override
  public Chatroom getPublicChatroom() {
    return chatroomRepository.findByType(ChatroomType.PUBLIC.name())
            .orElseThrow(() -> new NotFoundException("Chatroom not found"));
  }

  private Boolean isMemberIdsInChatroom(List<String> memberIds, Chatroom room) {
    return memberIds.contains(room.getMembers().get(0).getId()) &&
            memberIds.contains(room.getMembers().get(1).getId());
  }

  private Chatroom setChatroomName(Chatroom chatroom) {
    if (chatroom.getType().equals(ChatroomType.PRIVATE)) {
      chatroom.setTitle(chatroom.getMembers().get(0).getName() + " " + chatroom.getMembers().get(1).getName());
    }
    return chatroom;
  }

  private Chatroom updateMember(Chatroom existingChatroom, Chatroom newChatroom) {
    existingChatroom.setMembers(newChatroom.getMembers());
    this.setMembers(existingChatroom);
    return existingChatroom;
  }

  private Chatroom updateTitle(Chatroom existingChatroom, Chatroom newChatroom) {
    existingChatroom.setTitle(newChatroom.getTitle());
    return existingChatroom;
  }

  private Chatroom setMembers(Chatroom chatroom) {
    List<User> members = new ArrayList<>();
    if (chatroom.getMembers() != null) {
      chatroom.getMembers().forEach(member -> {
        members.add(userService.getUser(member.getId()));
      });
    }
    chatroom.setMembers(members);
    return chatroom;
  }
}
