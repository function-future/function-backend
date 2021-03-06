package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {

  Optional<Course> findByIdAndDeletedFalse(String courseId);

  Page<Course> findAllByOrderByUpdatedAtDesc(Pageable pageable);

}
