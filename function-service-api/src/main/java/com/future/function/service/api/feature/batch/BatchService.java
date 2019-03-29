package com.future.function.service.api.feature.batch;

import java.util.List;

import com.future.function.model.entity.feature.batch.Batch;

public interface BatchService {

  List<Batch> getBatches();

  Batch getBatch(long number);

  Batch createBatch();

  void deleteBatch(long batchNumber);

}
