package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.repository.feature.core.SequenceGenerator;
import com.future.function.service.api.feature.core.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation class for batch logic operations implementation.
 */
@Service
public class BatchServiceImpl implements BatchService {
  
  private final BatchRepository batchRepository;
  
  private final SequenceGenerator sequenceGenerator;
  
  @Autowired
  public BatchServiceImpl(
    BatchRepository batchRepository, SequenceGenerator sequenceGenerator
  ) {
    
    this.batchRepository = batchRepository;
    this.sequenceGenerator = sequenceGenerator;
  }
  
  @Override
  public List<Batch> getBatches() {
  
    return batchRepository.findAll();
  }
  
  @Override
  public Batch getBatch(long number) {
  
    return Optional.of(batchRepository.findOne(number))
      .orElseThrow(() -> new NotFoundException("Get Batch Not Found"));
  }
  
  @Override
  public Batch createBatch() {
  
    batchRepository.save(
      new Batch(sequenceGenerator.increment(Batch.SEQUENCE_NAME)));
  
    return batchRepository.findFirstByNumberIsNotNullOrderByUpdatedAtDesc()
      .orElseThrow(() -> new NotFoundException("Saved Batch Not Found"));
  }
  
}
