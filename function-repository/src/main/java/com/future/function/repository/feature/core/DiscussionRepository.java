package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiscussionRepository
  extends MongoRepository<Discussion, String> {

  Page<Discussion> findAllByCourseIdAndBatchIdOrderByCreatedAtDesc(
    String courseId, String batchCode, Pageable pageable
  );

  void deleteAllByCourseIdAndBatchId(String courseId, String batchId);

}
