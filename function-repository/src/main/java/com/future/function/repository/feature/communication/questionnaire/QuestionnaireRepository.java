package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionnaireRepository extends MongoRepository<Questionnaire, String> {

  /**
   * Find all paged questionnaire
   *
   * @param pageable pageable object for paging
   *
   * @return {@code Page<Questionnaire>} - paged qustionnaire list from database
   */
  Page<Questionnaire> findAllByDeletedFalseOrderByCreatedAtDesc (Pageable pageable);

  /**
   * Find questionnaire by questionnaireId
   *
   * @param questionnaireId id of to be searched
   *
   * @return {@code Questionnaire} - qustionnaire from database
   */

  Optional<Questionnaire> findById (String questionnaireId);

  /**
   * Find questionnaire by title name consist title name
   *
   * @param titleName title to be search
   * @param pageable pageable object for paging
   *
   * @return {@code Page<Questionnaire>} - paged questionnaire list from database
   */

  Page<Questionnaire> findAllByTitleIgnoreCaseContainingAndDeletedFalseOrderByCreatedAtDesc(String titleName, Pageable pageable);

}
