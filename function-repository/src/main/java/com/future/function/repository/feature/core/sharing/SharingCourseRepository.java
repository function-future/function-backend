package com.future.function.repository.feature.core.sharing;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.sharing.SharingCourse;
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
  extends MongoRepository<SharingCourse, String> {
  
  Optional<SharingCourse> findByCourseIdAndBatchNumber(
    String courseId, long batchNumber
  );
  
  Page<SharingCourse> findAllByBatch(Batch batch, Pageable pageable);
  
  Stream<SharingCourse> findAllByBatch(Batch batch);
  
}
