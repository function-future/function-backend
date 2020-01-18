package com.future.function.web.controller.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.communication.MqProperties;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.service.api.feature.communication.mq.MessagePublisherService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.controller.communication.chatroom.ChatroomController;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.communication.ChatroomRequestMapper;
import com.future.function.web.mapper.request.communication.MessageRequestMapper;
import com.future.function.web.mapper.response.communication.ChatroomResponseMapper;
import com.future.function.web.model.mq.ChatPayload;
import com.future.function.web.model.request.communication.ChatroomLimitRequest;
import com.future.function.web.model.request.communication.ChatroomRequest;
import com.future.function.web.model.request.communication.MessageRequest;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomDetailResponse;
import com.future.function.web.model.response.feature.communication.chatting.ChatroomResponse;
import com.future.function.web.model.response.feature.communication.chatting.MessageResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(ChatroomController.class)
public class ChatroomControllerTest extends TestHelper {

  private final String CHATROOM_ID = "chatroomId";

  private final String CHATROOM_TITLE = "chatroomTitle";

  private final ChatroomType CHATROOM_TYPE = ChatroomType.GROUP;

  private final String MESSAGE_ID = "messageId";

  private final String MEMBER_ID_1 = "admin-id";

  private final String MEMBER_ID_2 = "idUser2";

  private final String URL_PREFIX = "localhost:8080";

  private final User MEMBER_1 = User.builder()
    .id(MEMBER_ID_1)
    .build();

  private final User MEMBER_2 = User.builder()
    .id(MEMBER_ID_2)
    .build();

  private final Chatroom CHATROOM = Chatroom.builder()
    .type(CHATROOM_TYPE)
    .members(Arrays.asList(MEMBER_1, MEMBER_2))
    .title(CHATROOM_TITLE)
    .id(CHATROOM_ID)
    .build();

  private final ChatroomRequest CHATROOM_WEB_REQUEST = ChatroomRequest.builder()
    .members(Arrays.asList(MEMBER_ID_1, MEMBER_ID_2))
    .name(CHATROOM_TITLE)
    .build();

  private final DataResponse<ChatroomDetailResponse>
    CHATROOM_DETAIL_DATA_RESPONSE = new DataResponse<ChatroomDetailResponse>();

  private final Pageable PAGEABLE = new PageRequest(0, 10);

  private final MessageRequest MESSAGE_REQUEST = MessageRequest.builder()
    .message("test")
    .build();

  private Message MESSAGE = Message.builder()
    .id(MESSAGE_ID)
    .chatroom(CHATROOM)
    .text("test")
    .sender(MEMBER_1)
    .build();

  private JacksonTester<ChatroomRequest> chatroomRequestJacksonTester;

  private JacksonTester<MessageRequest> messageRequestJacksonTester;

  private JacksonTester<ChatroomLimitRequest> chatroomLimitRequestJacksonTester;

  @MockBean
  private MqProperties mqProperties;

  @MockBean
  private ChatroomRequestMapper chatroomRequestMapper;

  @MockBean
  private MessageRequestMapper messageRequestMapper;

  @MockBean
  private ChatroomService chatroomService;

  @MockBean
  private MessageService messageService;

  @MockBean
  private FileProperties fileProperties;

  @MockBean
  private MessageStatusService messageStatusService;

  @MockBean
  private UserService userService;

  @MockBean
  private MessagePublisherService publisherService;

  @MockBean
  private ResourceService resourceService;

