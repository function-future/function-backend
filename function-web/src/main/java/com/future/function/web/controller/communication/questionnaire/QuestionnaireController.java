package com.future.function.web.controller.communication.questionnaire;


import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/communication/questionnaires")
@WithAnyRole(roles = { Role.ADMIN })
public class QuestionnaireController {

  private final QuestionnaireService questionnaireService;

  @Autowired
  public QuestionnaireController(QuestionnaireService questionnaireService) {
    this.questionnaireService = questionnaireService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<QuestionnaireDetailResponse> getQuestionnaires(){
    return null;
  }
}
