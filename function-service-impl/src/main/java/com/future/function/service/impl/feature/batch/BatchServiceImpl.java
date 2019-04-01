package com.future.function.service.impl.feature.batch;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.repository.feature.batch.BatchRepository;
import com.future.function.service.api.feature.batch.BatchService;
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
  
  @Autowired
  public BatchServiceImpl(BatchRepository batchRepository) {
    
    this.batchRepository = batchRepository;
  }
  
  @Override
  public List<Batch> getBatches() {
    
    return batchRepository.findAllByDeletedIsFalse();
  }
  
  @Override
  public Batch getBatch(long number) {
    
    return batchRepository.findByNumberAndDeletedIsFalse(number)
      .orElseThrow(() -> new NotFoundException("Get Batch Not Found"));
  }
  
  @Override
  public Batch createBatch() {
    
    Optional<Batch> latestBatch =
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc();
    
    if (latestBatch.isPresent()) {
      return latestBatch.filter(Batch::isDeleted)
        .map(batch -> markBatch(batch, false))
        .orElseGet(() -> createNewBatch(latestBatch.get()
                                          .getNumber()));
    }
    return createNewBatch();
  }
  
  private Batch createNewBatch() {
    
    return createNewBatch(0);
  }
  
  private Batch createNewBatch(long number) {
    
    Batch newBatch = Batch.builder()
      .number(number + 1)
      .deleted(false)
      .build();
    return batchRepository.save(newBatch);
  }
  
  @Override
  public void deleteBatch(long batchNumber) {
    
    Optional<Batch> targetBatch = batchRepository.findByNumberAndDeletedIsFalse(
      batchNumber);
    
    if (!targetBatch.isPresent()) {
      throw new NotFoundException("Delete Batch Not Found");
    } else {
      markBatch(targetBatch.get(), true);
    }
  }
  
  private Batch markBatch(Batch batch, boolean deleted) {
    
    batch.setDeleted(deleted);
    
    return batchRepository.save(batch);
  }
  
}
