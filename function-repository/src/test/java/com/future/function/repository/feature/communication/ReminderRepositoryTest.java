package com.future.function.repository.feature.communication;

import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.repository.TestApplication;
import com.future.function.repository.feature.communication.reminder.ReminderRepository;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Author: PriagungSatyagama
 * Created At: 20:44 17/07/2019
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class ReminderRepositoryTest {

  public static final String TITLE = "test123";

  @Autowired
  private ReminderRepository reminderRepository;

  @Before
  public void setUp() {
    Reminder reminder = Reminder.builder()
            .title(TITLE)
            .build();

    reminderRepository.save(reminder);
  }

  @After
  public void tearDown() {
    reminderRepository.deleteAll();
  }

  @Test
  public void testGivenKeywordAndPagingByFindingAllReminderReturnPaging() {
    Page<Reminder> reminders = reminderRepository.findAllByTitleContainingIgnoreCaseOrderByUpdatedAtDesc(
            "test",
            new PageRequest(0, 10)
    );

    Assertions.assertThat(reminders.getContent().size()).isEqualTo(1);
    Assertions.assertThat(reminders.getContent().get(0).getTitle()).isEqualTo(TITLE);
  }

}
