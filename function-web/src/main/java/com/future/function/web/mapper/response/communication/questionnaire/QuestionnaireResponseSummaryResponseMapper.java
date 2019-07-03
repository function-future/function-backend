package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.*;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.xml.ws.Response;
import java.util.List;
import java.util.stream.Collectors;

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

  public static DataResponse<List<QuestionQuestionnaireSummaryResponse>> toDataResponseQuestionQuestionnaireSummaryResponseList(List<QuestionResponseSummary> data) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, toListQuestionQuestionnaireSummaryResponse(data));
  }

  private static List<QuestionQuestionnaireSummaryResponse> toListQuestionQuestionnaireSummaryResponse(List<QuestionResponseSummary> data) {
    return data.stream()
            .map(questionResponseSummary -> toQuestionQuestionnaireSummaryResponse(questionResponseSummary))
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
            .desc(questionQuestionnaire.getDescription())
            .build();
  }


  public static DataResponse<QuestionAnswerDetailResponse> toDataResponseQuestionAnswerDetailResponse(QuestionnaireResponseSummary questionnaireResponseSummary, QuestionResponseSummary questionResponseSummary, List<QuestionResponse> questionResponseByQuestionResponseSummary) {
    return ResponseHelper.toDataResponse(
            HttpStatus.OK,
            toQuestionAnswerDetailResponse(
                    questionnaireResponseSummary,
                    questionResponseSummary,
                    questionResponseByQuestionResponseSummary)
    );
  }

  private static QuestionAnswerDetailResponse toQuestionAnswerDetailResponse(QuestionnaireResponseSummary questionnaireResponseSummary, QuestionResponseSummary questionResponseSummary, List<QuestionResponse> questionResponseByQuestionResponseSummary) {

    return QuestionAnswerDetailResponse.builder()
            .questionnaireSummary(toQuestionnaireSummaryDescriptionResponse(questionnaireResponseSummary))
            .questionSummary(toQuestionQuestionnaireSummaryResponse(questionResponseSummary))
            .QuestionResponse(toQuestionAnswerReponseList(questionResponseByQuestionResponseSummary))
            .build();
  }

  private static List<QuestionAnswerResponse> toQuestionAnswerReponseList(List<QuestionResponse> data) {
    return data.stream()
            .map(questionResponse -> toQuestionAnswerReponse(questionResponse))
            .collect(Collectors.toList());

  }

  private static QuestionAnswerResponse toQuestionAnswerReponse(QuestionResponse questionResponse) {
    return QuestionAnswerResponse.builder()
            .name(questionResponse.getAppraiser().getName())
            .avatar(questionResponse.getAppraiser().getPictureV2().getThumbnailUrl())
            .score(questionResponse.getScore())
            .build();

  }

}
