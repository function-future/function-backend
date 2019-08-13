package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.SharedCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface SharedCourseRepository
  extends MongoRepository<SharedCourse, String> {

  Optional<SharedCourse> findByIdAndBatch(String courseId, Batch batch);

  Page<SharedCourse> findAllByBatch(Batch batch, Pageable pageable);

  Stream<SharedCourse> findAllByBatch(Batch batch);

  Stream<SharedCourse> findAllByCourseId(String courseId);

}
