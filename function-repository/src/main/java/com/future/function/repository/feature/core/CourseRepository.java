package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {}
