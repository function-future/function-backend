package com.future.function.repository.feature.communication;

import com.future.function.model.entity.feature.communication.Chatroom;
import com.future.function.model.entity.feature.communication.Message;
import com.future.function.model.entity.feature.communication.MessageStatus;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Author: PriagungSatyagama
 * Created At: 14:23 01/06/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class MessageStatusRepositoryTest {

  private static final Chatroom ROOM_1 = Chatroom.builder()
          .id("room1")
          .build();

  private static final Message MESSAGE_1 = Message.builder()
          .id("message1")
          .text("text1")
          .chatroom(ROOM_1)
          .build();

  private static final Message MESSAGE_2 = Message.builder()
          .id("message2")
          .text("text2")
          .chatroom(ROOM_1)
          .build();

  private static final Message MESSAGE_3 = Message.builder()
          .id("message3")
          .text("text3")
          .chatroom(ROOM_1)
          .build();

  private static final User MEMBER_1 = User.builder()
          .id("user1")
          .name("member1")
          .build();

  @Autowired
  private MessageStatusRepository messageStatusRepository;

  @Before
  public void setUp() {
    MessageStatus messageStatus1 = MessageStatus.builder()
            .message(MESSAGE_1)
            .chatroom(ROOM_1)
            .member(MEMBER_1)
            .seen(true)
            .build();
    messageStatus1.setCreatedAt(1L);

    MessageStatus messageStatus2 = MessageStatus.builder()
            .message(MESSAGE_2)
            .chatroom(ROOM_1)
            .member(MEMBER_1)
            .seen(false)
            .build();
    messageStatus2.setCreatedAt(2L);

    MessageStatus messageStatus3 = MessageStatus.builder()
            .message(MESSAGE_3)
            .chatroom(ROOM_1)
            .member(MEMBER_1)
            .seen(false)
            .build();
    messageStatus3.setCreatedAt(3L);

    messageStatusRepository.save(messageStatus1);
    messageStatusRepository.save(messageStatus2);
    messageStatusRepository.save(messageStatus3);

  }

  @After
  public void tearDown() {
    messageStatusRepository.deleteAll();
  }

  @Test
  public void testGivenMemberAndChatroomByFindingAllUnseenMessageStatusReturnListOfMessageStatus() {

    List<MessageStatus> messageStatuses = messageStatusRepository
            .findAllByChatroomAndMemberAndSeenIsFalseOrderByCreatedAtDesc(
              ROOM_1, MEMBER_1
            );


    assertThat(messageStatuses.size()).isEqualTo(2);
    assertThat(messageStatuses.get(0).isSeen()).isEqualTo(false);
    assertThat(messageStatuses.get(1).isSeen()).isEqualTo(false);

  }

  @Test
  public void testGivenMemberAndChatroomAndTimestampByFindingAllUnseenMessageStatusBeforeTimestampReturnListOfMessageStatus() {

    List<MessageStatus> messageStatuses1 = messageStatusRepository
            .findAllByChatroomAndMemberAndCreatedAtLessThanEqualAndSeenIsFalseOrderByCreatedAtDesc(
                    ROOM_1, MEMBER_1, 2l
            );

    List<MessageStatus> messageStatuses2 = messageStatusRepository
            .findAllByChatroomAndMemberAndCreatedAtLessThanEqualAndSeenIsFalseOrderByCreatedAtDesc(
                    ROOM_1, MEMBER_1, 3l
            );

    assertThat(messageStatuses1.size()).isEqualTo(1);
    assertThat(messageStatuses2.size()).isEqualTo(2);

    assertThat(messageStatuses1.get(0).isSeen()).isEqualTo(false);
    assertThat(messageStatuses2.get(0).isSeen()).isEqualTo(false);
    assertThat(messageStatuses2.get(1).isSeen()).isEqualTo(false);

  }


}
