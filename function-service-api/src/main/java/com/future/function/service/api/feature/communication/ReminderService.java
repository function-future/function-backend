package com.future.function.service.api.feature.communication;

import com.future.function.model.entity.feature.communication.reminder.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReminderService {

  List<Reminder> getAllReminder();

  Page<Reminder> getAllPagedReminder(Pageable pageable, String keyword);

  Reminder getReminder(String reminderId);

  Reminder createReminder(Reminder reminder);

  Reminder updateReminder(Reminder reminder);

  void deleteReminder(String reminderId);

}
