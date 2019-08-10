package com.future.function.web.mapper.response.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.embedded.ParticipantDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomParticipantResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.LastMessageResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.future.function.web.mapper.response.communication.ParticipantResponseMapper.toParticipantDetailResponse;

/**
 * Author: priagung.satyagama
 * Created At: 9:23 AM 6/11/2019
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomResponseMapper {

    private static ChatroomDetailResponse toChatroomDetailResponse(Chatroom chatroom, String urlPrefix) {
        List<ParticipantDetailResponse> participants = new ArrayList<>();
        chatroom.getMembers().forEach(member -> participants.add(toParticipantDetailResponse(member, urlPrefix)));
        return ChatroomDetailResponse.builder()
                .id(chatroom.getId())
                .name(chatroom.getTitle())
                .members(participants)
                .type(chatroom.getType().name())
                .build();
    }

    public static DataResponse<ChatroomDetailResponse> toChatroomDetailDataResponse(
            Chatroom chatroom, String urlPrefix) {
        return ResponseHelper.toDataResponse(HttpStatus.OK, toChatroomDetailResponse(chatroom, urlPrefix));
    }

    private static ChatroomParticipantResponse toChatroomParticipantResponse(User user, String urlPrefix) {
        return ChatroomParticipantResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .avatar(ParticipantResponseMapper.getAvatarThumbnailUrl(user.getPictureV2(), urlPrefix))
                .build();
    }

    private static ChatroomResponse toChatroomResponse(Chatroom chatroom, Message lastMessage, boolean isSeen, String urlPrefix) {
        List<ChatroomParticipantResponse> participants = new ArrayList<>();
        chatroom.getMembers().forEach(member -> participants.add(toChatroomParticipantResponse(member, urlPrefix)));
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
            Session session,
            String urlPrefix
    ) {
        return data.getContent()
                .stream()
                .map(content -> toChatroomResponse(
                        content,
                        messageService.getLastMessage(content.getId(), session),
                        messageStatusService.getSeenStatus(content.getId(), session.getUserId(), session),
                        urlPrefix)
                )
                .collect(Collectors.toList());
    }

    public static PagingResponse<ChatroomResponse> toPagingChatroomResponse(
            Page<Chatroom> data,
            MessageService messageService,
            MessageStatusService messageStatusService,
            String urlPrefix,
            Session session
    ) {
        return ResponseHelper.toPagingResponse(
                HttpStatus.OK, toChatroomResponseList(data, messageService, messageStatusService, session, urlPrefix), PageHelper.toPaging(data));
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

    private static MessageResponse toMessageResponse(Message message, String urlPrefix) {
        return MessageResponse.builder()
                .id(message.getId())
                .sender(toChatroomParticipantResponse(message.getSender(), urlPrefix))
                .text(message.getText())
                .time(message.getCreatedAt())
                .build();
    }

    private static List<MessageResponse> toMessageListResponse(Page<Message> data, String urlPrefix) {
        return data.getContent()
                .stream()
                .map(message -> toMessageResponse(message, urlPrefix))
                .collect(Collectors.toList());
    }

    public static PagingResponse<MessageResponse> toMessagePagingResponse(Page<Message> data, String urlPrefix) {
        return ResponseHelper.toPagingResponse(HttpStatus.OK, toMessageListResponse(data, urlPrefix), PageHelper.toPaging(data));
    }


}
