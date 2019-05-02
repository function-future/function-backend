package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.service.api.feature.core.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation class for batch logic operations implementation.
 */
@Service
public class BatchServiceImpl implements BatchService {
  
  private final BatchRepository batchRepository;
  
  @Autowired
  public BatchServiceImpl(
    BatchRepository batchRepository
  ) {
    
    this.batchRepository = batchRepository;
  }
  
  /**
   * {@inheritDoc}
   *
   * @return {@code Batch} - Batches found in database.
   */
  @Override
  public List<Batch> getBatches() {
    
    return batchRepository.findAllByIdIsNotNullOrderByUpdatedAtDesc();
  }
  
  /**
   * {@inheritDoc}
   *
   * @param number Number of the batch to be retrieved.
   *
   * @return {@code Batch} - The batch object found in database.
   */
  @Override
  public Batch getBatch(long number) {
    
    return batchRepository.findByCode(number)
      .orElseThrow(() -> new NotFoundException("Get Batch Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @return {@code Batch} - The batch object of the saved data.
   */
  @Override
  public Batch createBatch() {
    
    batchRepository.save(new Batch("id-1", "one", 1L));
    
    return batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()
      .orElseThrow(() -> new NotFoundException("Saved Batch Not Found"));
  }
  
}
