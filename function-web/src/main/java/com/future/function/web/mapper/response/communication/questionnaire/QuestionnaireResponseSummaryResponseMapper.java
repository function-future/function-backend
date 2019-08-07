package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraiseeResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionAnswerResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSummaryDescriptionResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionnaireResponseSummaryResponseMapper {

  private static final String NO_BATCH = "No Batch";

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
            .avatar(getThumnailUrl(appraisee))
            .batch(toBatchResponse(appraisee.getBatch()))
            .university(appraisee.getUniversity())
            .build();
  }

  private static BatchWebResponse toBatchResponse(Batch batch) {
    if(batch == null) {
      return BatchWebResponse.builder()
              .id(NO_BATCH)
              .name(NO_BATCH)
              .code(NO_BATCH)
              .build();
    }
    return BatchWebResponse.builder()
            .id(batch.getId())
            .name(batch.getName())
            .code(batch.getCode())
            .build();

  }

  public static DataResponse<List<QuestionQuestionnaireSummaryResponse>> toDataResponseQuestionQuestionnaireSummaryResponseList(List<QuestionResponseSummary> data) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, toListQuestionQuestionnaireSummaryResponse(data));
  }

  private static List<QuestionQuestionnaireSummaryResponse> toListQuestionQuestionnaireSummaryResponse(List<QuestionResponseSummary> data) {
    return data.stream()
            .map(QuestionnaireResponseSummaryResponseMapper::toQuestionQuestionnaireSummaryResponse)
            .collect(Collectors.toList());
  }

  public static  DataResponse<QuestionQuestionnaireSummaryResponse> toDataResponseQuestionQuestionnaireSummaryResponse(QuestionResponseSummary questionnaireResponseSummary){

    return ResponseHelper.toDataResponse(HttpStatus.OK, toQuestionQuestionnaireSummaryResponse(questionnaireResponseSummary));
  }

  private static QuestionQuestionnaireSummaryResponse toQuestionQuestionnaireSummaryResponse(QuestionResponseSummary questionnaireResponseSummary){
    return QuestionQuestionnaireSummaryResponse.builder()
            .id(questionnaireResponseSummary.getId())
            .question(toQuestionQuestionnaireResponse(questionnaireResponseSummary.getQuestion()))
            .score(questionnaireResponseSummary.getScoreSummary().getAverage())
            .build();
  }

  private static QuestionQuestionnaireResponse toQuestionQuestionnaireResponse (QuestionQuestionnaire questionQuestionnaire) {
    return QuestionQuestionnaireResponse.builder()
            .id(questionQuestionnaire.getId())
            .questionnaireId(questionQuestionnaire.getQuestionnaire().getId())
            .description(questionQuestionnaire.getDescription())
            .build();
  }


  public static DataResponse<List<QuestionAnswerResponse>> toDataResponseQuestionAnswerDetailResponse(List<QuestionResponse> questionResponseByQuestionResponseSummary) {
    return ResponseHelper.toDataResponse(
            HttpStatus.OK,
            toQuestionAnswerReponseList(
                    questionResponseByQuestionResponseSummary)
    );
  }

  private static List<QuestionAnswerResponse> toQuestionAnswerReponseList(List<QuestionResponse> data) {
    return data.stream()
            .map(QuestionnaireResponseSummaryResponseMapper::toQuestionAnswerReponse)
            .collect(Collectors.toList());

  }

  private static QuestionAnswerResponse toQuestionAnswerReponse(QuestionResponse questionResponse) {
    return QuestionAnswerResponse.builder()
            .name(questionResponse.getAppraiser().getName())
            .avatar(getThumnailUrl(questionResponse.getAppraiser()))
            .score(questionResponse.getScore())
            .build();

  }

  private static String getThumnailUrl(User user) {
    return Optional.ofNullable(user)
      .map(User::getPictureV2)
      .map(FileV2::getThumbnailUrl)
      .orElse(null);
  }

}
