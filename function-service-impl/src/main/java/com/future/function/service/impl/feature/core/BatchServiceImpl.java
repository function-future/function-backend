package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.impl.helper.CopyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
  
  /**
   * {@inheritDoc}
   *
   * @return {@code Batch} - Batches found in database.
   */
  @Override
  public Page<Batch> getBatches(Pageable pageable) {
    
    return batchRepository.findAllByIdIsNotNull(pageable);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param code Number of the batch to be retrieved.
   *
   * @return {@code Batch} - The batch object found in database.
   */
  @Override
  public Batch getBatchByCode(String code) {
    
    return batchRepository.findByCode(code)
      .orElseThrow(() -> new NotFoundException("Get Batch Not Found"));
  }
  
  @Override
  public Batch getBatchById(String batchId) {
    
    return Optional.ofNullable(batchId)
      .map(batchRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Get Batch Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @return {@code Batch} - The batch object of the saved data.
   */
  @Override
  public Batch createBatch(Batch batch) {
    
    batchRepository.save(batch);
    
    return batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc()
      .orElseThrow(() -> new NotFoundException("Saved Batch Not Found"));
  }
  
  @Override
  public Batch updateBatch(Batch batch) {
    
    return Optional.of(batch)
      .map(b -> batchRepository.findOne(b.getId()))
      .map(foundBatch -> copyPropertiesAndSaveBatch(batch, foundBatch))
      .flatMap(
        ignored -> batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc())
      .orElse(batch);
  }
  
  private Batch copyPropertiesAndSaveBatch(Batch batch, Batch foundBatch) {
    
    CopyHelper.copyProperties(batch, foundBatch);
    return batchRepository.save(foundBatch);
  }
  
  @Override
  public void deleteBatch(String batchId) {
    
    Optional.ofNullable(batchId)
      .map(batchRepository::findOne)
      .ifPresent(batch -> {
        batch.setDeleted(true);
        batchRepository.save(batch);
      });
  }
  
}
