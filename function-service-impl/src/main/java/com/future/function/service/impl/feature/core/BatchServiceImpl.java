package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.session.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class BatchServiceImpl implements BatchService {
  
  private final BatchRepository batchRepository;
  
  @Autowired
  public BatchServiceImpl(BatchRepository batchRepository) {
    
    this.batchRepository = batchRepository;
  }
  
  @Override
  public Page<Batch> getBatches(Session session, Pageable pageable) {
  
    return Optional.of(session)
      .filter(this::hasNonStudentRole)
      .map(ignored -> batchRepository.findAllByDeletedFalse(pageable))
      .orElseGet(
        () -> batchRepository.findAllByIdAndDeletedFalse(session.getBatchId(),
                                                         pageable
        ));
  }
  
  private boolean hasNonStudentRole(Session session) {
  
    return Stream.of(Role.ADMIN, Role.JUDGE, Role.MENTOR)
      .anyMatch(role -> role.equals(session.getRole()));
  }
  
  @Override
  public Batch getBatchByCode(String code) {
    
    return batchRepository.findByCodeAndDeletedFalse(code)
      .orElseThrow(() -> new NotFoundException("Get Batch Not Found"));
  }
  
  @Override
  public Batch getBatchById(String batchId) {
    
    return Optional.ofNullable(batchId)
      .map(batchRepository::findOne)
      .filter(foundBatch -> !foundBatch.isDeleted())
      .orElseThrow(() -> new NotFoundException("Get Batch Not Found"));
  }
  
  @Override
  public Batch createBatch(Batch batch) {
    
    batchRepository.save(batch);
    
    return batchRepository.findFirstByDeletedFalseOrderByUpdatedAtDesc()
      .orElseThrow(() -> new NotFoundException("Saved Batch Not Found"));
  }
  
  @Override
  public Batch updateBatch(Batch batch) {
    
    return Optional.of(batch)
      .map(b -> batchRepository.findOne(b.getId()))
      .filter(foundBatch -> !foundBatch.isDeleted())
      .map(foundBatch -> copyPropertiesAndSaveBatch(batch, foundBatch))
      .flatMap(
        ignored -> batchRepository.findFirstByDeletedFalseOrderByUpdatedAtDesc())
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
      .filter(foundBatch -> !foundBatch.isDeleted())
      .ifPresent(batch -> {
        batch.setDeleted(true);
        batchRepository.save(batch);
      });
  }
  
}
