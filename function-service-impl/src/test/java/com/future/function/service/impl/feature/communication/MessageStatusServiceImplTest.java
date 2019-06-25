package com.future.function.service.impl.feature.communication;

import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.communication.chatting.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.MessageStatusRepository;
import com.future.function.service.api.feature.communication.ChatroomService;
import com.future.function.service.api.feature.communication.MessageService;
import com.future.function.service.api.feature.core.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Author: PriagungSatyagama
 * Created At: 18:18 05/06/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageStatusServiceImplTest {

  private static final String CHATROOM_ID = "chatroomid";

  private static final String MESSAGE_ID_1 = "messageid1";

  private static final String MESSAGE_ID_2 = "messageid2";

  private static final String MESSAGE_ID_3 = "messageid3";

  private static final String MESSAGE_ST_ID_1 = "statusid1";

  private static final String MESSAGE_ST_ID_2 = "statusid2";

  private static final String MESSAGE_ST_ID_3 = "statusid3";

  private static final String USER_ID = "userid";

  private static final Chatroom CHATROOM = Chatroom.builder()
          .id(CHATROOM_ID)
          .build();

  private Message MESSAGE_1 = Message.builder()
          .id(MESSAGE_ID_1)
          .build();

  private Message MESSAGE_2 = Message.builder()
          .id(MESSAGE_ID_2)
          .build();

  private Message MESSAGE_3 = Message.builder()
          .id(MESSAGE_ID_3)
          .build();

  private static final User USER = User.builder()
          .id(USER_ID)
          .build();

  private static final Long TIMESTAMP = 2L;

  private MessageStatus messageStatus1;
  private MessageStatus messageStatus2;
  private MessageStatus messageStatus3;

  @Mock
  private UserService userService;

  @Mock
  private MessageStatusRepository messageStatusRepository;

  @Mock
  private ChatroomService chatroomService;

  @Mock
  private MessageService messageService;

  @InjectMocks
  private MessageStatusServiceImpl messageStatusService;

  @Before
  public void setUp() {
    messageStatus1 = MessageStatus.builder()
            .seen(true)
            .member(USER)
            .message(MESSAGE_1)
            .chatroom(CHATROOM)
            .id(MESSAGE_ST_ID_1)
            .build();
    messageStatus1.setCreatedAt(1L);

    messageStatus2 = MessageStatus.builder()
            .seen(false)
            .member(USER)
            .message(MESSAGE_2)
            .chatroom(CHATROOM)
            .id(MESSAGE_ST_ID_2)
            .build();
    messageStatus2.setCreatedAt(2L);

    messageStatus3 = MessageStatus.builder()
            .seen(false)
            .member(USER)
            .message(MESSAGE_3)
            .chatroom(CHATROOM)
            .id(MESSAGE_ST_ID_3)
            .build();
    messageStatus3.setCreatedAt(3L);


    MESSAGE_3.setCreatedAt(3L);
    MESSAGE_2.setCreatedAt(2L);
    MESSAGE_1.setCreatedAt(1L);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(userService, messageService, messageStatusRepository, chatroomService);
  }

  @Test
  public void testGivenChatroomIdAndUserIdByGettingUnseenMessageStatusReturnListOfMessageStatus() {
    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(chatroomService.getChatroom(CHATROOM_ID)).thenReturn(CHATROOM);
    when(messageStatusRepository.findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(CHATROOM, USER))
            .thenReturn(Arrays.asList(messageStatus3, messageStatus2));

    List<MessageStatus> messageStatuses = messageStatusService.getUnseenMessageStatus(CHATROOM_ID, USER_ID);

    assertThat(messageStatuses.size()).isEqualTo(2);
    assertThat(messageStatuses.get(0).getId()).isEqualTo(MESSAGE_ST_ID_3);
    assertThat(messageStatuses.get(1).getId()).isEqualTo(MESSAGE_ST_ID_2);

    verify(userService).getUser(USER_ID);
    verify(chatroomService).getChatroom(CHATROOM_ID);
    verify(messageStatusRepository).findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(CHATROOM, USER);
  }

  @Test
  public void testGivenChatroomIdAndUserIdAndTimestampByGettingUnseenMessageStatusBeforeTimestampReturnListOfMessageStatus() {
    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(chatroomService.getChatroom(CHATROOM_ID)).thenReturn(CHATROOM);
    when(messageStatusRepository.findAllByChatroomAndMemberAndCreatedAtLessThanEqualAndSeenIsFalseOrderByCreatedAtDesc(
            CHATROOM, USER, TIMESTAMP)).thenReturn(Collections.singletonList(messageStatus2));

    List<MessageStatus> messageStatuses = messageStatusService.getUnseenMessageStatusBeforeTimestamp(
            CHATROOM_ID, USER_ID, TIMESTAMP);

    assertThat(messageStatuses.size()).isEqualTo(1);
    assertThat(messageStatuses.get(0).getId()).isEqualTo(MESSAGE_ST_ID_2);

    verify(userService).getUser(USER_ID);
    verify(chatroomService).getChatroom(CHATROOM_ID);
    verify(messageStatusRepository).findAllByChatroomAndMemberAndCreatedAtLessThanEqualAndSeenIsFalseOrderByCreatedAtDesc(
            CHATROOM, USER, TIMESTAMP);
  }

  @Test
  public void testGivenChatroomIdAndUserIdByGettingSeenStatusReturnBoolean() {
    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(chatroomService.getChatroom(CHATROOM_ID)).thenReturn(CHATROOM);
    when(messageStatusRepository.findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(CHATROOM, USER))
            .thenReturn(Arrays.asList(messageStatus3, messageStatus2));

    assertThat(messageStatusService.getSeenStatus(CHATROOM_ID, USER_ID)).isFalse();

    verify(userService).getUser(USER_ID);
    verify(chatroomService).getChatroom(CHATROOM_ID);
    verify(messageStatusRepository).findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(CHATROOM, USER);
  }

  @Test
  public void testGivenMessageStatusByCreatingMessageStatusReturnMessageStatus() {
    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(messageService.getMessage(MESSAGE_ID_1)).thenReturn(MESSAGE_1);
    when(chatroomService.getChatroom(CHATROOM_ID)).thenReturn(CHATROOM);
    when(messageStatusRepository.save(messageStatus1)).thenReturn(messageStatus1);

    MessageStatus messageStatusResult = messageStatusService.createMessageStatus(messageStatus1);

    assertThat(messageStatusResult).isNotNull();
    assertThat(messageStatusResult.getId()).isEqualTo(MESSAGE_ST_ID_1);
    assertThat(messageStatusResult.getMessage().getId()).isEqualTo(MESSAGE_ID_1);
    assertThat(messageStatusResult.getChatroom().getId()).isEqualTo(CHATROOM_ID);
    assertThat(messageStatusResult.getMember().getId()).isEqualTo(USER_ID);

    verify(userService).getUser(USER_ID);
    verify(chatroomService).getChatroom(CHATROOM_ID);
    verify(messageService).getMessage(MESSAGE_ID_1);
    verify(messageStatusRepository).save(messageStatus1);
  }

  @Test
  public void testGivenChatroomIdAndMessageIdByUpdatingSeenStatusReturnVoid() {
    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(chatroomService.getChatroom(CHATROOM_ID)).thenReturn(CHATROOM);
    when(messageStatusRepository.findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(
            CHATROOM, USER)).thenReturn(Arrays.asList(messageStatus3, messageStatus2));
    when(messageService.getMessage(MESSAGE_ID_3)).thenReturn(MESSAGE_3);
    when(messageService.getMessage(MESSAGE_ID_2)).thenReturn(MESSAGE_2);
    when(messageStatusRepository.save(messageStatus2)).thenReturn(messageStatus2);
    when(messageStatusRepository.save(messageStatus3)).thenReturn(messageStatus3);

    messageStatusService.updateSeenStatus(CHATROOM_ID, MESSAGE_ID_3, USER_ID);

    verify(userService, times(3)).getUser(USER_ID);
    verify(chatroomService, times(3)).getChatroom(CHATROOM_ID);
    verify(messageStatusRepository).findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(
            CHATROOM, USER);
    verify(messageService, times(2)).getMessage(MESSAGE_ID_3);
    verify(messageService).getMessage(MESSAGE_ID_2);
    verify(messageStatusRepository).save(messageStatus2);
    verify(messageStatusRepository).save(messageStatus3);
  }
}
