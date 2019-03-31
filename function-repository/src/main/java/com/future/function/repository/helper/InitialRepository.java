package com.future.function.repository.helper;

import com.future.function.model.entity.sample.InitialEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitialRepository
  extends MongoRepository<InitialEntity, String> {}
