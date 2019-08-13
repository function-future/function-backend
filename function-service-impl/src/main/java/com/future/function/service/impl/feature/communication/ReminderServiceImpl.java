package com.future.function.service.impl.feature.communication;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.communication.reminder.Reminder;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.reminder.ReminderRepository;
import com.future.function.service.api.feature.communication.ReminderService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReminderServiceImpl implements ReminderService {

  private final ReminderRepository reminderRepository;

  private final UserService userService;

  @Autowired
  public ReminderServiceImpl(ReminderRepository reminderRepository, UserService userService) {
    this.reminderRepository = reminderRepository;
    this.userService = userService;
  }

  @Override
  public List<Reminder> getAllReminder() {
    return reminderRepository.findAll();
  }

  @Override
  public Page<Reminder> getAllPagedReminder(Pageable pageable, String keyword) {
    return reminderRepository.findAllByTitleContainingIgnoreCaseOrderByUpdatedAtDesc(keyword, pageable);
  }

  @Override
  public Reminder getReminder(String reminderId) {
    return Optional.ofNullable(reminderId)
            .map(reminderRepository::findOne)
            .orElseThrow(() -> new NotFoundException("Reminder not found"));
  }

  @Override
  public Reminder createReminder(Reminder reminder) {
    return Optional.of(reminder)
            .map(r -> this.setMembers(r, r.getMembers()))
            .map(reminderRepository::save)
            .orElseThrow(UnsupportedOperationException::new);
  }

  @Override
  public Reminder updateReminder(Reminder reminder) {
    return Optional.of(reminder.getId())
            .map(reminderRepository::findOne)
            .map(r -> this.setMembers(r, reminder.getMembers()))
            .map(r -> this.setReminderAttributes(reminder, r))
            .map(reminderRepository::save)
            .orElse(reminder);
  }

  @Override
  public void deleteReminder(String reminderId) {
    reminderRepository.delete(reminderId);
  }

  private Reminder setMembers(Reminder reminder, List<User> members) {
    List<User> membersFromDb = reminder.getMembers().stream()
            .map(User::getId)
            .map(userService::getUser)
            .collect(Collectors.toList());

    reminder.setMembers(membersFromDb);
    return reminder;
  }

  private Reminder setReminderAttributes(Reminder newReminder, Reminder reminderFromDb) {
    reminderFromDb.setContent(newReminder.getContent());
    reminderFromDb.setDays(newReminder.getDays());
    reminderFromDb.setHour(newReminder.getHour());
    reminderFromDb.setIsRepeatedMonthly(newReminder.getIsRepeatedMonthly());
    reminderFromDb.setLastReminderSent(newReminder.getLastReminderSent());
    reminderFromDb.setMinute(newReminder.getMinute());
    reminderFromDb.setMonthlyDate(newReminder.getMonthlyDate());
    reminderFromDb.setTitle(newReminder.getTitle());
    return reminderFromDb;
  }

}
