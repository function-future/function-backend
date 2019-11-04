package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.scoring.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AssignmentRepository
  extends MongoRepository<Assignment, String> {

  Optional<Assignment> findByIdAndDeletedFalse(String id);

  Page<Assignment> findAllByBatchAndDeletedFalseAndDeadlineBeforeOrderByDeadlineAsc(
      Batch batch, Long deadline, Pageable pageable
  );

  Page<Assignment> findAllByBatchAndDeletedFalseAndDeadlineAfterOrderByDeadlineDesc(
      Batch batch, Long deadline, Pageable pageable
  );

}
