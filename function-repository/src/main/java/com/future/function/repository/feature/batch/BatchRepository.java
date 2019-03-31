package com.future.function.repository.feature.batch;

import com.future.function.model.entity.feature.batch.Batch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {
  
  List<Batch> findAllByDeletedIsFalse();
  
  Optional<Batch> findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  
  Optional<Batch> findByNumberAndDeletedIsFalse(long number);
  
}
