package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.communication.MessageStatusService;
import com.future.function.service.api.feature.core.UserService;
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
import org.springframework.web.bind.annotation.*;

/**
 * Author: priagung.satyagama
 * Created At: 1:55 PM 6/12/2019
 */
@RestController
@RequestMapping(value = "/api/communication/chatrooms")
@WithAnyRole(roles = {
        Role.STUDENT, Role.MENTOR, Role.JUDGE, Role.ADMIN
})
public class ChatroomController {

    private ChatroomRequestMapper chatroomRequestMapper;

    private MessageRequestMapper messageRequestMapper;

    private ChatroomService chatroomService;

    private MessageService messageService;
    
    private UserService userService;

    private MessageStatusService messageStatusService;

    @Autowired
    public ChatroomController(
            ChatroomRequestMapper chatroomRequestMapper,
            ChatroomService chatroomService,
            MessageService messageService,
            MessageStatusService messageStatusService,
            MessageRequestMapper messageRequestMapper,
            UserService userService
    ) {
        this.chatroomRequestMapper = chatroomRequestMapper;
        this.chatroomService = chatroomService;
        this.messageService = messageService;
        this.userService = userService;
        this.messageStatusService = messageStatusService;
        this.messageRequestMapper = messageRequestMapper;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<ChatroomResponse> getChatrooms(
            @RequestParam(required = false, defaultValue = "PUBLIC") String type,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Session session) {

        if (search != null) {
            return ChatroomResponseMapper.toPagingChatroomResponse(
                    chatroomService.getChatroomsWithKeyword(search, getUserId(session), PageHelper.toPageable(page, size)),
                    messageService,
                    messageStatusService,
                    getUserId(session)
            );
        }
        else {
            return ChatroomResponseMapper.toPagingChatroomResponse(
                    chatroomService.getChatrooms(type, getUserId(session), PageHelper.toPageable(page, size)),
                    messageService,
                    messageStatusService,
                    getUserId(session)
            );
        }
    }

    @GetMapping(value = "/{chatroomId:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<ChatroomDetailResponse> getChatroom(@PathVariable String chatroomId,
                                                            Session session) {
        return ChatroomResponseMapper.toChatroomDetailDataResponse(chatroomService.getChatroom(chatroomId));
    }

    @GetMapping(value = "/{chatroomId:.+}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponse<MessageResponse> getMessages(
            @PathVariable String chatroomId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Session session
    ) {
        return ChatroomResponseMapper.toMessagePagingResponse(
                messageService.getMessages(chatroomId, PageHelper.toPageable(page, size)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<ChatroomDetailResponse> createChatroom(@RequestBody ChatroomRequest chatroomRequest,
                                                               Session session) {
        return ChatroomResponseMapper.toChatroomDetailDataResponse(chatroomService
                .createChatroom(chatroomRequestMapper.toChatroom(chatroomRequest, null)));
    }

    @PostMapping( value = "/{chatroomId:.+}/messages",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public BaseResponse createMessage(@PathVariable String chatroomId, @RequestBody MessageRequest messageRequest,
                                      Session session) {
        messageService.setMessageToAChatroom(messageRequestMapper
                .toMessage(messageRequest, getUserId(session), chatroomId), chatroomId, getUserId(session));
        return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{chatroomId:.+}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataResponse<ChatroomDetailResponse> updateChatroom(@PathVariable String chatroomId, @RequestBody ChatroomRequest chatroomRequest,
                                                               Session session) {
        return ChatroomResponseMapper.toChatroomDetailDataResponse(
                chatroomService.updateChatroom(chatroomRequestMapper.toChatroom(chatroomRequest, chatroomId)));
    }

    @PutMapping(value = "/{chatroomId:.+}/messages/{messageId:.+}/_read", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse updateMessageStatus(@PathVariable String chatroomId, @PathVariable String messageId,
                                            Session session) {
        messageStatusService.updateSeenStatus(chatroomId, messageId, getUserId(session));
        return ResponseHelper.toBaseResponse(HttpStatus.OK);
    }
    
    private String getUserId(Session session) {
        return userService.getUserByEmail(session.getEmail()).getId();
    }
}
