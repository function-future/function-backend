package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BatchRepository extends MongoRepository<Batch, String> {

  List<Batch> findAllByDeletedFalse();

  List<Batch> findAllByIdAndDeletedFalse(String id);

  Optional<Batch> findFirstByDeletedFalseOrderByUpdatedAtDesc();

  Optional<Batch> findByCodeAndDeletedFalse(String code);

  Boolean existsDistinctByCodeAndDeletedFalse(String code);

}
