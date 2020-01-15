package com.future.function.web.controller.communication.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.properties.communication.RedisProperties;
import com.future.function.common.properties.communication.WsProperties;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.communication.chatroom.MessageService;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.mapper.helper.PageHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ChatroomRedisListenerTest {

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private RedisProperties redisProperties;

  @Mock
  private SimpMessagingTemplate simpMessagingTemplate;

  @Mock
  private ChatroomService chatroomService;

  @Mock
  private MessageService messageService;

  @Mock
  private MessageStatusService messageStatusService;

  @Mock
  private UserService userService;

  @Mock
  private FileProperties fileProperties;

  @Mock
  private WsProperties wsProperties;

  private static RedisTemplate<String, Object> redisTemplate;

  private static ValueOperations<String, Object> valueOperations;

  @InjectMocks
  private ChatroomRedisListener chatroomRedisListener;

  private static final String USER_ID_1 = "user1";

  private static final String USER_ID_2 = "user2";

  private static final String USER_NAME_1 = "Priagung";

  private static final String USER_NAME_2 = "Satyagama";

  private static final String CHATROOM_ID = "CHATROOM_ID";

  private static final ChatroomType TYPE = ChatroomType.GROUP;

  private static final String TITLE_GROUP = "GRUP KELUARGA";

  private static final User MEMBER_1 = User.builder()
          .id(USER_ID_1)
          .name(USER_NAME_1)
          .build();

  private static final User MEMBER_2 = User.builder()
          .id(USER_ID_2)
          .name(USER_NAME_2)
          .build();

  private static final Chatroom chatroom = Chatroom.builder()
      .title(TITLE_GROUP)
      .id(CHATROOM_ID)
      .type(TYPE)
      .members(Arrays.asList(MEMBER_1, MEMBER_2))
      .build();

  Message message = new org.springframework.data.redis.connection.Message() {
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

  private String userId = "userId";
  private Long limit = 10L;

  @BeforeClass
  public static void setupClass() {
    redisTemplate = mock(RedisTemplate.class);
    valueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @AfterClass
  public static void tearDownClass() {

    int numberOfTestMethodInClass = 2;

    verify(redisTemplate, times(numberOfTestMethodInClass)).opsForValue();
  }

  @Test
  public void testGivenMessageByCallingOnMessageReturnVoid1() throws IOException {
    Map<String, String> key = new HashMap<>();
    Map<String, String> topic = new HashMap<>();
    key.put("limit-chatroom", "limit-chatroom/{userId}");
    topic.put("chatroom", "chatroom/{userId}");
    Pageable page = PageHelper.toPageable(1, limit.intValue());

    when(objectMapper.readValue(message.getBody(), String.class)).thenReturn(userId);
    when(redisProperties.getKey()).thenReturn(key);
    when(valueOperations.get("limit-chatroom/" + userId)).thenReturn(limit);
    when(objectMapper.convertValue(limit, Long.class)).thenReturn(limit);
    when(chatroomService.getChatrooms(userId, page)).thenReturn(new PageImpl<>(Collections.singletonList(chatroom), page, 1));
    when(fileProperties.getUrlPrefix()).thenReturn("prefix");
    when(wsProperties.getTopic()).thenReturn(topic);

    chatroomRedisListener.onMessage(message, bytes);

    verify(objectMapper).readValue(message.getBody(), String.class);
    verify(redisProperties).getKey();
    verify(valueOperations).get("limit-chatroom/" + userId);
    verify(objectMapper).convertValue(limit, Long.class);
    verify(chatroomService).getChatrooms(userId, page);
    verify(fileProperties).getUrlPrefix();
    verify(wsProperties).getTopic();

  }

  @Test
  public void testGivenMessageByCallingOnMessageReturnVoid2() throws IOException {
    Map<String, String> key = new HashMap<>();
    Map<String, String> topic = new HashMap<>();
    key.put("limit-chatroom", "limit-chatroom/{userId}");
    topic.put("chatroom", "chatroom/{userId}");
    Pageable page = PageHelper.toPageable(1, 1);

    when(objectMapper.readValue(message.getBody(), String.class)).thenReturn(userId);
    when(redisProperties.getKey()).thenReturn(key);
    when(valueOperations.get("limit-chatroom/" + userId)).thenReturn(limit);
    when(objectMapper.convertValue(limit, Long.class)).thenReturn(null);
    when(chatroomService.getChatrooms(userId, page)).thenReturn(new PageImpl<>(Collections.singletonList(chatroom), page, 1));
    when(fileProperties.getUrlPrefix()).thenReturn("prefix");
    when(wsProperties.getTopic()).thenReturn(topic);

    chatroomRedisListener.onMessage(message, bytes);

    verify(objectMapper).readValue(message.getBody(), String.class);
    verify(redisProperties).getKey();
    verify(valueOperations, times(2)).get("limit-chatroom/" + userId);
    verify(objectMapper).convertValue(limit, Long.class);
    verify(chatroomService).getChatrooms(userId, page);
    verify(fileProperties).getUrlPrefix();
    verify(wsProperties).getTopic();

  }

}
