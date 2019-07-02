package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionnaireResponseSummaryResponseMapper {

  public static DataResponse<QuestionnaireSummaryDescriptionResponse> toDataResponseQuestionnaireDataSummaryDescription(
    QuestionnaireResponseSummary questionnaireResponseSummary){

    return ResponseHelper.toDataResponse(HttpStatus.OK, toQuestionnaireSummaryDescriptionResponse(questionnaireResponseSummary));
  }

  private static QuestionnaireSummaryDescriptionResponse toQuestionnaireSummaryDescriptionResponse(
    QuestionnaireResponseSummary questionnaireResponseSummary) {
    return QuestionnaireSummaryDescriptionResponse.builder()
            .questionnaireDetail(toQuestionnaireDetailResponse(questionnaireResponseSummary.getQuestionnaire()))
            .appraisee(toAppraiseeResponse(questionnaireResponseSummary.getAppraisee()))
            .rating(questionnaireResponseSummary.getScoreSummary().getAverage())
            .build();
  }

  private static QuestionnaireDetailResponse toQuestionnaireDetailResponse(Questionnaire questionnaire) {
    return QuestionnaireDetailResponse.builder()
            .id(questionnaire.getId())
            .title(questionnaire.getTitle())
            .description(questionnaire.getDescription())
            .startDate(questionnaire.getStartDate())
            .dueDate(questionnaire.getDueDate())
            .build();
  }

  private static AppraiseeResponse toAppraiseeResponse(User appraisee) {
    return AppraiseeResponse.builder()
            .id(appraisee.getId())
            .name(appraisee.getName())
            .avatar(appraisee.getPictureV2().getThumbnailUrl())
            .batch(toBatchResponse(appraisee.getBatch()))
            .university(appraisee.getUniversity())
            .build();
  }

  private static BatchResponse toBatchResponse(Batch batch) {
    return BatchResponse.builder()
      .id(batch.getId())
      .name(batch.getName())
      .code(batch.getCode())
      .build();
  }

  public static DataResponse<List<QuestionQuestionnaireSummaryResponse>> toDataResponseQuestionQuestionnaireSummaryResponse(

  )
}
