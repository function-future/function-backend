package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BatchRepository extends MongoRepository<Batch, String> {

  Page<Batch> findAllByDeletedFalse(Pageable pageable);

  Page<Batch> findAllByIdAndDeletedFalse(String id, Pageable pageable);

  Optional<Batch> findFirstByDeletedFalseOrderByUpdatedAtDesc();

  Optional<Batch> findByCodeAndDeletedFalse(String code);

}
