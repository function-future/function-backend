package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuizRepository extends MongoRepository<Quiz, String> {

  Optional<Quiz> findByIdAndDeletedFalse(String id);

  Page<Quiz> findAllByBatchAndDeletedFalseAndEndDateBeforeOrderByEndDateAsc(Batch batch, Long endDate, Pageable pageable);

  Page<Quiz> findAllByBatchAndDeletedFalseAndEndDateAfterOrderByEndDateDesc(Batch batch, Long endDate, Pageable pageable);

  Boolean existsByIdAndDeletedFalse(String id);
}
