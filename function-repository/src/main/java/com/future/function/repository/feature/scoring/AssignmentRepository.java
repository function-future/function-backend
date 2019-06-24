package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Assignment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends MongoRepository<Assignment, String> {

  Optional<Assignment> findByIdAndDeletedFalse(String id);

  Page<Assignment> findAllByBatchCode(String batchCode, Pageable pageable);

}
