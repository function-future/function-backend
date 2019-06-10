package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class used to manipulate quiz entity in the database
 */
@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {

  /**
   * Used to find existing quiz with its id and is not deleted in the database
   * @param id (String)
   * @return Optional<Quiz>
   */
  Optional<Quiz> findByIdAndDeletedFalse(String id);

    Page<Quiz> findAllByDeletedFalse(Pageable pageable);

}
