package com.future.function.repository.helper;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.future.function.model.entity.sample.InitialEntity;

@Repository
public interface InitialRepository extends MongoRepository<InitialEntity, String> {}
