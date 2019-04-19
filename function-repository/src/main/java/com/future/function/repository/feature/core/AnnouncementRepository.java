package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for announcement database operations.
 */
@Repository
public interface AnnouncementRepository
  extends MongoRepository<Announcement, String> {

  /**
   * Finds announcements in database.
   *
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<Announcement>} - Announcements found in database, if
   * any exists; otherwise returns empty
   * {@link org.springframework.data.domain.Page}.
   */
  Page<Announcement> findAll(Pageable pageable);

}
