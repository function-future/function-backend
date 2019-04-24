package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository class for batch database operations.
 */
@Repository
public interface BatchRepository extends MongoRepository<Batch, Long> {
  
  /**
   * Finds batches in database based on {@code updatedAt} field.
   *
   * @return {@code List<Batch>} - Batches found in database, if any exists;
   * otherwise returns empty {@link java.util.List}.
   */
  List<Batch> findAllByIdIsNotNullOrderByUpdatedAtDesc();
  
  /**
   * Finds first (latest) batch in database based on {@code updatedAt} field.
   *
   * @return {@code Optional<Batch>} - Batch found in database, if any
   * exists; otherwise returns {@link java.util.Optional#empty()}.
   */
  Optional<Batch> findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  
  /**
   * Finds a specific batch, given parameter number.
   *
   * @param number Number of batch to be found.
   *
   * @return {@code Optional<Batch>} - Batch found in database, if any exists;
   * otherwise returns {@link java.util.Optional#empty()}.
   */
  Optional<Batch> findByNumber(long number);
  
}
