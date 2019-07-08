package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.QuestionBank;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends MongoRepository<QuestionBank, String> {

  Optional<QuestionBank> findByIdAndDeletedFalse(String id);

  Page<QuestionBank> findAllByDeletedFalse(Pageable pageable);

}
