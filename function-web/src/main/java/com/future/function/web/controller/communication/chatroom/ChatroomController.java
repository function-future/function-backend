package com.future.function.web.controller.communication.chatroom;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.service.api.feature.communication.mq.MessagePublisherService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.ChatroomRequestMapper;
import com.future.function.web.mapper.request.communication.MessageRequestMapper;
import com.future.function.web.mapper.response.communication.ChatroomResponseMapper;
import com.future.function.web.model.mq.ChatPayload;
import com.future.function.web.model.request.communication.ChatroomRequest;
import com.future.function.web.model.request.communication.MessageRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/communication/chatrooms")
@WithAnyRole(roles = {
  Role.STUDENT, Role.MENTOR, Role.ADMIN, Role.JUDGE
})
public class ChatroomController {

  private ChatroomRequestMapper chatroomRequestMapper;

  private ChatroomService chatroomService;

  private MessageService messageService;

  private FileProperties fileProperties;

  private MessageStatusService messageStatusService;

  private MessagePublisherService publisherService;

  @Value("${function.mq.topic.chat}")
  private String mqTopicChat;

  @Autowired
  public ChatroomController(
          ChatroomRequestMapper chatroomRequestMapper,
          ChatroomService chatroomService, MessageService messageService,
          MessageStatusService messageStatusService,
          FileProperties fileProperties,
          MessagePublisherService publisherService) {

    this.fileProperties = fileProperties;
    this.chatroomRequestMapper = chatroomRequestMapper;
    this.chatroomService = chatroomService;
    this.messageService = messageService;
    this.messageStatusService = messageStatusService;
    this.publisherService = publisherService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<ChatroomResponse> getChatrooms(
    Session session,
    @RequestParam(required = false,
                  defaultValue = "PUBLIC")
      String type,
    @RequestParam(required = false)
      String search,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {

    if (search != null && !search.equals("")) {
      return ChatroomResponseMapper.toPagingChatroomResponse(
        chatroomService.getChatroomsWithKeyword(
          search, session.getUserId(), PageHelper.toPageable(page, size)),
        messageService, messageStatusService, fileProperties.getUrlPrefix(),
        session.getUserId()
      );
    } else {
      return ChatroomResponseMapper.toPagingChatroomResponse(
        chatroomService.getChatrooms(
          type, session.getUserId(), PageHelper.toPageable(page, size)),
        messageService, messageStatusService, fileProperties.getUrlPrefix(),
        session.getUserId()
      );
    }
  }

  @GetMapping(value = "/{chatroomId:.+}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ChatroomDetailResponse> getChatroom(
    Session session,
    @PathVariable
      String chatroomId
  ) {

    return ChatroomResponseMapper.toChatroomDetailDataResponse(
      chatroomService.getChatroom(chatroomId, session.getUserId()),
      fileProperties.getUrlPrefix()
    );
  }

  @GetMapping(value = "/{chatroomId:.+}/messages",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<MessageResponse> getMessages(
    Session session,
    @PathVariable
      String chatroomId,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {

    return ChatroomResponseMapper.toMessagePagingResponse(
      messageService.getMessages(chatroomId, PageHelper.toPageable(page, size),
                                 session.getUserId()
      ), fileProperties.getUrlPrefix());
  }

  @GetMapping(value = "/{chatroomId:.+}/messages/_after",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<MessageResponse> getMessagesAfterPivot(
    Session session,
    @PathVariable
      String chatroomId,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size,
    @RequestParam(required = true)
      String messageId
  ) {

    return ChatroomResponseMapper.toMessagePagingResponse(
      messageService.getMessagesAfterPivot(chatroomId, messageId,
                                           PageHelper.toPageable(page, size),
                                           session.getUserId()
      ), fileProperties.getUrlPrefix());
  }

  @GetMapping(value = "/{chatroomId:.+}/messages/_before",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<MessageResponse> getMessagesBeforePivot(
    Session session,
    @PathVariable
      String chatroomId,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size,
    @RequestParam(required = true)
      String messageId
  ) {

    return ChatroomResponseMapper.toMessagePagingResponse(
      messageService.getMessagesBeforePivot(chatroomId, messageId,
                                            PageHelper.toPageable(page, size),
                                            session.getUserId()
      ), fileProperties.getUrlPrefix());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ChatroomDetailResponse> createChatroom(
    Session session,
    @RequestBody
      ChatroomRequest chatroomRequest
  ) {

    return ChatroomResponseMapper.toChatroomDetailDataResponse(
      chatroomService.createChatroom(
        chatroomRequestMapper.toChatroom(chatroomRequest, null)),
      fileProperties.getUrlPrefix()
    );
  }

  @PostMapping(value = "/{chatroomId:.+}/messages",
               consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.CREATED)
  public BaseResponse createMessage(
    Session session,
    @PathVariable
      String chatroomId,
    @RequestBody
      MessageRequest messageRequest
  ) {
    publisherService.publish(ChatPayload.builder()
            .messageRequest(messageRequest)
            .chatroomId(chatroomId)
            .userId(session.getUserId())
            .build(), mqTopicChat);
    return ResponseHelper.toBaseResponse(HttpStatus.CREATED);
  }

  @PutMapping(value = "/{chatroomId:.+}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<ChatroomDetailResponse> updateChatroom(
    Session session,
    @PathVariable
      String chatroomId,
    @RequestBody
      ChatroomRequest chatroomRequest
  ) {

    return ChatroomResponseMapper.toChatroomDetailDataResponse(
      chatroomService.updateChatroom(
        chatroomRequestMapper.toChatroom(chatroomRequest, chatroomId),
        session.getUserId()
      ), fileProperties.getUrlPrefix());
  }

  @PutMapping(value = "/{chatroomId:.+}/messages/{messageId:.+}/_read",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse updateMessageStatus(
    Session session,
    @PathVariable
      String chatroomId,
    @PathVariable
      String messageId
  ) {

    if (chatroomId.equalsIgnoreCase("public")) {
      chatroomId = chatroomService.getPublicChatroom()
        .getId();
    }
    messageStatusService.updateSeenStatus(
      chatroomId, messageId, session.getUserId());
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @PostMapping(value = "/{chatroomId}/_enter", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse enterChatroom(Session session, @PathVariable String chatroomId) {
    chatroomService.enterChatroom(chatroomId, session.getUserId());
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @PostMapping(value = "/{chatroomId}/_leave", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse leaveChatroom(Session session, @PathVariable String chatroomId) {
    chatroomService.leaveChatroom(chatroomId, session.getUserId());
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

}
