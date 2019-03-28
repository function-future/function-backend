package com.future.function.service.impl.feature.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.repository.feature.batch.BatchRepository;
import com.future.function.service.api.feature.batch.BatchService;

@Service
public class BatchServiceImpl implements BatchService {

  private BatchRepository batchRepository;

  @Autowired
  public BatchServiceImpl(BatchRepository batchRepository) {

    this.batchRepository = batchRepository;
  }

  @Override
  public Batch findByNumber(long number) {

    return batchRepository.findByNumber(number)
        .orElseThrow(() -> new RuntimeException("Not Found"));
  }

}
