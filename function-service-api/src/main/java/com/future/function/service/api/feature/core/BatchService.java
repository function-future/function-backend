package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface class for batch logic operations declaration.
 */
public interface BatchService {
  
  /**
   * Retrieves batches from database in descending order, based on last
   * inserted ({@code updatedAt}) information.
   *
   * @return {@code List<Batch>} - Batches found in database.
   */
  Page<Batch> getBatches(Pageable pageable);
  
  /**
   * Retrieves a batch from the database given the batch's code. If not
   * found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param code The code of the batch to be retrieved.
   *
   * @return {@code Batch} - The batch object found in database.
   */
  Batch getBatchByCode(String code);
  
  /**
   * Retrieves a batch from the database given the batch's code. If not
   * found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param batchId The id of the batch to be retrieved.
   *
   * @return {@code Batch} - The batch object found in database.
   */
  Batch getBatchById(String batchId);
  
  /**
   * Saves a new batch to the database.
   *
   * @return {@code Batch} - The batch object of the saved data.
   */
  Batch createBatch(Batch batch);
  
  /**
   * Saves an existing batch to the database.
   *
   * @return {@code Batch} - The batch object of the saved data.
   */
  Batch updateBatch(Batch batch);
  
  /**
   * Deletes a batch from the database.
   *
   * @param batchId Id (not code) of batch to be deleted.
   */
  void deleteBatch(String batchId);
  
}
