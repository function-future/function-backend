package com.future.function.repository.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.communication.chatting.Message;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.communication.chatting.MessageRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class MessageRepositoryTest {

  private static final String TEXT_1 = "text message 1";

  private static final String TEXT_2 = "text message 2";

  private static final String TEXT_3 = "text message 3";

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final Chatroom chatroom1 = Chatroom.builder()
    .id("id1")
    .type(ChatroomType.GROUP)
    .title("chatroom1")
    .build();

  private static final Chatroom chatroom2 = Chatroom.builder()
    .id("id2")
    .type(ChatroomType.GROUP)
    .title("chatroom2")
    .build();

  @Autowired
  private MessageRepository messageRepository;

  @Before
  public void setUp() {

    Message message1 = Message.builder()
      .chatroom(chatroom1)
      .text(TEXT_1)
      .build();
    message1.setCreatedAt(10L);

    Message message2 = Message.builder()
      .chatroom(chatroom1)
      .text(TEXT_2)
      .build();
    message2.setCreatedAt(15L);

    Message message3 = Message.builder()
      .chatroom(chatroom2)
      .text(TEXT_3)
      .build();
    message3.setCreatedAt(20L);

    messageRepository.save(message1);
    messageRepository.save(message2);
    messageRepository.save(message3);
  }

  @After
  public void tearDown() {

    messageRepository.deleteAll();
  }

  @Test
  public void testGivenChatroomByFindingAllMessageReturnPagedMessages() {

    Page<Message> messages =
      messageRepository.findAllByChatroomOrderByCreatedAtDesc(
        chatroom1, PAGEABLE);

    assertThat(messages.getTotalElements()).isEqualTo(2);
    assertThat(messages.getContent()
                 .get(0)
                 .getText()).isEqualTo(TEXT_2);
    assertThat(messages.getContent()
                 .get(1)
                 .getText()).isEqualTo(TEXT_1);
  }

  @Test
  public void testGivenChatroomByFindingLatestMessageReturnMessage() {

    Message message = messageRepository.findFirstByChatroomOrderByCreatedAtDesc(
      chatroom1);

    assertThat(message).isNotNull();
    assertThat(message.getText()).isEqualTo(TEXT_2);
  }

}
