package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for announcement database operations.
 */
@Repository
public interface AnnouncementRepository
  extends MongoRepository<Announcement, String> {
  
  Page<Announcement> findAllByOrderByUpdatedAtDesc(Pageable pageable);
  
}
