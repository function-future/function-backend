package com.future.function.repository.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.TestApplication;
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
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: PriagungSatyagama
 * Created At: 8:54 01/06/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ChatroomRepositoryTest {

  private static final String TITLE_1 = "title1";

  private static final String TITLE_2 = "judul2";

  private static final String TITLE_3 = "title3";

  private static final String KEYWORD = "tle";

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final User member1 = User.builder()
          .name("member1")
          .id("id1")
          .build();

  private static final User member2 = User.builder()
          .name("member2")
          .id("id2")
          .build();

  @Autowired
  private ChatroomRepository chatroomRepository;

  @Before
  public void setUp() {
    Chatroom chatroom1 = Chatroom.builder()
            .members(Arrays.asList(member1, member2))
            .title(TITLE_1)
            .type(ChatroomType.GROUP)
            .build();
    chatroom1.setCreatedAt(5L);
    chatroom1.setUpdatedAt(5L);

    Chatroom chatroom2 = Chatroom.builder()
            .members(Collections.singletonList(member1))
            .title(TITLE_2)
            .type(ChatroomType.GROUP)
            .build();
    chatroom2.setCreatedAt(10L);
    chatroom2.setUpdatedAt(10L);

    Chatroom chatroom3 = Chatroom.builder()
            .members(Arrays.asList(member1, member2))
            .title(TITLE_3)
            .type(ChatroomType.PRIVATE)
            .build();
    chatroom3.setCreatedAt(15L);
    chatroom3.setUpdatedAt(15L);

    chatroomRepository.save(chatroom1);
    chatroomRepository.save(chatroom2);
    chatroomRepository.save(chatroom3);
  }

  @After
  public void tearDown() {
    chatroomRepository.deleteAll();
  }

  @Test
  public void testGivenTypeAndMemberByFindingAllChatroomReturnPagedChatrooms() {
    Page<Chatroom> chatrooms = chatroomRepository
            .findAllByTypeAndMembersOrderByUpdatedAtDesc(ChatroomType.GROUP, member1, PAGEABLE);

    assertThat(chatrooms.getTotalElements()).isEqualTo(2);
    assertThat(chatrooms.getContent().get(0).getTitle()).isEqualTo(TITLE_2);
    assertThat(chatrooms.getContent().get(1).getTitle()).isEqualTo(TITLE_1);
  }

  @Test
  public void testGivenTypeAndMemberAndKeywordByFindingAllChatroomReturnPagedChatrooms() {
    Page<Chatroom> chatrooms = chatroomRepository.findAllByTitleContainingIgnoreCaseAndMembersOrderByUpdatedAtDesc(KEYWORD, member1, PAGEABLE);

    assertThat(chatrooms.getTotalElements()).isEqualTo(2);
    assertThat(chatrooms.getContent().get(0).getTitle()).isEqualTo(TITLE_3);
    assertThat(chatrooms.getContent().get(1).getTitle()).isEqualTo(TITLE_1);
  }

}
