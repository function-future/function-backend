package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Quiz;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {

  Optional<Quiz> findByIdAndDeletedFalse(String id);

}
