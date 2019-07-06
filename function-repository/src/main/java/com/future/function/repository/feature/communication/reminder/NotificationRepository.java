package com.future.function.repository.feature.communication.reminder;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: PriagungSatyagama
 * Created At: 14:43 06/07/2019
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

  Page<Notification> findAllByMemberOrderByCreatedAtDesc(User member, Pageable pageable);

}
