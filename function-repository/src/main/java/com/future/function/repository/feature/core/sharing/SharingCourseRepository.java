package com.future.function.repository.feature.core.sharing;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.sharing.SharedCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository class for course sharing database operations.
 */
@Repository
public interface SharingCourseRepository
  extends MongoRepository<SharedCourse, String> {
  
  Optional<SharedCourse> findByCourseIdAndBatchNumber(
    String courseId, long batchNumber
  );
  
  Page<SharedCourse> findAllByBatch(Batch batch, Pageable pageable);
  
  Stream<SharedCourse> findAllByBatch(Batch batch);
  
}
