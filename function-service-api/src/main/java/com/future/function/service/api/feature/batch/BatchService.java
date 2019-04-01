package com.future.function.service.api.feature.batch;

import com.future.function.model.entity.feature.batch.Batch;

import java.util.List;

/**
 * Service interface class for batch logic operations declaration.
 */
public interface BatchService {
  
  List<Batch> getBatches();
  
  Batch getBatch(long number);
  
  Batch createBatch();
  
  void deleteBatch(long batchNumber);
  
}
