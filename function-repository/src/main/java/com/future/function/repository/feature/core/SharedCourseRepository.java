package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.SharedCourse;
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
  
  /**
   * Finds shared course in database based on {@code courseId} and {@code
   * batch} parameters.
   *
   * @param courseId Id of existing course to be searched.
   * @param batch    Batch object reference of request's batch number.
   *
   * @return {@code Optional<SharedCourse>} - SharedCourse object found in
   * database, if any exists; otherwise returns
   * {@link java.util.Optional#empty()}.
   */
  Optional<SharedCourse> findByIdAndBatch(String courseId, Batch batch);
  
  /**
   * Finds shared courses in database based on {@code batch} amd {@code
   * pageable} parameters.
   *
   * @param batch    Batch object reference of request's batch number.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<SharedCourse>} - SharedCourse objects found in
   * database, if any exists; otherwise returns
   * {@link java.util.Optional#empty()}.
   */
  Page<SharedCourse> findAllByBatch(Batch batch, Pageable pageable);
  
  /**
   * Finds shared courses based on {@code batch} parameter.
   *
   * @param batch Batch object reference of request's batch number.
   *
   * @return {@code Stream<SharedCourse>} - SharedCourse objects as stream,
   * if any exists; otherwise returns {@link java.util.stream.Stream#empty()}.
   */
  Stream<SharedCourse> findAllByBatch(Batch batch);
  
  /**
   * Finds shared courses based on {@code courseId} parameter.
   *
   * @param courseId Id of course of shared courses to be fetched.
   *
   * @return {@code Stream<SharedCourse>} - SharedCourse objects as stream,
   * if any exists; otherwise returns {@link java.util.stream.Stream#empty()}.
   */
  Stream<SharedCourse> findAllByCourseId(String courseId);
  
}
