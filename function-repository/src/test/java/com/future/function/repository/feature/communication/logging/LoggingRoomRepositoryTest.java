package com.future.function.repository.feature.communication.logging;

import com.future.function.model.entity.feature.communication.logging.LogMessage;
import com.future.function.model.entity.feature.communication.logging.LoggingRoom;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author : Ricky Kennedy
 * Created At : 17:12 28/07/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class LoggingRoomRepositoryTest {

  private static final String MEMBER_ID = "memberId1";
  private static final String MEMBER_ID2 = "memberId2";
  private static final String LOGGING_ROOM_ID1 = "loggingRoomId1";
  private static final String LOGGING_ROOM_ID2 = "loggingRoomId2";
  private static final String TITLE1 = "title1";
  private static final String TITLE2 = "title2";
  private static final User MEMBER = User.builder().id(MEMBER_ID).build();
  private static final User MEMBER2 = User.builder().id(MEMBER_ID2).build();
  private static final PageRequest PAGEABLE = new PageRequest(0, 2);

  @Autowired
  private LoggingRoomRepository loggingRoomRepository;

  @Before
  public void setUp () {
    LoggingRoom loggingRoom1 =
      LoggingRoom.builder()
        .id(LOGGING_ROOM_ID1)
        .title(TITLE1)
        .members(Arrays.asList(MEMBER))
        .build();
    loggingRoom1.setCreatedAt(1L);

    LoggingRoom loggingRoom2 =
      LoggingRoom.builder()
        .id(LOGGING_ROOM_ID2)
        .title(TITLE2)
        .members(Arrays.asList(MEMBER2))
        .build();
    loggingRoom2.setCreatedAt(2L);

    loggingRoomRepository.save(loggingRoom1);
    loggingRoomRepository.save(loggingRoom2);
  }

  @After
  public void tearDown () {
    loggingRoomRepository.deleteAll();
  }

  @Test
  public void findAllByDeletedFalseOrderByCreatedAtDesc(){
    Page<LoggingRoom> results =
      loggingRoomRepository.findAllByDeletedFalseOrderByCreatedAtDesc(PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID2);
    assertThat(results.getContent().get(1).getId()).isEqualTo(LOGGING_ROOM_ID1);
  }

  @Test
  public void findAllByTitleContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc(){
    Page<LoggingRoom> results =
      loggingRoomRepository.findAllByTitleContainingIgnoreCaseAndDeletedFalseOrderByCreatedAtDesc("tle1", PAGEABLE);

    System.out.println(results.getContent());
    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);
  }

  @Test
  public void findAllByMembersAndDeletedFalseOrderByCreatedAtDesc(){
    Page<LoggingRoom> results =
      loggingRoomRepository.findAllByMembersAndDeletedFalseOrderByCreatedAtDesc(MEMBER, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);

    results =
      loggingRoomRepository.findAllByMembersAndDeletedFalseOrderByCreatedAtDesc(MEMBER2, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID2);
  }

  @Test
  public void findAllByTitleContainingIgnoreCaseAndMembersAndDeletedFalseOrderByCreatedAtDesc(){
    Page<LoggingRoom> results =
      loggingRoomRepository.findAllByTitleContainingIgnoreCaseAndMembersAndDeletedFalseOrderByCreatedAtDesc(
        "tle1", MEMBER, PAGEABLE);

    assertThat(results).isNotNull();
    assertThat(results.getContent().get(0).getId()).isEqualTo(LOGGING_ROOM_ID1);

    results =
      loggingRoomRepository.findAllByTitleContainingIgnoreCaseAndMembersAndDeletedFalseOrderByCreatedAtDesc(
        "tle1",MEMBER2, PAGEABLE);

    assertThat(results.getContent()).isEmpty();
  }
}
