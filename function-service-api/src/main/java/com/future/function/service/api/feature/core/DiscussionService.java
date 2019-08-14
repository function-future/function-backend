package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscussionService {
  
  Page<Discussion> getDiscussions(
    String email, String courseId, String batchCode, Pageable pageable
  );
  
  Discussion createDiscussion(Discussion discussion);
  
  void deleteDiscussions(String courseId, String batchCode);
  
}
