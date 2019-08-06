package com.future.function.web.mapper.request.communication.questionnaire;


import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.questionnaire.QuestionnaireRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuestionnaireRequestMapper {

  private final RequestValidator validator;

  @Autowired
  public QuestionnaireRequestMapper(RequestValidator validator) {
    this.validator = validator;
  }

  public Questionnaire toQuestionnaire(QuestionnaireRequest questionnaireRequest, String questionnaireId){
    return toValidateQuestionnaire(questionnaireRequest, questionnaireId);
  }

  private Questionnaire toValidateQuestionnaire(QuestionnaireRequest questionnaireRequest, String questionnaireId) {
    validator.validate(questionnaireRequest);

    Questionnaire newQuestionnaire = Questionnaire.builder()
      .id(questionnaireId)
      .title(questionnaireRequest.getTitle())
      .description(questionnaireRequest.getDesc())
      .startDate(questionnaireRequest.getStartDate())
      .dueDate(questionnaireRequest.getDueDate())
      .build();

    return newQuestionnaire;
  }


}
