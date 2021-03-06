package com.future.function.service.impl.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.properties.communication.MqProperties;
import com.future.function.common.properties.communication.RedisProperties;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.chatting.ChatroomRepository;
import com.future.function.service.api.feature.communication.chatroom.MessageStatusService;
import com.future.function.service.api.feature.communication.mq.MessagePublisherService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.feature.communication.chatroom.ChatroomServiceImpl;
import com.future.function.session.model.Session;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.*;
import org.springframework.web.util.UriTemplate;

import java.util.*;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ChatroomServiceImplTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String TITLE_GROUP = "GRUP KELUARGA";

  private static final String UPDATED_TITLE_GROUP = "GRUP MENTORING";

  private static final String KEYWORD = "KEL";

  private static final String CHATROOM_ID = "CHATROOM_ID";

  private static final String CHATROOM_ID_FAKE = "CHATROOM_ID_FAKE";

  private static final ChatroomType TYPE = ChatroomType.GROUP;

  private static final ChatroomType UPDATED_TYPE = ChatroomType.PUBLIC;

  private static final String USER_ID_1 = "user1";

  private static final String USER_ID_2 = "user2";

  private static final String USER_ID_3 = "user3";

  private static final String USER_NAME_1 = "Priagung";

  private static final String USER_NAME_2 = "Satyagama";

  private static final Session SESSION = Session.builder()
    .userId(USER_ID_1)
    .build();

  private static final Session SESSION_2 = Session.builder()
    .userId(USER_ID_3)
    .build();


  private static final User MEMBER_1 = User.builder()
    .id(USER_ID_1)
    .name(USER_NAME_1)
    .build();

  private static final User MEMBER_2 = User.builder()
    .id(USER_ID_2)
    .name(USER_NAME_2)
    .build();

  private static final User MEMBER_3 = User.builder()
    .id(USER_ID_3)
    .build();

  private Chatroom chatroom;

  @Mock
  private UserService userService;

  @Mock
  private ChatroomRepository chatroomRepository;

  @Mock
  private MessageStatusService messageStatusService;

  private static RedisProperties redisProperties;

  @Mock
  private MessagePublisherService messagePublisherService;

  @Mock
  private MqProperties mqProperties;

  @Mock
  private ResourceService resourceService;

  private static RedisTemplate<String, Object> redisTemplate;

  private static ValueOperations<String, Object> valueOperations;

  private static RedisOperations<String, Object> redisOperations;

  @BeforeClass
  public static void setupClass() {
    redisTemplate = mock(RedisTemplate.class);
    valueOperations = mock(ValueOperations.class);
    redisProperties = mock(RedisProperties.class);

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);

    Map<String, String> key = new HashMap<>();
    key.put("limit-chatroom", "limit-chatroom");
    when(redisProperties.getKey()).thenReturn(key);
  }

  @AfterClass
  public static void tearDownClass() {

    int numberOfTestMethodInClass = 15;

    verify(redisTemplate, times(numberOfTestMethodInClass)).opsForValue();
    verify(redisProperties, times(numberOfTestMethodInClass)).getKey();
  }

  @InjectMocks
  private ChatroomServiceImpl chatroomService;

  @Before
  public void setUp() {

    chatroom = Chatroom.builder()
      .title(TITLE_GROUP)
      .id(CHATROOM_ID)
      .type(TYPE)
      .members(Arrays.asList(MEMBER_1, MEMBER_2))
      .build();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(userService, chatroomRepository);
  }

  @Test
  public void testGivenTypeAndMemberByGettingChatroomByTypeAndMemberReturnPagedChatrooms() {

    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);
    when(chatroomRepository.findAllByMembersOrderByUpdatedAtDesc(
                                                                        MEMBER_1,
                                                                        PAGEABLE
    )).thenReturn(
      new PageImpl<>(Collections.singletonList(chatroom), PAGEABLE, 1));

    Page<Chatroom> chatroomPage = chatroomService.getChatrooms(
      USER_ID_1, PAGEABLE);

    assertThat(chatroomPage.getTotalElements()).isEqualTo(1);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getTitle()).isEqualTo(TITLE_GROUP);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getType()).isEqualTo(TYPE);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getMembers()
                 .size()).isEqualTo(2);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getMembers()
                 .contains(MEMBER_1)).isTrue();
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getMembers()
                 .contains(MEMBER_2)).isTrue();

    verify(userService).getUser(USER_ID_1);
    verify(chatroomRepository).findAllByMembersOrderByUpdatedAtDesc(MEMBER_1, PAGEABLE);
  }

  @Test
  public void testGivenKeywordAndMemberByGettingChatroomByKeywordAndMemberReturnPagedChatrooms() {

    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);
    when(
      chatroomRepository.findAllByTitleContainingIgnoreCaseAndMembersOrderByUpdatedAtDesc(
        KEYWORD, MEMBER_1, PAGEABLE)).thenReturn(
      new PageImpl<>(Collections.singletonList(chatroom), PAGEABLE, 1));

    Page<Chatroom> chatroomPage = chatroomService.getChatroomsWithKeyword(
      KEYWORD, USER_ID_1, PAGEABLE);

    assertThat(chatroomPage.getTotalElements()).isEqualTo(1);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getTitle()).isEqualTo(TITLE_GROUP);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getType()).isEqualTo(TYPE);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getMembers()
                 .size()).isEqualTo(2);
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getMembers()
                 .contains(MEMBER_1)).isTrue();
    assertThat(chatroomPage.getContent()
                 .get(0)
                 .getMembers()
                 .contains(MEMBER_2)).isTrue();

    verify(userService).getUser(USER_ID_1);
    verify(
      chatroomRepository).findAllByTitleContainingIgnoreCaseAndMembersOrderByUpdatedAtDesc(
      KEYWORD, MEMBER_1, PAGEABLE);
  }

  @Test
  public void testGivenChatroomIdByGettingChatroomByIdReturnChatroom() {

    when(chatroomRepository.findOne(CHATROOM_ID)).thenReturn(chatroom);
    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);

    Chatroom chatroomResult = chatroomService.getChatroom(
      CHATROOM_ID, SESSION.getUserId());

    assertThat(chatroomResult).isNotNull();
    assertThat(chatroomResult.getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomResult.getTitle()).isEqualTo(TITLE_GROUP);
    assertThat(chatroomResult.getType()).isEqualTo(TYPE);

    verify(chatroomRepository).findOne(CHATROOM_ID);
    verify(userService).getUser(USER_ID_1);
  }

  @Test
  public void testGivenChatroomIdByGettingChatroomByIdReturnNotFoundException() {

    when(chatroomRepository.findOne(CHATROOM_ID_FAKE)).thenReturn(null);

    catchException(
      () -> chatroomService.getChatroom(CHATROOM_ID_FAKE, SESSION.getUserId()));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Chatroom Not Found");

    verify(chatroomRepository).findOne(CHATROOM_ID_FAKE);
  }

  @Test
  public void testGivenChatroomIdByGettingChatroomByIdReturnForbiddenException() {

    when(chatroomRepository.findOne(CHATROOM_ID)).thenReturn(chatroom);
    when(userService.getUser(USER_ID_3)).thenReturn(MEMBER_3);

    catchException(
      () -> chatroomService.getChatroom(CHATROOM_ID, SESSION_2.getUserId()));

    assertThat(caughtException().getClass()).isEqualTo(
      ForbiddenException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Chatroom did not belong to this user");

    verify(chatroomRepository).findOne(CHATROOM_ID);
    verify(userService).getUser(USER_ID_3);
  }

  @Test
  public void testGivenChatroomByCreatingChatroomReturnChatroom() {

    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);
    when(userService.getUser(USER_ID_2)).thenReturn(MEMBER_2);
    when(chatroomRepository.save(chatroom)).thenReturn(chatroom);

    Chatroom chatroomResult = chatroomService.createChatroom(chatroom);

    assertThat(chatroomResult).isNotNull();
    assertThat(chatroomResult.getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomResult.getTitle()).isEqualTo(TITLE_GROUP);
    assertThat(chatroomResult.getType()).isEqualTo(TYPE);

    verify(userService).getUser(USER_ID_1);
    verify(userService).getUser(USER_ID_2);
    verify(chatroomRepository).save(chatroom);
  }

  @Test
  public void testGivenChatroomByCreatingPrivateChatroomReturnExistingChatroom() {

    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);
    when(userService.getUser(USER_ID_2)).thenReturn(MEMBER_2);
    when(chatroomRepository.save(chatroom)).thenReturn(chatroom);
    when(chatroomRepository.findAllByMembersContaining(
      Arrays.asList(MEMBER_1, MEMBER_2))).thenReturn(new ArrayList<>());

    chatroom.setType(ChatroomType.PRIVATE);

    Chatroom chatroomResult = chatroomService.createChatroom(chatroom);

    assertThat(chatroomResult).isNotNull();
    assertThat(chatroomResult.getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomResult.getType()).isEqualTo(ChatroomType.PRIVATE);

    verify(userService, times(2)).getUser(USER_ID_1);
    verify(userService, times(2)).getUser(USER_ID_2);
    verify(chatroomRepository).save(chatroom);
    verify(chatroomRepository).findAllByMembersContaining(
      Arrays.asList(MEMBER_1, MEMBER_2));
  }

  @Test
  public void testGivenChatroomByUpdatingChatroomReturnUpdatedChatroom() {

    Chatroom newChatroom = new Chatroom();
    BeanUtils.copyProperties(chatroom, newChatroom);
    newChatroom.setTitle(UPDATED_TITLE_GROUP);
    newChatroom.setType(UPDATED_TYPE);

    when(chatroomRepository.findOne(CHATROOM_ID)).thenReturn(chatroom);
    when(chatroomRepository.save(chatroom)).thenReturn(newChatroom);
    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);
    when(userService.getUser(USER_ID_2)).thenReturn(MEMBER_2);

    Chatroom chatroomResult = chatroomService.updateChatroom(
      newChatroom, SESSION.getUserId());

    assertThat(chatroomResult).isNotNull();
    assertThat(chatroomResult.getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomResult.getTitle()).isEqualTo(UPDATED_TITLE_GROUP);
    assertThat(chatroomResult.getType()).isEqualTo(UPDATED_TYPE);

    verify(chatroomRepository).findOne(CHATROOM_ID);
    verify(chatroomRepository).save(chatroom);
    verify(userService, times(2)).getUser(USER_ID_1);
    verify(userService).getUser(USER_ID_2);
  }

  @Test
  public void testGivenUserIdAndChatroomIdByCallingAuthorizeSubscriptionReturnVoid1() {
    when(chatroomRepository.findByType(ChatroomType.PUBLIC.name())).thenReturn(Optional.of(chatroom));
    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);

    chatroomService.authorizeSubscription(USER_ID_1, "public");

    verify(chatroomRepository).findByType(ChatroomType.PUBLIC.name());
    verify(userService).getUser(USER_ID_1);
  }

  @Test
  public void testGivenUserIdAndChatroomIdByCallingAuthorizeSubscriptionReturnVoid2() {
    when(chatroomRepository.findOne(CHATROOM_ID)).thenReturn(chatroom);
    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);

    chatroomService.authorizeSubscription(USER_ID_1, CHATROOM_ID);

    verify(chatroomRepository).findOne(CHATROOM_ID);
    verify(userService).getUser(USER_ID_1);
  }

  @Test
  public void testGivenUserIdByCallingSyncChatroomListReturnVoid() {
    Map<String, String> topic = new HashMap<>();
    topic.put("chatroom", "chatroom");
    when(mqProperties.getTopic()).thenReturn(topic);
    doNothing().when(messagePublisherService).publish(USER_ID_1, "chatroom");

    chatroomService.syncChatroomList(USER_ID_1);

    verify(mqProperties).getTopic();
    verify(messagePublisherService).publish(USER_ID_1, "chatroom");
  }

  @Test
  public void testGivenUserIdAndLimitByCallingSetLimitChatroomsReturnVoid() {
    Map<String, String> topic = new HashMap<>();
    topic.put("chatroom", "chatroom");

    when(mqProperties.getTopic()).thenReturn(topic);
    doNothing().when(messagePublisherService).publish(USER_ID_1, "chatroom");
    chatroomService.setLimitChatrooms(USER_ID_1, 2);

    verify(mqProperties).getTopic();
    verify(messagePublisherService).publish(USER_ID_1, "chatroom");
  }

  @Test
  public void testGivenUserIdAndLimitByCallingUnsetLimitChatroomsReturnVoid() {
    Map<String, String> key = new HashMap<>();
    key.put("limit-chatroom", "limit-chatroom");

    redisOperations = mock(RedisOperations.class);
    when(valueOperations.getOperations()).thenReturn(redisOperations);
    doNothing().when(redisOperations).delete(USER_ID_1);

    chatroomService.unsetLimitChatrooms(USER_ID_1);

    verify(valueOperations).getOperations();
    verify(redisOperations).delete(key.get("limit-chatroom"));
  }

  @Test
  public void testGivenChatroomByCallingSyncChatroomListReturnChatroom() {
    Map<String, String> topic = new HashMap<>();
    topic.put("chatroom", "chatroom");
    when(mqProperties.getTopic()).thenReturn(topic);
    doNothing().when(messagePublisherService).publish(USER_ID_1, "chatroom");
    doNothing().when(messagePublisherService).publish(USER_ID_2, "chatroom");

    chatroomService.syncChatroomList(chatroom);

    verify(mqProperties, times(2)).getTopic();
    verify(messagePublisherService).publish(USER_ID_1, "chatroom");
    verify(messagePublisherService).publish(USER_ID_2, "chatroom");
  }

  @Test
  public void testGivenChatroomByCallingUpdateDateReturnChatroom() {
    when(chatroomRepository.save(chatroom)).thenReturn(chatroom);

    chatroomService.updateDate(chatroom);

    verify(chatroomRepository).save(chatroom);
  }
}
