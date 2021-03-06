package com.future.function.service.impl.feature.communication;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.chatting.MessageRepository;
import com.future.function.service.api.feature.communication.chatroom.ChatroomService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.feature.communication.chatroom.MessageServiceImpl;
import com.future.function.session.model.Session;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageServiceImplTest {

  private static final String CHATROOM_ID = "chatroom";

  private static final String USER_ID = "user";

  private static final String MESSAGE_ID_1 = "message1";

  private static final String MESSAGE_ID_2 = "message2";

  private static final String MESSAGE_TEXT_1 = "text message1";

  private static final String MESSAGE_TEXT_2 = "text message2";

  private static final Session SESSION = Session.builder()
    .userId(USER_ID)
    .build();

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final Chatroom CHATROOM = Chatroom.builder()
    .id(CHATROOM_ID)
    .build();

  private static final User USER = User.builder()
    .id(USER_ID)
    .build();

  private Message message1;

  private Message message2;

  @Mock
  private UserService userService;

  @Mock
  private ChatroomService chatroomService;

  @Mock
  private MessageRepository messageRepository;

  @InjectMocks
  private MessageServiceImpl messageService;

  @Before
  public void setUp() {

    message1 = Message.builder()
      .id(MESSAGE_ID_1)
      .text(MESSAGE_TEXT_1)
      .build();

    message2 = Message.builder()
      .id(MESSAGE_ID_2)
      .text(MESSAGE_TEXT_2)
      .build();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(userService, chatroomService, messageRepository);
  }

  @Test
  public void testGivenChatroomByGettingMessagesByChatroomReturnPagedMessages() {

    when(
      chatroomService.getChatroom(CHATROOM_ID, SESSION.getUserId())).thenReturn(
      CHATROOM);
    when(messageRepository.findAllByChatroomOrderByCreatedAtDesc(CHATROOM,
                                                                 PAGEABLE
    )).thenReturn(
      new PageImpl<>(Arrays.asList(message2, message1), PAGEABLE, 2));

    Page<Message> messagePage = messageService.getMessages(
      CHATROOM_ID, PAGEABLE, SESSION.getUserId());

    assertThat(messagePage.getTotalElements()).isEqualTo(2);
    assertThat(messagePage.getContent()
                 .get(0)
                 .getId()).isEqualTo(MESSAGE_ID_2);
    assertThat(messagePage.getContent()
                 .get(1)
                 .getId()).isEqualTo(MESSAGE_ID_1);

    verify(chatroomService).getChatroom(CHATROOM_ID, SESSION.getUserId());
    verify(messageRepository).findAllByChatroomOrderByCreatedAtDesc(
      CHATROOM, PAGEABLE);
  }

  @Test
  public void testGivenChatroomAndMessageIdByGettingMessagesByChatroomAndMessageIdGreaterReturnPagedMessages() {

    ObjectId objectId = new ObjectId();
    when(
      chatroomService.getChatroom(CHATROOM_ID, SESSION.getUserId())).thenReturn(
      CHATROOM);
    when(
      messageRepository.findAllByChatroomAndIdGreaterThanOrderByCreatedAtDesc(
        CHATROOM, objectId, PAGEABLE)).thenReturn(
      new PageImpl<>(Arrays.asList(message2, message1), PAGEABLE, 2));

    Page<Message> messagePage = messageService.getMessagesAfterPivot(
      CHATROOM_ID, objectId.toHexString(), PAGEABLE, SESSION.getUserId());

    assertThat(messagePage.getTotalElements()).isEqualTo(2);
    assertThat(messagePage.getContent()
                 .get(0)
                 .getId()).isEqualTo(MESSAGE_ID_2);
    assertThat(messagePage.getContent()
                 .get(1)
                 .getId()).isEqualTo(MESSAGE_ID_1);

    verify(chatroomService).getChatroom(CHATROOM_ID, SESSION.getUserId());
    verify(
      messageRepository).findAllByChatroomAndIdGreaterThanOrderByCreatedAtDesc(
      CHATROOM, objectId, PAGEABLE);
  }

  @Test
  public void testGivenChatroomAndMessageIdByGettingMessagesByChatroomAndMessageIdLessReturnPagedMessages() {

    ObjectId objectId = new ObjectId();
    when(
      chatroomService.getChatroom(CHATROOM_ID, SESSION.getUserId())).thenReturn(
      CHATROOM);
    when(messageRepository.findAllByChatroomAndIdLessThanOrderByCreatedAtDesc(
      CHATROOM, objectId, PAGEABLE)).thenReturn(
      new PageImpl<>(Arrays.asList(message2, message1), PAGEABLE, 2));

    Page<Message> messagePage = messageService.getMessagesBeforePivot(
      CHATROOM_ID, objectId.toHexString(), PAGEABLE, SESSION.getUserId());

    assertThat(messagePage.getTotalElements()).isEqualTo(2);
    assertThat(messagePage.getContent()
                 .get(0)
                 .getId()).isEqualTo(MESSAGE_ID_2);
    assertThat(messagePage.getContent()
                 .get(1)
                 .getId()).isEqualTo(MESSAGE_ID_1);

    verify(chatroomService).getChatroom(CHATROOM_ID, SESSION.getUserId());
    verify(
      messageRepository).findAllByChatroomAndIdLessThanOrderByCreatedAtDesc(
      CHATROOM, objectId, PAGEABLE);
  }

  @Test
  public void testGivenMessageIdByGettingMessageByMessageIdReturnMessage() {

    when(messageRepository.findOne(MESSAGE_ID_1)).thenReturn(message1);

    Message message = messageService.getMessage(MESSAGE_ID_1);

    assertThat(message).isNotNull();
    assertThat(message.getId()).isEqualTo(MESSAGE_ID_1);

    verify(messageRepository).findOne(MESSAGE_ID_1);
  }

  @Test
  public void testGivenMessageIdByGettingMessageByMessageIdReturnNotFoundException() {

    when(messageRepository.findOne(MESSAGE_ID_1)).thenReturn(null);

    catchException(() -> messageService.getMessage(MESSAGE_ID_1));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Message not found");

    verify(messageRepository).findOne(MESSAGE_ID_1);
  }

  @Test
  public void testGivenChatroomByGettingLastMessageByChatroomReturnMessage() {

    when(
      chatroomService.getChatroom(CHATROOM_ID, SESSION.getUserId())).thenReturn(
      CHATROOM);
    when(messageRepository.findFirstByChatroomOrderByCreatedAtDesc(
      CHATROOM)).thenReturn(message2);

    Message messageResult = messageService.getLastMessage(
      CHATROOM_ID, SESSION.getUserId());

    assertThat(messageResult).isNotNull();
    assertThat(messageResult.getId()).isEqualTo(MESSAGE_ID_2);
    assertThat(messageResult.getText()).isEqualTo(MESSAGE_TEXT_2);

    verify(chatroomService).getChatroom(CHATROOM_ID, SESSION.getUserId());
    verify(messageRepository).findFirstByChatroomOrderByCreatedAtDesc(CHATROOM);
  }

  @Test
  public void testGivenChatroomByCreatingChatroomReturnChatroom() {

    when(messageRepository.save(message1)).thenReturn(message1);
    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(
      chatroomService.getChatroom(CHATROOM_ID, SESSION.getUserId())).thenReturn(
      CHATROOM);
    when(
      chatroomService.updateDate(CHATROOM)).thenReturn(
      CHATROOM);

    message1.setChatroom(CHATROOM);
    message1.setSender(USER);

    Message messageResult = messageService.createMessage(
      message1, SESSION.getUserId());

    assertThat(messageResult).isNotNull();
    assertThat(messageResult.getId()).isEqualTo(MESSAGE_ID_1);
    assertThat(messageResult.getText()).isEqualTo(MESSAGE_TEXT_1);

    verify(messageRepository).save(message1);
    verify(userService).getUser(USER_ID);
    verify(chatroomService).getChatroom(CHATROOM_ID, SESSION.getUserId());
    verify(chatroomService).updateDate(CHATROOM);
  }

}
