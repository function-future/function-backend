package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {

  Optional<QuestionBank> findByIdAndDeletedFalse(String id);

  Page<QuestionBank> findAllByDeletedFalse(Pageable pageable);

}
