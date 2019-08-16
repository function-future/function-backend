package com.future.function.service.api.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.session.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BatchService {

  Page<Batch> getBatches(Session session, Pageable pageable);

  Batch getBatchByCode(String code);

  Batch getBatchById(String batchId);

  Batch createBatch(Batch batch);

  Batch updateBatch(Batch batch);

  void deleteBatch(String batchId);

}
