package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Option;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends MongoRepository<Option, String> {

  Optional<Option> findByIdAndDeletedFalse(String id);

  List<Option> findAllByQuestionId(String questionId);
}
