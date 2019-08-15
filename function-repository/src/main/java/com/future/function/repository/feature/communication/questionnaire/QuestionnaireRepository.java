package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionnaireRepository
  extends MongoRepository<Questionnaire, String> {

  Page<Questionnaire> findAllByDeletedFalseOrderByCreatedAtDesc(
    Pageable pageable
  );

  Optional<Questionnaire> findById(String questionnaireId);

  Page<Questionnaire> findAllByTitleIgnoreCaseContainingAndDeletedFalseOrderByCreatedAtDesc(
    String titleName, Pageable pageable
  );

}
