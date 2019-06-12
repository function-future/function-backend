package com.future.function.web.mapper.response.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.response.core.BatchResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
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
                .avatar(user.getPictureV2().getThumbnailUrl())
                .batch(BatchResponseMapper.toBatchDataResponse(user.getBatch()).getData())
                .name(user.getName())
                .type(user.getRole().name())
                .university(user.getUniversity())
                .build();
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
                .avatar(user.getPictureV2().getThumbnailUrl())
                .build();
    }

    private static ChatroomResponse toChatroomResponse(Chatroom chatroom, Message lastMessage, boolean isSeen) {
        List<ChatroomParticipantResponse> participants = new ArrayList<>();
        chatroom.getMembers().forEach(member -> participants.add(toChatroomParticipantResponse(member)));
        return ChatroomResponse.builder()
                .id(chatroom.getId())
                .participants(participants)
                .lastMessage(toLastMessageResponse(lastMessage, isSeen))
                .type(chatroom.getType().name())
                .build();
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
        return LastMessageResponse.builder()
                .isSeen(isSeen)
                .message(message.getText())
                .time(message.getCreatedAt())
                .build();
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
