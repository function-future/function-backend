package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository class for course database operations.
 */
public interface CourseRepository extends MongoRepository<Course, String> {}
