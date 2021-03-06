package com.future.function.web.mapper.response.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.ResourceResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomParticipantResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.LastMessageResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import com.future.function.web.model.response.feature.embedded.ParticipantDetailResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.future.function.web.mapper.response.communication.ParticipantResponseMapper.toParticipantDetailResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomResponseMapper {

  public static DataResponse<ChatroomDetailResponse> toChatroomDetailDataResponse(
    Chatroom chatroom, String urlPrefix, ResourceService resourceService
  ) {

    return ResponseHelper.toDataResponse(
      HttpStatus.OK, toChatroomDetailResponse(chatroom, urlPrefix, resourceService));
  }

  private static ChatroomDetailResponse toChatroomDetailResponse(
    Chatroom chatroom, String urlPrefix, ResourceService resourceService
  ) {

    List<ParticipantDetailResponse> participants = new ArrayList<>();
    chatroom.getMembers()
      .forEach(member -> participants.add(
        toParticipantDetailResponse(member, urlPrefix)));
    return ChatroomDetailResponse.builder()
      .id(chatroom.getId())
      .name(chatroom.getTitle())
      .members(participants)
      .type(chatroom.getType()
              .name())
      .picture(chatroom.getPicture() != null && !chatroom.getPicture().equals("") ?
              getGroupPicture(resourceService.getFile(chatroom.getPicture()), urlPrefix) : null)
      .build();
  }

  public static PagingResponse<ChatroomResponse> toPagingChatroomResponse(
          Page<Chatroom> data, MessageService messageService,
          MessageStatusService messageStatusService, UserService userService, ResourceService resourceService,
          String urlPrefix, String userId
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toChatroomResponseList(data,
                                                                  messageService,
                                                                  messageStatusService,
                                                                  userService,
                                                                  resourceService,
                                                                  userId,
                                                                  urlPrefix
                                           ), PageHelper.toPaging(data)
    );
  }

  private static List<ChatroomResponse> toChatroomResponseList(
    Page<Chatroom> data, MessageService messageService,
    MessageStatusService messageStatusService, UserService userService, ResourceService resourceService,
    String userId, String urlPrefix
  ) {

    return data.getContent()
      .stream()
      .map(content -> toChatroomResponse(content, messageService.getLastMessage(
        content.getId(), userId), messageStatusService.getSeenStatus(
        content.getId(), userId), urlPrefix, userService.getUser(userId),
              content.getPicture() != null && !content.getPicture().equals("") ? resourceService.getFile(content.getPicture()) : null))
      .collect(Collectors.toList());
  }

  private static ChatroomResponse toChatroomResponse(
    Chatroom chatroom, Message lastMessage, boolean isSeen, String urlPrefix, User user, FileV2 picture
  ) {

    List<ChatroomParticipantResponse> participants = new ArrayList<>();
    chatroom.getMembers()
      .forEach(member -> participants.add(
        toChatroomParticipantResponse(member, urlPrefix)));
    return ChatroomResponse.builder()
      .id(chatroom.getId())
      .participants(participants)
      .lastMessage(toLastMessageResponse(lastMessage, isSeen))
      .type(getType(chatroom))
      .name(chatroom.getTitle())
      .picture(getPictureUrl(chatroom, user, urlPrefix, picture))
      .build();
  }

  private static FileContentWebResponse getPictureUrl(Chatroom chatroom, User user, String urlPrefix, FileV2 picture) {
    String chatroomType = getType(chatroom);
    if (chatroomType != null && chatroomType.equals(ChatroomType.PRIVATE.name())) {
      return getPrivatePicture(chatroom, user, urlPrefix);
    } else {
      return getGroupPicture(picture, urlPrefix);
    }
  }

  private static FileContentWebResponse getPrivatePicture(Chatroom chatroom, User user, String urlPrefix) {
    return chatroom.getMembers()
            .stream()
            .filter(member -> !member.getId().equals(user.getId()))
            .map(member -> {
              if (member.getPictureV2() == null) {
                return null;
              }
              return ResourceResponseMapper.toResourceDataResponse(member.getPictureV2(), urlPrefix).getData();
            })
            .iterator()
            .next();
  }

  private static FileContentWebResponse getGroupPicture(FileV2 picture, String urlPrefix) {
    if (picture == null) {
      return null;
    }
    return ResourceResponseMapper.toResourceDataResponse(picture, urlPrefix).getData();
  }

  private static ChatroomParticipantResponse toChatroomParticipantResponse(
    User user, String urlPrefix
  ) {

    return ChatroomParticipantResponse.builder()
      .id(user.getId())
      .name(user.getName())
      .avatar(
        ParticipantResponseMapper.getAvatarThumbnailUrl(user.getPictureV2(),
                                                        urlPrefix
        ))
      .build();
  }

  private static String getType(Chatroom chatroom) {

    return Optional.ofNullable(chatroom.getType())
      .map(ChatroomType::name)
      .orElse(null);
  }

  private static LastMessageResponse toLastMessageResponse(
    Message message, boolean isSeen
  ) {

    return Optional.ofNullable(message)
      .map(m -> LastMessageResponse.builder()
        .isSeen(isSeen)
        .message(m.getText())
        .time(m.getCreatedAt())
        .build())
      .orElse(null);
  }

  public static PagingResponse<MessageResponse> toMessagePagingResponse(
    Page<Message> data, String urlPrefix
  ) {

    return ResponseHelper.toPagingResponse(HttpStatus.OK,
                                           toMessageListResponse(data,
                                                                 urlPrefix
                                           ), PageHelper.toPaging(data)
    );
  }

  private static List<MessageResponse> toMessageListResponse(
    Page<Message> data, String urlPrefix
  ) {

    return data.getContent()
      .stream()
      .map(message -> toMessageResponse(message, urlPrefix))
      .collect(Collectors.toList());
  }

  public static MessageResponse toMessageResponse(
    Message message, String urlPrefix
  ) {

    return MessageResponse.builder()
      .id(message.getId())
      .sender(toChatroomParticipantResponse(message.getSender(), urlPrefix))
      .text(message.getText())
      .time(message.getCreatedAt())
      .build();
  }

}
