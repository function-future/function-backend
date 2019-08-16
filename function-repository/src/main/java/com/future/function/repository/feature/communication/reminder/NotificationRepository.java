package com.future.function.repository.feature.communication.reminder;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository
  extends MongoRepository<Notification, String> {

  Page<Notification> findAllByMemberOrderByCreatedAtDesc(
    User member, Pageable pageable
  );

  List<Notification> findAllByMemberAndSeen(User Member, Boolean seen);

}
