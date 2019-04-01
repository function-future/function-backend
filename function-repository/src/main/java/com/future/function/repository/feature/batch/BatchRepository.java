package com.future.function.repository.feature.batch;

import com.future.function.model.entity.feature.batch.Batch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository class for batch database operations.
 */
@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {
  
  /**
   * Finds all batches which is not marked as deleted.
   *
   * @return {@code List<Batch>} - List of batches found as not marked as
   * deleted.
   */
  List<Batch> findAllByDeletedIsFalse();
  
  /**
   * Finds first (latest) batch in database based on {@code updatedAt} field.
   *
   * @return {@code Optional<Batch>} - Batch found in database, if any exists;
   * otherwise returns {@code Optional.empty()}.
   */
  Optional<Batch> findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  
  /**
   * Finds a specific batch, given parameter number.
   *
   * @param number - Number of batch to be found.
   *
   * @return {@code Optional<Batch>} - Batch found in database, if any exists;
   * otherwise returns {@code Optional.empty()}.
   */
  Optional<Batch> findByNumberAndDeletedIsFalse(long number);
  
}
