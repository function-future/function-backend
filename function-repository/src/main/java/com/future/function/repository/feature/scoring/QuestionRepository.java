package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

  Optional<Question> findByIdAndDeletedFalse(String id);

  Page<Question> findAllByQuestionBankId(String questionBankId, Pageable pageable);

  List<Question> findAllByQuestionBankId(String questionBankId);
}
