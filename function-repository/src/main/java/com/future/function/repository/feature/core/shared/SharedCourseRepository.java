package com.future.function.repository.feature.core.shared;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.shared.SharedCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository class for shared course database operations.
 */
@Repository
public interface SharedCourseRepository
  extends MongoRepository<SharedCourse, String> {
  
  Optional<SharedCourse> findByCourseIdAndBatch(String courseId, Batch batch);
  
  Page<SharedCourse> findAllByBatch(Batch batch, Pageable pageable);
  
  Stream<SharedCourse> findAllByBatch(Batch batch);
  
}