  @Override
  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.ADMIN);
    when(userService.getUserByEmail(ADMIN_EMAIL)).thenReturn(MEMBER_1);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(chatroomRequestMapper, messageRequestMapper,
                             chatroomService, messageService,
                             messageStatusService
    );
  }

  @Test
  public void testGivenCalToChatroomsApiByGettingChatroomByIdReturnDataResponseChatroomDetail()
    throws Exception {

    when(chatroomService.getChatroom(CHATROOM_ID,
                                     ADMIN_SESSION.getUserId()
    )).thenReturn(CHATROOM);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    mockMvc.perform(
      get("/api/communication/chatrooms/" + CHATROOM_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CHATROOM_DETAIL_DATA_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(chatroomService).getChatroom(CHATROOM_ID, ADMIN_SESSION.getUserId());
  }

  @Test
  public void testGivenCallToChatroomsWithKeywordApiByGettingChatroomsReturnPagingResponse()
    throws Exception {

    String KEYWORD = "chat";
    MESSAGE.setCreatedAt(1L);

    when(chatroomService.getChatroomsWithKeyword(KEYWORD, MEMBER_ID_1,
                                                 PAGEABLE
    )).thenReturn(
      new PageImpl<>(Arrays.asList(CHATROOM, CHATROOM), PAGEABLE, 2));
    when(messageService.getLastMessage(CHATROOM_ID,
                                       ADMIN_SESSION.getUserId()
    )).thenReturn(MESSAGE);
    when(messageStatusService.getSeenStatus(CHATROOM_ID,
                                            ADMIN_SESSION.getUserId()
    )).thenReturn(false);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<ChatroomResponse> response =
      ChatroomResponseMapper.toPagingChatroomResponse(
        chatroomService.getChatroomsWithKeyword(KEYWORD, MEMBER_ID_1, PAGEABLE),
        messageService, messageStatusService, userService, resourceService, URL_PREFIX,
        ADMIN_SESSION.getUserId()
      );
    when(userService.getUser(ADMIN_SESSION.getUserId())).thenReturn(MEMBER_1);

    mockMvc.perform(get("/api/communication/chatrooms").cookie(cookies)
                      .param("search", KEYWORD))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(chatroomService, times(2)).getChatroomsWithKeyword(
      KEYWORD, MEMBER_ID_1, PAGEABLE);
    verify(messageService, times(4)).getLastMessage(
      CHATROOM_ID, ADMIN_SESSION.getUserId());
    verify(messageStatusService, times(4)).getSeenStatus(
      CHATROOM_ID, ADMIN_SESSION.getUserId());
    verify(fileProperties).getUrlPrefix();
    verify(userService, times(4)).getUser(ADMIN_SESSION.getUserId());

  }

  @Test
  public void testGivenCallToChatroomsApiByGettingChatroomsReturnPagingResponse()
    throws Exception {

    MESSAGE.setCreatedAt(1L);

    when(
      chatroomService.getChatrooms(MEMBER_ID_1, PAGEABLE)).thenReturn(
      new PageImpl<>(Arrays.asList(CHATROOM, CHATROOM), PAGEABLE, 2));
    when(messageService.getLastMessage(CHATROOM_ID,
                                       ADMIN_SESSION.getUserId()
    )).thenReturn(MESSAGE);
    when(messageStatusService.getSeenStatus(CHATROOM_ID,
                                            ADMIN_SESSION.getUserId()
    )).thenReturn(false);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);
    when(userService.getUser(ADMIN_SESSION.getUserId())).thenReturn(MEMBER_1);

    PagingResponse<ChatroomResponse> response =
      ChatroomResponseMapper.toPagingChatroomResponse(
        chatroomService.getChatrooms(MEMBER_ID_1, PAGEABLE),
        messageService, messageStatusService, userService, resourceService, URL_PREFIX,
        ADMIN_SESSION.getUserId()
      );

    mockMvc.perform(get("/api/communication/chatrooms").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(chatroomService, times(2)).getChatrooms(
      MEMBER_ID_1, PAGEABLE);
    verify(messageService, times(4)).getLastMessage(
      CHATROOM_ID, ADMIN_SESSION.getUserId());
    verify(messageStatusService, times(4)).getSeenStatus(
      CHATROOM_ID, ADMIN_SESSION.getUserId());
    verify(fileProperties).getUrlPrefix();
    verify(userService, times(4)).getUser(ADMIN_SESSION.getUserId());

  }

  @Test
  public void testGivenCallToChatroomsApiByGettingMessagesReturnPaging()
    throws Exception {

    Page<Message> messagePage = new PageImpl<>(
      Arrays.asList(MESSAGE, MESSAGE), PAGEABLE, 2);
    when(messageService.getMessages(CHATROOM_ID, PAGEABLE,
                                    ADMIN_SESSION.getUserId()
    )).thenReturn(messagePage);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<MessageResponse> response =
      ChatroomResponseMapper.toMessagePagingResponse(messagePage, URL_PREFIX);

    mockMvc.perform(
      get("/api/communication/chatrooms/" + CHATROOM_ID + "/messages").cookie(
        cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(messageService).getMessages(
      CHATROOM_ID, PAGEABLE, ADMIN_SESSION.getUserId());
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void testGivenCallToChatroomsApiByGettingMessagesBeforePivotReturnPaging()
    throws Exception {

    String messageId = "messageId";

    Page<Message> messagePage = new PageImpl<>(
      Arrays.asList(MESSAGE, MESSAGE), PAGEABLE, 2);
    when(messageService.getMessagesBeforePivot(CHATROOM_ID, messageId, PAGEABLE,
                                               ADMIN_SESSION.getUserId()
    )).thenReturn(messagePage);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<MessageResponse> response =
      ChatroomResponseMapper.toMessagePagingResponse(messagePage, URL_PREFIX);

    mockMvc.perform(get("/api/communication/chatrooms/" + CHATROOM_ID +
                        "/messages/_before").param("messageId", messageId)
                      .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(messageService).getMessagesBeforePivot(
      CHATROOM_ID, messageId, PAGEABLE, ADMIN_SESSION.getUserId());
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void testGivenCallToChatroomsApiByGettingMessagesAfterPivotReturnPaging()
    throws Exception {

    String messageId = "messageId";

    Page<Message> messagePage = new PageImpl<>(
      Arrays.asList(MESSAGE, MESSAGE), PAGEABLE, 2);
    when(messageService.getMessagesAfterPivot(CHATROOM_ID, messageId, PAGEABLE,
                                              ADMIN_SESSION.getUserId()
    )).thenReturn(messagePage);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    PagingResponse<MessageResponse> response =
      ChatroomResponseMapper.toMessagePagingResponse(messagePage, URL_PREFIX);

    mockMvc.perform(get(
      "/api/communication/chatrooms/" + CHATROOM_ID + "/messages/_after").param(
      "messageId", messageId)
                      .cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response)
                                  .getJson()));

    verify(messageService).getMessagesAfterPivot(
      CHATROOM_ID, messageId, PAGEABLE, ADMIN_SESSION.getUserId());
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void testGivenChatroomDataByCreatingChatroomReturnDataResponseChatroom()
    throws Exception {

    super.setCookie(Role.ADMIN);

    when(
      chatroomRequestMapper.toChatroom(CHATROOM_WEB_REQUEST, null)).thenReturn(
      CHATROOM);
    when(chatroomService.createChatroom(CHATROOM)).thenReturn(CHATROOM);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    DataResponse<ChatroomDetailResponse> response =
      ChatroomResponseMapper.toChatroomDetailDataResponse(CHATROOM, URL_PREFIX, resourceService);

    mockMvc.perform(post("/api/communication/chatrooms").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(
                        chatroomRequestJacksonTester.write(CHATROOM_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(response)
                                  .getJson()));

    verify(chatroomRequestMapper).toChatroom(CHATROOM_WEB_REQUEST, null);
    verify(chatroomService).createChatroom(CHATROOM);
    verify(fileProperties).getUrlPrefix();

  }

  @Test
  public void testGivenMessageDataByCreatingMessageReturnBaseResponseCreated()
    throws Exception {

    super.setCookie(Role.ADMIN);

    Map<String, String> topic = new HashMap<>();
    topic.put("chat", "chat");

    when(mqProperties.getTopic()).thenReturn(topic);

    ChatPayload chatPayload = ChatPayload.builder()
            .messageRequest(MESSAGE_REQUEST)
            .chatroomId(CHATROOM_ID)
            .userId(ADMIN_SESSION.getUserId())
            .build();

    mockMvc.perform(post(
      "/api/communication/chatrooms/" + CHATROOM_ID + "/messages").cookie(
      cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(
                        messageRequestJacksonTester.write(MESSAGE_REQUEST)
                          .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(baseResponseJacksonTester.write(
        ResponseHelper.toBaseResponse(HttpStatus.CREATED))
                                  .getJson()));

    verify(mqProperties).getTopic();
    verify(publisherService).publish(chatPayload, topic.get("chat"));
  }

  @Test
  public void testGivenChatroomDataByUpdatingChatroomReturnBaseResponseOk()
    throws Exception {

    super.setCookie(Role.ADMIN);

    when(chatroomRequestMapper.toChatroom(CHATROOM_WEB_REQUEST,
                                          CHATROOM_ID
    )).thenReturn(CHATROOM);
    when(chatroomService.updateChatroom(CHATROOM,
                                        ADMIN_SESSION.getUserId()
    )).thenReturn(CHATROOM);
    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    mockMvc.perform(put("/api/communication/chatrooms/" + CHATROOM_ID).cookie(
      cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(
                        chatroomRequestJacksonTester.write(CHATROOM_WEB_REQUEST)
                          .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(
        ResponseHelper.toBaseResponse(HttpStatus.OK))
                                  .getJson()));

    verify(chatroomRequestMapper).toChatroom(CHATROOM_WEB_REQUEST, CHATROOM_ID);
    verify(chatroomService).updateChatroom(CHATROOM, ADMIN_SESSION.getUserId());
    verify(fileProperties).getUrlPrefix();
  }

  @Test
  public void testGivenCallToChatroomsApiByUpdatingMessageStatusReturnBaseResponseOk()
    throws Exception {

    super.setCookie(Role.ADMIN);

    doNothing().when(messageStatusService)
      .updateSeenStatus(CHATROOM_ID, MESSAGE_ID, ADMIN_SESSION.getUserId(), false);

    mockMvc.perform(put(
      "/api/communication/chatrooms/" + CHATROOM_ID + "/messages/" +
      MESSAGE_ID + "/_read").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(
        ResponseHelper.toBaseResponse(HttpStatus.OK))
                                  .getJson()));

    verify(messageStatusService).updateSeenStatus(
      CHATROOM_ID, MESSAGE_ID, ADMIN_SESSION.getUserId(), false);
  }

  @Test
  public void testGivenCallToChatroomsApiByEnteringChatroomReturnBaseResponseOk()
          throws Exception {

    super.setCookie(Role.ADMIN);

    doNothing().when(messageStatusService)
            .enterChatroom(CHATROOM_ID, ADMIN_SESSION.getUserId());

    mockMvc.perform(post(
            "/api/communication/chatrooms/" + CHATROOM_ID + "/_enter").cookie(cookies))
            .andExpect(status().isOk())
            .andExpect(content().json(baseResponseJacksonTester.write(
                    ResponseHelper.toBaseResponse(HttpStatus.OK))
                    .getJson()));

    verify(messageStatusService).enterChatroom(CHATROOM_ID, ADMIN_SESSION.getUserId());
  }

  @Test
  public void testGivenCallToChatroomsApiByLeavingChatroomReturnBaseResponseOk()
          throws Exception {

    super.setCookie(Role.ADMIN);

    doNothing().when(messageStatusService)
            .leaveChatroom(CHATROOM_ID, ADMIN_SESSION.getUserId());

    mockMvc.perform(post(
            "/api/communication/chatrooms/" + CHATROOM_ID + "/_leave").cookie(cookies))
            .andExpect(status().isOk())
            .andExpect(content().json(baseResponseJacksonTester.write(
                    ResponseHelper.toBaseResponse(HttpStatus.OK))
                    .getJson()));

    verify(messageStatusService).leaveChatroom(CHATROOM_ID, ADMIN_SESSION.getUserId());
  }

  @Test
  public void testGivenCallToChatroomsApiBySetLimitReturnBaseResponseOk()
          throws Exception {

    super.setCookie(Role.ADMIN);

    ChatroomLimitRequest request = ChatroomLimitRequest.builder()
            .limit(5L)
            .build();

    doNothing().when(chatroomService)
            .setLimitChatrooms(ADMIN_SESSION.getUserId(), request.getLimit());

    mockMvc.perform(post(
            "/api/communication/chatrooms/_setlimit").cookie(cookies)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(
                    chatroomLimitRequestJacksonTester.write(request)
                            .getJson()))
            .andExpect(status().isOk())
            .andExpect(content().json(baseResponseJacksonTester.write(
                    ResponseHelper.toBaseResponse(HttpStatus.OK))
                    .getJson()));

    verify(chatroomService).setLimitChatrooms(ADMIN_SESSION.getUserId(), request.getLimit());
  }

}
