package com.future.function.web.mapper.request.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.questionnaire.QuestionQuestionnaireRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuestionQuestionnaireRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public QuestionQuestionnaireRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public QuestionQuestionnaire toQuestionQuestionnaire(QuestionQuestionnaireRequest questionQuestionnaireRequest,String questionQuestionnaireId, Questionnaire questionnaire) {
    return toValidateQuestionQuestionnaire(questionQuestionnaireRequest,questionQuestionnaireId,questionnaire);
  }

  private QuestionQuestionnaire toValidateQuestionQuestionnaire(
          QuestionQuestionnaireRequest questionQuestionnaireRequest,
          String questionQuestionnaireId,
          Questionnaire questionnaire
  ) {
    validator.validate(questionQuestionnaireRequest);

    QuestionQuestionnaire questionQuestionnaire =
            QuestionQuestionnaire.builder()
                    .description(questionQuestionnaireRequest.getDesc())
                    .questionnaire(questionnaire)
                    .build();

    if(questionQuestionnaireId != null) {
      questionQuestionnaire.setId(questionQuestionnaireId);
    }

    return questionQuestionnaire;
  }
}
