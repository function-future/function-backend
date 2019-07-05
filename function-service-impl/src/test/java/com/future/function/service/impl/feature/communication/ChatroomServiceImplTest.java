package com.future.function.service.impl.feature.communication;

import com.future.function.common.enumeration.communication.ChatroomType;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.chatting.Chatroom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.ChatroomRepository;
import com.future.function.service.api.feature.core.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Author: PriagungSatyagama
 * Created At: 14:51 04/06/2019
 */
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

  private static final String USER_NAME_1 = "Priagung";

  private static final String USER_NAME_2 = "Satyagama";

  private static final User MEMBER_1 = User.builder()
          .id(USER_ID_1)
          .name(USER_NAME_1)
          .build();

  private static final User MEMBER_2 = User.builder()
          .id(USER_ID_2)
          .name(USER_NAME_2)
          .build();

  private Chatroom chatroom;

  @Mock
  private UserService userService;

  @Mock
  private ChatroomRepository chatroomRepository;

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
    when(chatroomRepository.findAllByTypeAndMembersOrderByUpdatedAtDesc(TYPE, MEMBER_1, PAGEABLE))
            .thenReturn(new PageImpl<>(Collections.singletonList(chatroom), PAGEABLE, 1));

    Page<Chatroom> chatroomPage = chatroomService.getChatrooms(TYPE.name(), USER_ID_1, PAGEABLE);

    assertThat(chatroomPage.getTotalElements()).isEqualTo(1);
    assertThat(chatroomPage.getContent().get(0).getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomPage.getContent().get(0).getTitle()).isEqualTo(TITLE_GROUP);
    assertThat(chatroomPage.getContent().get(0).getType()).isEqualTo(TYPE);
    assertThat(chatroomPage.getContent().get(0).getMembers().size()).isEqualTo(2);
    assertThat(chatroomPage.getContent().get(0).getMembers().contains(MEMBER_1)).isTrue();
    assertThat(chatroomPage.getContent().get(0).getMembers().contains(MEMBER_2)).isTrue();

    verify(userService).getUser(USER_ID_1);
    verify(chatroomRepository).findAllByTypeAndMembersOrderByUpdatedAtDesc(TYPE, MEMBER_1, PAGEABLE);
  }

  @Test
  public void testGivenKeywordAndMemberByGettingChatroomByKeywordAndMemberReturnPagedChatrooms() {
    when(userService.getUser(USER_ID_1)).thenReturn(MEMBER_1);
    when(chatroomRepository.findAllByTitleContainingIgnoreCaseAndMembersOrderByUpdatedAtDesc(KEYWORD, MEMBER_1, PAGEABLE))
            .thenReturn(new PageImpl<>(Collections.singletonList(chatroom), PAGEABLE, 1));

    Page<Chatroom> chatroomPage = chatroomService.getChatroomsWithKeyword(KEYWORD, USER_ID_1, PAGEABLE);

    assertThat(chatroomPage.getTotalElements()).isEqualTo(1);
    assertThat(chatroomPage.getContent().get(0).getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomPage.getContent().get(0).getTitle()).isEqualTo(TITLE_GROUP);
    assertThat(chatroomPage.getContent().get(0).getType()).isEqualTo(TYPE);
    assertThat(chatroomPage.getContent().get(0).getMembers().size()).isEqualTo(2);
    assertThat(chatroomPage.getContent().get(0).getMembers().contains(MEMBER_1)).isTrue();
    assertThat(chatroomPage.getContent().get(0).getMembers().contains(MEMBER_2)).isTrue();

    verify(userService).getUser(USER_ID_1);
    verify(chatroomRepository).findAllByTitleContainingIgnoreCaseAndMembersOrderByUpdatedAtDesc(
            KEYWORD, MEMBER_1, PAGEABLE);
  }

  @Test
  public void testGivenChatroomIdByGettingChatroomByIdReturnChatroom() {
    when(chatroomRepository.findOne(CHATROOM_ID)).thenReturn(chatroom);

    Chatroom chatroomResult = chatroomService.getChatroom(CHATROOM_ID);

    assertThat(chatroomResult).isNotNull();
    assertThat(chatroomResult.getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomResult.getTitle()).isEqualTo(TITLE_GROUP);
    assertThat(chatroomResult.getType()).isEqualTo(TYPE);

    verify(chatroomRepository).findOne(CHATROOM_ID);
  }

  @Test
  public void testGivenChatroomIdByGettingChatroomByIdReturnNotFoundException() {
    when(chatroomRepository.findOne(CHATROOM_ID_FAKE)).thenReturn(null);

    catchException(() -> chatroomService.getChatroom(CHATROOM_ID_FAKE));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo("Get Chatroom Not Found");

    verify(chatroomRepository).findOne(CHATROOM_ID_FAKE);
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
    when(chatroomRepository.findByMembersContaining(Arrays.asList(MEMBER_1, MEMBER_2))).thenReturn(new ArrayList<>());

    chatroom.setType(ChatroomType.PRIVATE);

    Chatroom chatroomResult = chatroomService.createChatroom(chatroom);

    assertThat(chatroomResult).isNotNull();
    assertThat(chatroomResult.getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomResult.getType()).isEqualTo(ChatroomType.PRIVATE);

    verify(userService, times(2)).getUser(USER_ID_1);
    verify(userService, times(2)).getUser(USER_ID_2);
    verify(chatroomRepository).save(chatroom);
    verify(chatroomRepository).findByMembersContaining(Arrays.asList(MEMBER_1, MEMBER_2));
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

    Chatroom chatroomResult = chatroomService.updateChatroom(newChatroom);

    assertThat(chatroomResult).isNotNull();
    assertThat(chatroomResult.getId()).isEqualTo(CHATROOM_ID);
    assertThat(chatroomResult.getTitle()).isEqualTo(UPDATED_TITLE_GROUP);
    assertThat(chatroomResult.getType()).isEqualTo(UPDATED_TYPE);

    verify(chatroomRepository).findOne(CHATROOM_ID);
    verify(chatroomRepository).save(chatroom);
    verify(userService).getUser(USER_ID_1);
    verify(userService).getUser(USER_ID_2);
  }

}
