package com.future.function.service.impl.feature.communication.logging;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.logging.LoggingRoomRepository;
import com.future.function.service.api.feature.core.UserService;
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

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Author : Ricky Kennedy
 * Created At : 20:53 28/07/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class LoggingRoomServiceImplTest {

  private static final String MEMBER_ID = "memberId1";
  private static final String MEMBER_ID2 = "memberId2";
  private static final String LOGGING_ROOM_ID1 = "loggingRoomId1";
  private static final String LOGGING_ROOM_ID2 = "loggingRoomId2";
  private static final String TITLE1 = "title1";
  private static final String TITLE2 = "title2";
  private static final String KEYWORD = "tle1";
  private static final User MEMBER = User.builder().id(MEMBER_ID).role(Role.STUDENT).build();
  private static final User MEMBER2 = User.builder().id(MEMBER_ID2).role(Role.MENTOR).build();
  private static final PageRequest PAGEABLE = new PageRequest(0, 2);

  private LoggingRoom loggingRoom1;
  private LoggingRoom loggingRoom2;

  @Mock
  private LoggingRoomRepository loggingRoomRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private LoggingRoomServiceImpl loggingRoomService;

  @Before
  public void setUp() {
    loggingRoom1 =
      LoggingRoom.builder()
        .id(LOGGING_ROOM_ID1)
        .title(TITLE1)
        .members(Arrays.asList(MEMBER))
        .build();
    loggingRoom1.setCreatedAt(1L);

    loggingRoom2 =
      LoggingRoom.builder()
        .id(LOGGING_ROOM_ID2)
        .title(TITLE2)
        .members(Arrays.asList(MEMBER2))
        .build();
    loggingRoom2.setCreatedAt(2L);
  }

  @After
  public void tearDown () {
    verifyNoMoreInteractions(
      loggingRoomRepository,
      userService
    );
  }

  @Test
  public void getLoggingRoomByMember() {
    when(userService.getUser(MEMBER_ID)).thenReturn(MEMBER);
    when(userService.getUser(MEMBER_ID2)).thenReturn(MEMBER2);
    when(loggingRoomRepository.findAllByTitleContainingIgnoreCaseAndMembersAndDeletedFalseOrderByCreatedAtDesc(
      KEYWORD, MEMBER, PAGEABLE
    )).thenReturn(new PageImpl<>(Arrays.asList(loggingRoom1), PAGEABLE, 1));
    when(loggingRoomRepository.findAllByTitleContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(KEYWORD, PAGEABLE))
      .thenReturn(new PageImpl<>(Arrays.asList(loggingRoom1), PAGEABLE, 2));

    Page<LoggingRoom> results = loggingRoomService.getLoggingRoomsByMemberWithKeyword(KEYWORD, MEMBER_ID, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);

    results = loggingRoomService.getLoggingRoomsByMemberWithKeyword(KEYWORD, MEMBER_ID2, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);

    verify(userService,times(2)).getUser(MEMBER_ID);
    verify(userService).getUser(MEMBER_ID2);
    verify(loggingRoomRepository)
      .findAllByTitleContainingIgnoreCaseAndMembersAndDeletedFalseOrderByCreatedAtDesc(
        KEYWORD, MEMBER, PAGEABLE);
    verify(loggingRoomRepository)
      .findAllByTitleContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(
        KEYWORD, PAGEABLE);
  }

  @Test
  public void getLoggingRoomsByMemberWithKeyword() {
    when(userService.getUser(MEMBER_ID)).thenReturn(MEMBER);
    when(userService.getUser(MEMBER_ID2)).thenReturn(MEMBER2);
    when(loggingRoomRepository.findAllByMembersAndDeletedFalseOrderByCreatedAtDesc(
      MEMBER, PAGEABLE
    )).thenReturn(new PageImpl<>(Arrays.asList(loggingRoom1), PAGEABLE, 1));
    when(loggingRoomRepository.findAllByDeletedFalseOrderByCreatedAtDesc(PAGEABLE))
      .thenReturn(new PageImpl<>(Arrays.asList(loggingRoom1, loggingRoom2), PAGEABLE, 2));

    Page<LoggingRoom> results = loggingRoomService.getLoggingRoomsByMember(MEMBER_ID, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);

    results = loggingRoomService.getLoggingRoomsByMember(MEMBER_ID2, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);
    assertThat(results.getContent().get(1).getId()).isEqualTo(LOGGING_ROOM_ID2);

    verify(userService).getUser(MEMBER_ID);
    verify(userService).getUser(MEMBER_ID2);
    verify(loggingRoomRepository).findAllByMembersAndDeletedFalseOrderByCreatedAtDesc(
      MEMBER, PAGEABLE);
    verify(loggingRoomRepository).findAllByDeletedFalseOrderByCreatedAtDesc(PAGEABLE);
  }

  @Test
  public void getLoggingRoom() {
    when(loggingRoomRepository.findOne(LOGGING_ROOM_ID1))
      .thenReturn(loggingRoom1);

    LoggingRoom result = loggingRoomService.getLoggingRoom(LOGGING_ROOM_ID1);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(LOGGING_ROOM_ID1);

    verify(loggingRoomRepository).findOne(LOGGING_ROOM_ID1);

  }

  @Test
  public void createLoggingRoom() {
    when(loggingRoomRepository.save(loggingRoom1))
      .thenReturn(loggingRoom1);

    when(userService.getUser(MEMBER_ID))
      .thenReturn(MEMBER);

    LoggingRoom result = loggingRoomService.createLoggingRoom(loggingRoom1);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(LOGGING_ROOM_ID1);

    verify(userService).getUser(MEMBER_ID);
    verify(loggingRoomRepository).save(loggingRoom1);
  }

  @Test
  public void updateLoggingRoom() {
    when(loggingRoomRepository.save(loggingRoom1))
      .thenReturn(loggingRoom1);

    when(userService.getUser(MEMBER_ID))
      .thenReturn(MEMBER);

    when(loggingRoomRepository.findOne(LOGGING_ROOM_ID1))
      .thenReturn(loggingRoom1);

    LoggingRoom result = loggingRoomService.updateLoggingRoom(loggingRoom1);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(LOGGING_ROOM_ID1);

    verify(userService).getUser(MEMBER_ID);
    verify(loggingRoomRepository).save(loggingRoom1);
    verify(loggingRoomRepository).findOne(LOGGING_ROOM_ID1);

  }

  @Test
  public void deleteLoggingRoom() {

    when(loggingRoomRepository.save(loggingRoom1))
      .thenReturn(loggingRoom1);

    when(loggingRoomRepository.findOne(LOGGING_ROOM_ID1))
      .thenReturn(loggingRoom1);

    loggingRoomService.deleteLoggingRoom(LOGGING_ROOM_ID1);

    assertThat(loggingRoom1.isDeleted()).isEqualTo(Boolean.TRUE);

    verify(loggingRoomRepository).save(loggingRoom1);
    verify(loggingRoomRepository).findOne(LOGGING_ROOM_ID1);

  }
}
