package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for activity blog database operations.
 */
@Repository
public interface ActivityBlogRepository
  extends MongoRepository<ActivityBlog, String> {}
