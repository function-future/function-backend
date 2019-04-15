package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Batch;

import java.util.List;

/**
 * Service interface class for batch logic operations declaration.
 */
public interface BatchService {
  
  /**
   * Retrieves batches from database.
   *
   * @return {@code List<Batch>} - Batches found in database.
   */
  List<Batch> getBatches();
  
  /**
   * Retrieves a batch from the database given the batch's number. If not
   * found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param number The number of the batch to be retrieved.
   *
   * @return {@code Batch} - The batch object found in database.
   */
  Batch getBatch(long number);
  
  /**
   * Saves a new batch to the database.
   *
   * @return {@code Batch} - The batch object of the saved data.
   */
  Batch createBatch();
  
}
