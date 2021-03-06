package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.*;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyQuestionnaireService {

  Page<Questionnaire> getQuestionnairesByMemberLoginAsAppraiser(
    User memberLogin, String search, Pageable pageable
  );

  List<QuestionnaireParticipant> getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(
    Questionnaire questionnaire, User memberLogin
  );

  List<QuestionnaireResponse> getListAppraiseeDone(
    Questionnaire questionnaire, User memberLogin
  );

  QuestionnaireParticipant getQuestionnaireParticipantById(
    String questionnaireParticipantId
  );

  List<QuestionQuestionnaire> getQuestionsFromQuestionnaire(
    Questionnaire questionnaire
  );

  void createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(
    Questionnaire questionnaire, List<QuestionResponseQueue> questionResponses,
    User memberLogin, User appraisee
  );

}
