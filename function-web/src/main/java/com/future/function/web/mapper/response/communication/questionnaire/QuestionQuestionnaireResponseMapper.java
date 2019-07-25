package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionQuestionnaireResponseMapper {

  public static DataResponse<List<QuestionQuestionnaireResponse>> toDataResponseListQuestionQuestionnaireResponse(List<QuestionQuestionnaire> questionsByIdQuestionnaire, HttpStatus httpStatus) {

    return ResponseHelper.toDataResponse( httpStatus, toQuestionQuestionnaireResponseList(questionsByIdQuestionnaire));
  }

  public static List<QuestionQuestionnaireResponse> toQuestionQuestionnaireResponseList(List<QuestionQuestionnaire> questionQuestionnaires) {
    return questionQuestionnaires.stream()
            .map(questionQuestionnaire ->
              toQuestionQuestionnaireResponse(questionQuestionnaire)
            )
            .collect(Collectors.toList());
  }

  public static QuestionQuestionnaireResponse toQuestionQuestionnaireResponse(QuestionQuestionnaire questionQuestionnaire) {
      return new QuestionQuestionnaireResponse().builder()
            .id(questionQuestionnaire.getId())
            .questionnaireId(questionQuestionnaire.getQuestionnaire().getId())
            .description(questionQuestionnaire.getDescription())
            .build();
  }

  public static DataResponse<QuestionQuestionnaireResponse> toDataResponseQuestionQuestionnaireResponse(QuestionQuestionnaire questionQuestionnaire, HttpStatus httpStatus) {
    return ResponseHelper.toDataResponse(httpStatus, toQuestionQuestionnaireResponse(questionQuestionnaire));
  }
}
