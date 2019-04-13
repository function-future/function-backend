package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Batch;

import java.util.List;

/**
 * Service interface class for batch logic operations declaration.
 */
public interface BatchService {
  
  List<Batch> getBatches();
  
  Batch getBatch(long number);
  
  Batch createBatch();
  
}
