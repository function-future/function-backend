package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnouncementService {
  
  Page<Announcement> getAnnouncements(Pageable pageable);
  
  Announcement getAnnouncement(String announcementId);
  
  Announcement createAnnouncement(
    Announcement announcement
  );
  
  Announcement updateAnnouncement(
    Announcement announcement
  );
  
  void deleteAnnouncement(String announcementId);
  
}
