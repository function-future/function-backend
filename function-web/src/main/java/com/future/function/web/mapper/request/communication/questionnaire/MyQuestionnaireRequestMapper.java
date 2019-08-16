package com.future.function.web.mapper.request.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.web.model.request.communication.questionnaire.QuestionResponseRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MyQuestionnaireRequestMapper {

  private final QuestionnaireService questionnaireService;

  @Autowired
  public MyQuestionnaireRequestMapper(
    QuestionnaireService questionnaireService
  ) {

    this.questionnaireService = questionnaireService;
  }


  public List<QuestionResponse> toListQuestionResponse(
    List<QuestionResponseRequest> responses, User appraiser, User appraisee
  ) {

    return responses.stream()
      .map(response -> toQuestionResponse(response, appraiser, appraisee))
      .collect(Collectors.toList());
  }

  private QuestionResponse toQuestionResponse(
    QuestionResponseRequest response, User appraiser, User appraisee
  ) {

    return QuestionResponse.builder()
      .appraiser(appraiser)
      .appraisee(appraisee)
      .question(
        questionnaireService.getQuestionQuestionnaire(response.getIdQuestion()))
      .comment(response.getComment())
      .score(response.getScore())
      .build();
  }

}
