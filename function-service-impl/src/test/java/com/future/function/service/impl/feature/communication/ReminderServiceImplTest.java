package com.future.function.service.impl.feature.communication;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.reminder.ReminderRepository;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReminderServiceImplTest {

  private static final String REMINDER_ID_1 = "reminderId1";

  private static final String REMINDER_ID_2 = "reminderId2";

  private static final Reminder REMINDER_1 = Reminder.builder()
    .title("test")
    .id(REMINDER_ID_1)
    .build();

  private static final Reminder REMINDER_2 = Reminder.builder()
    .title("test123")
    .id(REMINDER_ID_2)
    .build();

  private static final PageRequest PAGEABLE = new PageRequest(0, 10);

  private static final String USER_ID = "userId";

  private static final User USER = User.builder()
    .id(USER_ID)
    .build();

  @Mock
  private ReminderRepository reminderRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private ReminderServiceImpl reminderService;

  @After
  public void tearDown() {

    verifyNoMoreInteractions(reminderRepository, userService);
  }

  @Test
  public void testGivenReminderIdByGettingReminderReturnReminder() {

    when(reminderRepository.findOne(REMINDER_ID_1)).thenReturn(REMINDER_1);

    Reminder reminder = reminderService.getReminder(REMINDER_ID_1);

    assertThat(reminder.getId()).isEqualTo(REMINDER_ID_1);
    verify(reminderRepository).findOne(REMINDER_ID_1);
  }

  @Test
  public void testGivenReminderIdByGettingReminderReturnNotFoundException() {

    when(reminderRepository.findOne(REMINDER_ID_1)).thenReturn(null);

    catchException(() -> reminderService.getReminder(REMINDER_ID_1));

    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    verify(reminderRepository).findOne(REMINDER_ID_1);
  }

  @Test
  public void testGivenMethodCallByGettingAllReminderReturnListReminder() {

    when(reminderRepository.findAll()).thenReturn(
      Arrays.asList(REMINDER_1, REMINDER_2));

    List<Reminder> reminders = reminderService.getAllReminder();

    assertThat(reminders.size()).isEqualTo(2);
    assertThat(reminders.get(0)
                 .getId()).isEqualTo(REMINDER_ID_1);
    assertThat(reminders.get(1)
                 .getId()).isEqualTo(REMINDER_ID_2);
    verify(reminderRepository).findAll();
  }

  @Test
  public void testGivenMethodCallByGettingAllReminderReturnPageReminder() {

    when(
      reminderRepository.findAllByTitleContainingIgnoreCaseOrderByUpdatedAtDesc(
        "", PAGEABLE)).thenReturn(
      PageHelper.toPage(Arrays.asList(REMINDER_1, REMINDER_2), PAGEABLE));

    Page<Reminder> reminders = reminderService.getAllPagedReminder(
      PAGEABLE, "");

    assertThat(reminders.getTotalElements()).isEqualTo(2);
    assertThat(reminders.getContent()
                 .get(0)
                 .getId()).isEqualTo(REMINDER_ID_1);
    assertThat(reminders.getContent()
                 .get(1)
                 .getId()).isEqualTo(REMINDER_ID_2);
    verify(
      reminderRepository).findAllByTitleContainingIgnoreCaseOrderByUpdatedAtDesc(
      "", PAGEABLE);
  }

  @Test
  public void testGivenReminderByCreatingReminderReturnReminder() {

    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(reminderRepository.save(REMINDER_1)).thenReturn(REMINDER_1);
    REMINDER_1.setMembers(Collections.singletonList(USER));

    Reminder reminder = reminderService.createReminder(REMINDER_1);

    assertThat(reminder.getId()).isEqualTo(REMINDER_ID_1);
    verify(userService).getUser(USER_ID);
    verify(reminderRepository).save(REMINDER_1);
  }

  @Test
  public void testGivenReminderByCreatingReminderReturnUnsupportedOperationException() {

    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(reminderRepository.save(REMINDER_1)).thenReturn(null);
    REMINDER_1.setMembers(Collections.singletonList(USER));

    catchException(() -> reminderService.createReminder(REMINDER_1));

    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    verify(userService).getUser(USER_ID);
    verify(reminderRepository).save(REMINDER_1);
  }

  @Test
  public void testGivenReminderByUpdatingReminderReturnReminder() {

    when(reminderRepository.findOne(REMINDER_ID_1)).thenReturn(REMINDER_1);
    when(userService.getUser(USER_ID)).thenReturn(USER);
    when(reminderRepository.save(REMINDER_1)).thenReturn(REMINDER_1);
    REMINDER_1.setMembers(Collections.singletonList(USER));

    Reminder reminder = reminderService.updateReminder(REMINDER_1);

    assertThat(reminder.getId()).isEqualTo(REMINDER_ID_1);
    assertThat(reminder.getMembers()
                 .get(0)
                 .getId()).isEqualTo(USER.getId());
    verify(reminderRepository).findOne(REMINDER_ID_1);
    verify(userService).getUser(USER_ID);
    verify(reminderRepository).save(REMINDER_1);
  }

  @Test
  public void testGivenReminderIdByDeletingReminderReturnVoid() {

    doNothing().when(reminderRepository)
      .delete(REMINDER_ID_1);
    reminderService.deleteReminder(REMINDER_ID_1);
    verify(reminderRepository).delete(REMINDER_ID_1);
  }

}
