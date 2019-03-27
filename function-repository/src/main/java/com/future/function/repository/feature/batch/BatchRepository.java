package com.future.function.repository.feature.batch;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.future.function.model.entity.feature.batch.Batch;

@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {

  Optional<Batch> findByNumber(long number);

}
