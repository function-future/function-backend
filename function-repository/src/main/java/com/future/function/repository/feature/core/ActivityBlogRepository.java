package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository class for activity blog database operations.
 */
public interface ActivityBlogRepository
  extends MongoRepository<ActivityBlog, String>, ActivityBlogRepositoryCustom {}
