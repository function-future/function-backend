package com.future.function.service.api.feature.communication;

import com.future.function.model.entity.feature.communication.reminder.Notification;
import com.future.function.session.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Author: PriagungSatyagama
 * Created At: 15:05 06/07/2019
 */
public interface NotificationService {

  Page<Notification> getNotifications(Session session, Pageable pageable);

  Notification createNotification(Notification notification);

}
