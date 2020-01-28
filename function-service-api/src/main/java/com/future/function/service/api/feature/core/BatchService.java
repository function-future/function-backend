package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.session.model.Session;

import java.util.List;

public interface BatchService {

  List<Batch> getBatches(Session session);

  Batch getBatchByCode(String code);

  Batch getBatchById(String batchId);

  Batch createBatch(Batch batch);

  Batch updateBatch(Batch batch);

  void deleteBatch(String batchId);

}
