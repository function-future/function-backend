package com.future.function.repository.feature.communication.reminder;

import com.future.function.model.entity.feature.communication.reminder.Reminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReminderRepository extends MongoRepository<Reminder, String> {

  Page<Reminder> findAllByTitleContainingIgnoreCaseOrderByUpdatedAtDesc(String title, Pageable pageable);

}
