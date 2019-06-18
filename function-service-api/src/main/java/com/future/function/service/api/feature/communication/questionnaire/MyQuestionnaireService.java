package com.future.function.service.api.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponse;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface class for questionnaire logic operations declaration.
 */
public interface MyQuestionnaireService {

  Page<Questionnaire> getQuestionnairesByMemberLoginAsAppraiser(User memberLogin);

  List<QuestionnaireParticipant> getListAppraisedByQuestionnaireAndMemberLoginAsAppraiser(Questionnaire questionnaire, User memberLogin);

  QuestionnaireParticipant getAppraiseeDataByQuestionnaireAndAppraisee(Questionnaire questionnaire, User appraisee);

  List<QuestionQuestionnaire> getQuestionsFromQuestionnaire(Questionnaire questionnaire);

  QuestionnaireResponse createQuestionnaireResponseToAppraiseeFromMemberLoginAsAppraiser(Questionnaire questionnaire, User memberLogin, User appraisee);
}
