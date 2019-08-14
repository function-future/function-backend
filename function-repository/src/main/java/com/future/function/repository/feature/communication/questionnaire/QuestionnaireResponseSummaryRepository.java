package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionnaireResponseSummaryRepository extends MongoRepository<QuestionnaireResponseSummary, String> {

  Page<QuestionnaireResponseSummary> findAllByAppraiseeAndDeletedFalse(User appraisee, Pageable pageable);

  Optional<QuestionnaireResponseSummary> findByAppraiseeAndQuestionnaireAndDeletedFalse(User appraisee, Questionnaire questionnaire);
}
