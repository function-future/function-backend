package com.future.function.web.controller.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.web.mapper.request.communication.MessageRequestMapper;
import com.future.function.web.mapper.response.communication.ChatroomResponseMapper;
import com.future.function.web.model.mq.ChatPayload;
import com.future.function.web.model.request.communication.MessageRequest;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatRedisListenerTest {
  String chatroomId = "chatroomId";
  String userId = "userId";
  MessageRequest messageRequest = MessageRequest.builder()
          .message("test")
          .build();
  ChatPayload chatPayload = ChatPayload.builder()
          .chatroomId(chatroomId)
          .userId(userId)
          .messageRequest(messageRequest)
          .build();
  ChatPayload chatPayloadPublic = ChatPayload.builder()
          .chatroomId("public")
          .userId(userId)
          .messageRequest(messageRequest)
          .build();
  User user1 = User.builder().id(userId).build();
  User user2 = User.builder().id("userId2").build();
  Chatroom chatroomPublic = Chatroom.builder()
          .id("public")
          .members(Arrays.asList(user1, user2))
          .type(ChatroomType.PUBLIC)
          .build();
  Chatroom chatroom = Chatroom.builder()
          .members(Arrays.asList(user1, user2))
          .type(ChatroomType.PRIVATE)
          .build();
  Message messagePublic = Message.builder().chatroom(chatroomPublic).sender(user1).build();
  Message message1 = Message.builder().chatroom(chatroom).sender(user1).build();
  Message message2 = Message.builder().chatroom(chatroom).sender(user2).build();

  org.springframework.data.redis.connection.Message message = new org.springframework.data.redis.connection.Message() {
    @Override
    public byte[] getBody() {
      return new byte[0];
    }

    @Override
    public byte[] getChannel() {
      return new byte[0];
    }
  };

  byte[] bytes = new byte[0];

  @Mock
  ObjectMapper objectMapper;

  @Mock
  MessageService messageService;

  @Mock
  ChatroomService chatroomService;

  @Mock
  MessageStatusService messageStatusService;

  @Mock
  MessageRequestMapper messageRequestMapper;

  @Mock
  SimpMessagingTemplate simpMessagingTemplate;

  @Mock
  FileProperties fileProperties;

  private static RedisTemplate<String, Object> redisTemplate;

  private static SetOperations<String, Object> redisSetOperations;

  @InjectMocks
  ChatRedisListener chatRedisListener;

  @BeforeClass
  public static void setupClass() {
    redisTemplate = mock(RedisTemplate.class);
    redisSetOperations = mock(SetOperations.class);

    when(redisTemplate.opsForSet()).thenReturn(redisSetOperations);
  }

  @AfterClass
  public static void tearDownClass() {

    int numberOfTestMethodInClass = 2;

    verify(redisTemplate, times(numberOfTestMethodInClass)).opsForSet();
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(objectMapper, messageService, chatroomService, messageStatusService, messageRequestMapper,
            simpMessagingTemplate, fileProperties);
  }

  @Test
  public void testGivenMessageAndBytesByCallingOnMessageReturnVoid1() throws IOException {
    when(objectMapper.readValue(message.getBody(), ChatPayload.class)).thenReturn(chatPayloadPublic);
    when(chatroomService.getPublicChatroom()).thenReturn(chatroomPublic);
    when(messageRequestMapper.toMessage(chatPayloadPublic.getMessageRequest(), chatPayloadPublic.getUserId(), chatPayloadPublic.getChatroomId()))
            .thenReturn(messagePublic);
    when(messageService.createMessage(messagePublic, chatPayloadPublic.getUserId())).thenReturn(messagePublic);
    when(fileProperties.getUrlPrefix()).thenReturn("test");
    doNothing().when(simpMessagingTemplate).convertAndSend("/topic/chatrooms/" + chatroomPublic.getId(),
            ChatroomResponseMapper.toMessageResponse(messagePublic, "test"));
    when(chatroomService.getChatroom(chatroomPublic.getId(), chatPayloadPublic.getUserId())).thenReturn(chatroomPublic);

    chatRedisListener.onMessage(message, bytes);

    verify(objectMapper).readValue(message.getBody(), ChatPayload.class);
    verify(chatroomService, times(2)).getPublicChatroom();
    verify(messageRequestMapper).toMessage(chatPayloadPublic.getMessageRequest(), chatPayloadPublic.getUserId(), chatPayloadPublic.getChatroomId());
    verify(messageService).createMessage(messagePublic, chatPayloadPublic.getUserId());
    verify(fileProperties).getUrlPrefix();
    verify(simpMessagingTemplate).convertAndSend("/topic/chatrooms/" + chatroomPublic.getId(),
            ChatroomResponseMapper.toMessageResponse(messagePublic, "test"));
    verify(chatroomService).getChatroom(chatroomPublic.getId(), chatPayloadPublic.getUserId());
  }

  @Test
  public void testGivenMessageAndBytesByCallingOnMessageReturnVoid2() throws IOException {
    when(objectMapper.readValue(message.getBody(), ChatPayload.class)).thenReturn(chatPayload);
    when(messageRequestMapper.toMessage(chatPayload.getMessageRequest(), chatPayload.getUserId(), chatPayload.getChatroomId()))
            .thenReturn(message1);
    when(messageService.createMessage(message1, chatPayload.getUserId())).thenReturn(message1);
    when(fileProperties.getUrlPrefix()).thenReturn("test");
    doNothing().when(simpMessagingTemplate).convertAndSend("/topic/chatrooms/" + chatroomId,
            ChatroomResponseMapper.toMessageResponse(message1, "test"));
    when(chatroomService.getChatroom(chatPayload.getChatroomId(), chatPayload.getUserId())).thenReturn(chatroom);
    when(messageStatusService.createMessageStatus(any(MessageStatus.class), anyString())).thenReturn(null);
    when(redisSetOperations.isMember("chatroom:" + chatroomId + ":active.user", userId)).thenReturn(true);
    when(chatroomService.getPublicChatroom()).thenReturn(chatroomPublic);

    chatRedisListener.onMessage(message, bytes);

    verify(objectMapper).readValue(message.getBody(), ChatPayload.class);
    verify(messageRequestMapper).toMessage(chatPayload.getMessageRequest(), chatPayload.getUserId(), chatPayload.getChatroomId());
    verify(messageService).createMessage(message1, chatPayload.getUserId());
    verify(fileProperties).getUrlPrefix();
    verify(simpMessagingTemplate).convertAndSend("/topic/chatrooms/" + chatroomId,
            ChatroomResponseMapper.toMessageResponse(message1, "test"));
    verify(chatroomService).getChatroom(chatPayload.getChatroomId(), chatPayload.getUserId());
    verify(messageStatusService, times(2)).createMessageStatus(any(MessageStatus.class), anyString());
    verify(redisSetOperations).isMember("chatroom:" + chatroomId + ":active.user", "userId2");
    verify(chatroomService).getPublicChatroom();

  }
}
