package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository class for batch database operations.
 */
public interface BatchRepository extends MongoRepository<Batch, String> {

  /**
   * Finds batches in database based on {@code Sort} parameter.
   *
   * @return {@code List<Batch>} - Batches found in database, if any exists;
   * otherwise returns empty {@link java.util.List}.
   */
  Page<Batch> findAllByDeletedFalse(Pageable pageable);

  /**
   * Finds batches in database based on {@code id} parameter.
   *
   * @return {@code Page<Batch>} - Batches found in database, if any exists;
   * otherwise returns empty {@link Page}.
   */
  Page<Batch> findAllByIdAndDeletedFalse(String id, Pageable pageable);

  /**
   * Finds first (latest) batch in database based on {@code updatedAt} field.
   *
   * @return {@code Optional<Batch>} - Batch found in database, if any
   * exists; otherwise returns {@link java.util.Optional#empty()}.
   */
  Optional<Batch> findFirstByDeletedFalseOrderByUpdatedAtDesc();

  /**
   * Finds a specific batch, given parameter code.
   *
   * @param code Code of batch to be found.
   *
   * @return {@code Optional<Batch>} - Batch found in database, if any exists;
   * otherwise returns {@link java.util.Optional#empty()}.
   */
  Optional<Batch> findByCodeAndDeletedFalse(String code);

}
