package com.future.function.web.mapper.response.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomParticipantDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomParticipantResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.LastMessageResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Author: priagung.satyagama
 * Created At: 9:23 AM 6/11/2019
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomResponseMapper {

    private static ChatroomParticipantDetailResponse toChatroomParticipantDetailResponse(User user) {
        return ChatroomParticipantDetailResponse.builder()
                .id(user.getId())
                .avatar(getAvatarThumbnailUrl(user.getPictureV2()))
                .batch(getBatch(user.getBatch()))
                .name(user.getName())
                .type(getRole(user))
                .university(user.getUniversity())
                .build();
    }

    private static String getRole(User user) {
        return Optional.ofNullable(user.getRole())
                .map(Role::name)
                .orElse(null);
    }

    private static String getAvatarThumbnailUrl(FileV2 fileV2) {
        return Optional.ofNullable(fileV2)
                .map(FileV2::getThumbnailUrl)
                .orElse(null);
    }

    private static BatchWebResponse getBatch(Batch batch) {
        return Optional.ofNullable(batch)
                .map(BatchResponseMapper::toBatchDataResponse)
                .map(DataResponse::getData)
                .orElse(null);
    }

    private static ChatroomDetailResponse toChatroomDetailResponse(Chatroom chatroom) {
        List<ChatroomParticipantDetailResponse> participants = new ArrayList<>();
        chatroom.getMembers().forEach(member -> participants.add(toChatroomParticipantDetailResponse(member)));
        return ChatroomDetailResponse.builder()
                .id(chatroom.getId())
                .name(chatroom.getTitle())
                .members(participants)
                .build();
    }

    public static DataResponse<ChatroomDetailResponse> toChatroomDetailDataResponse(
            Chatroom chatroom) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, toChatroomDetailResponse(chatroom));
    }

    private static ChatroomParticipantResponse toChatroomParticipantResponse(User user) {
        return ChatroomParticipantResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .avatar(getAvatarThumbnailUrl(user.getPictureV2()))
                .build();
    }

    private static ChatroomResponse toChatroomResponse(Chatroom chatroom, Message lastMessage, boolean isSeen) {
        List<ChatroomParticipantResponse> participants = new ArrayList<>();
        chatroom.getMembers().forEach(member -> participants.add(toChatroomParticipantResponse(member)));
        return ChatroomResponse.builder()
                .id(chatroom.getId())
                .participants(participants)
                .lastMessage(toLastMessageResponse(lastMessage, isSeen))
                .type(getType(chatroom))
                .name(chatroom.getTitle())
                .build();
    }

    private static String getType(Chatroom chatroom) {
        return Optional.ofNullable(chatroom.getType())
                .map(ChatroomType::name)
                .orElse(null);
    }

    private static List<ChatroomResponse> toChatroomResponseList(
            Page<Chatroom> data,
            MessageService messageService,
            MessageStatusService messageStatusService,
            String userId
    ) {
        return data.getContent()
                .stream()
                .map(content -> toChatroomResponse(
                        content,
                        messageService.getLastMessage(content.getId()),
                        messageStatusService.getSeenStatus(content.getId(), userId))
                )
                .collect(Collectors.toList());
    }

    public static PagingResponse<ChatroomResponse> toPagingChatroomResponse(
            Page<Chatroom> data,
            MessageService messageService,
            MessageStatusService messageStatusService,
            String userId
    ) {
        return ResponseHelper.toPagingResponse(
                HttpStatus.OK, toChatroomResponseList(data, messageService, messageStatusService, userId), PageHelper.toPaging(data));
    }

    private static LastMessageResponse toLastMessageResponse(Message message, boolean isSeen) {
        return Optional.ofNullable(message)
                .map(m -> LastMessageResponse.builder()
                    .isSeen(isSeen)
                    .message(m.getText())
                    .time(m.getCreatedAt())
                    .build())
                .orElse(null);
    }

    private static MessageResponse toMessageResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(toChatroomParticipantResponse(message.getSender()))
                .text(message.getText())
                .time(message.getCreatedAt())
                .build();
    }

    private static List<MessageResponse> toMessageListResponse(Page<Message> data) {
        return data.getContent()
                .stream()
                .map(ChatroomResponseMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    public static PagingResponse<MessageResponse> toMessagePagingResponse(Page<Message> data) {
        return ResponseHelper.toPagingResponse(HttpStatus.OK, toMessageListResponse(data), PageHelper.toPaging(data));
    }


}