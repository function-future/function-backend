package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.ChatroomRequestMapper;
import com.future.function.web.mapper.request.communication.MessageRequestMapper;
import com.future.function.web.mapper.response.communication.ChatroomResponseMapper;
import com.future.function.web.model.request.communication.ChatroomRequest;
import com.future.function.web.model.request.communication.MessageRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: priagung.satyagama
 * Created At: 1:55 PM 6/12/2019
 */
@RestController
@RequestMapping(value = "/api/communication/chatrooms")
@WithAnyRole(roles = {
        Role.STUDENT, Role.MENTOR, Role.ADMIN, Role.JUDGE
})
public class ChatroomController {

    private ChatroomRequestMapper chatroomRequestMapper;

    private MessageRequestMapper messageRequestMapper;

    private ChatroomService chatroomService;

    private MessageService messageService;

    private FileProperties fileProperties;

    private MessageStatusService messageStatusService;

    @Autowired
    public ChatroomController(
            ChatroomRequestMapper chatroomRequestMapper,
            ChatroomService chatroomService,
            MessageService messageService,
            MessageStatusService messageStatusService,
            MessageRequestMapper messageRequestMapper,
            FileProperties fileProperties
    ) {
        this.fileProperties = fileProperties;
        this.chatroomRequestMapper = chatroomRequestMapper;
        this.chatroomService = chatroomService;
        this.messageService = messageService;
        this.messageStatusService = messageStatusService;
        this.messageRequestMapper = messageRequestMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<ChatroomResponse> getChatrooms(
            Session session,
            @RequestParam(required = false, defaultValue = "PUBLIC") String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size
            ) {

        if (search != null && !search.equals("")) {
            return ChatroomResponseMapper.toPagingChatroomResponse(
                    chatroomService.getChatroomsWithKeyword(search, session.getUserId(), PageHelper.toPageable(page, size)),
                    messageService,
                    messageStatusService,
                    session.getUserId(),
                    fileProperties.getUrlPrefix()
            );
        }
        else {
            return ChatroomResponseMapper.toPagingChatroomResponse(
                    chatroomService.getChatrooms(type, session.getUserId(), PageHelper.toPageable(page, size)),
                    messageService,
                    messageStatusService,
                    session.getUserId(),
                    fileProperties.getUrlPrefix()
            );
        }
    }

    @GetMapping(value = "/{chatroomId:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<ChatroomDetailResponse> getChatroom(Session session,
                                                            @PathVariable String chatroomId) {
        return ChatroomResponseMapper.toChatroomDetailDataResponse(chatroomService.getChatroom(chatroomId), fileProperties.getUrlPrefix());
    }

    @GetMapping(value = "/{chatroomId:.+}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<MessageResponse> getMessages(
            Session session,
            @PathVariable String chatroomId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ChatroomResponseMapper.toMessagePagingResponse(
                messageService.getMessages(chatroomId, PageHelper.toPageable(page, size)), fileProperties.getUrlPrefix());
    }

    @GetMapping(value = "/{chatroomId:.+}/messages/_after", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<MessageResponse> getMessagesAfterPivot(
            Session session,
            @PathVariable String chatroomId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = true) String messageId
    ) {
        return ChatroomResponseMapper.toMessagePagingResponse(
                messageService.getMessagesAfterPivot(chatroomId, messageId, PageHelper.toPageable(page, size)), fileProperties.getUrlPrefix());
    }

    @GetMapping(value = "/{chatroomId:.+}/messages/_before", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<MessageResponse> getMessagesBeforePivot(
            Session session,
            @PathVariable String chatroomId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = true) String messageId
    ) {
        return ChatroomResponseMapper.toMessagePagingResponse(
                messageService.getMessagesBeforePivot(chatroomId, messageId, PageHelper.toPageable(page, size)), fileProperties.getUrlPrefix());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<ChatroomDetailResponse> createChatroom(Session session,
                                                               @RequestBody ChatroomRequest chatroomRequest) {
        return ChatroomResponseMapper.toChatroomDetailDataResponse(chatroomService
                .createChatroom(chatroomRequestMapper.toChatroom(chatroomRequest, null)), fileProperties.getUrlPrefix());
    }

    @PostMapping( value = "/{chatroomId:.+}/messages",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public BaseResponse createMessage(Session session,
                                      @PathVariable String chatroomId, @RequestBody MessageRequest messageRequest) {
        if (chatroomId.equalsIgnoreCase("public")) {
            chatroomId = chatroomService.getPublicChatroom().getId();
        }
        Message message = messageService.createMessage(messageRequestMapper.toMessage(messageRequest, session.getUserId(), chatroomId));
        Chatroom chatroom = chatroomService.getChatroom(chatroomId);
        if (!chatroom.getType().equals(ChatroomType.PUBLIC)) {
            chatroom.getMembers().forEach(member -> messageStatusService.createMessageStatus(MessageStatus.builder()
                    .message(message)
                    .chatroom(chatroom)
                    .member(member)
                    .seen(member.getId().equals(session.getUserId()))
                    .build())
            );
        }
        return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{chatroomId:.+}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<ChatroomDetailResponse> updateChatroom(Session session, @PathVariable String chatroomId,
                                                               @RequestBody ChatroomRequest chatroomRequest) {
        return ChatroomResponseMapper.toChatroomDetailDataResponse(
                chatroomService.updateChatroom(chatroomRequestMapper.toChatroom(chatroomRequest, chatroomId)), fileProperties.getUrlPrefix());
    }

    @PutMapping(value = "/{chatroomId:.+}/messages/{messageId:.+}/_read", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateMessageStatus(Session session, @PathVariable String chatroomId,
                                            @PathVariable String messageId) {
        if (chatroomId.equalsIgnoreCase("public")) {
            chatroomId = chatroomService.getPublicChatroom().getId();
        }
        messageStatusService.updateSeenStatus(chatroomId, messageId, session.getUserId());
        return ResponseHelper.toBaseResponse(HttpStatus.OK);
    }

}
