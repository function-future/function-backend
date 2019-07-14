package com.future.function.repository.feature.communication.reminder;

import com.future.function.model.entity.feature.communication.reminder.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Author: PriagungSatyagama
 * Created At: 14:43 06/07/2019
 */
public interface ReminderRepository extends MongoRepository<Reminder, String> {

}
