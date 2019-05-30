package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface class for discussion logic operations declaration.
 */
public interface DiscussionService {
  
  /**
   * Retrieves courses from database.
   *
   * @param courseId  Id of course's discussion to be retrieved.
   * @param batchCode Batch code of target discussion.
   * @param pageable  Pageable object for paging data.
   *
   * @return {@code Page<Discussion>} - Page of discussions found in database.
   */
  Page<Discussion> getDiscussions(
    String email, String courseId, String batchCode, Pageable pageable
  );
  
  /**
   * Creates discussion object in database.
   *
   * @param discussion Discussion object to be saved.
   *
   * @return {@code Discussion} - The discussion object of the saved data.
   */
  Discussion createDiscussion(
    Discussion discussion
  );
  
}
