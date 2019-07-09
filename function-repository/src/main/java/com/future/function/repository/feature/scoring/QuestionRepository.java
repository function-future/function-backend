package com.future.function.repository.feature.scoring;

import com.future.function.model.entity.feature.scoring.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {

  Optional<Question> findByIdAndDeletedFalse(String id);

  Page<Question> findAllByQuestionBankIdAndDeletedFalse(String questionBankId, Pageable pageable);

  List<Question> findAllByQuestionBankIdAndDeletedFalse(String questionBankId);
}
