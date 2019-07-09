package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraisalDataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraiseeResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.future.function.web.mapper.response.communication.questionnaire.QuestionQuestionnaireResponseMapper.toQuestionQuestionnaireResponseList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyQuestionnaireResponseMapper {

  public static DataResponse<List<AppraiseeResponse>> toDataResponseAppraiseeResponseList(List<QuestionnaireParticipant> data) {

    return ResponseHelper.toDataResponse(
            HttpStatus.OK,
            toAppraiseeResponseList(data)
    );
  }

  private static List<AppraiseeResponse> toAppraiseeResponseList(List<QuestionnaireParticipant> data) {
    return data.stream()
            .map(questionnaireParticipant -> toAppraiseeResponse(questionnaireParticipant))
            .collect(Collectors.toList());
  }

  private static AppraiseeResponse toAppraiseeResponse(QuestionnaireParticipant questionnaireParticipant) {
    return AppraiseeResponse.builder()
            .id(questionnaireParticipant.getMember().getId())
            .name(questionnaireParticipant.getMember().getName())
            .avatar(questionnaireParticipant.getMember().getPictureV2().getThumbnailUrl())
            .batch(toBatchResponse(questionnaireParticipant.getMember().getBatch()))
            .university(questionnaireParticipant.getMember().getUniversity())
            .build();
  }

  private static BatchWebResponse toBatchResponse(Batch batch) {
    return BatchWebResponse.builder()
            .id(batch.getId())
            .name(batch.getName())
            .code(batch.getCode())
            .build();
  }

  public static DataResponse<AppraisalDataResponse> toDataResponseQuestionnaireSummaryDescriptionResponse(Questionnaire questionnaire, User user) {
    return ResponseHelper.toDataResponse(
            HttpStatus.OK,
            toQuestionnaireSummaryDescriptionResponse(questionnaire, user)
    );
  }

  private static AppraisalDataResponse toQuestionnaireSummaryDescriptionResponse(Questionnaire questionnaire, User user) {
    return AppraisalDataResponse.builder()
            .questionnaireDetail(toQuestionnaireDetailResponse(questionnaire))
            .appraisee(toAppraiseeResponse(user))
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

  public static DataResponse<List<QuestionQuestionnaireResponse>> toDataResponseQuestionQuestionnaireResponseList(List<QuestionQuestionnaire> data) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, toQuestionQuestionnaireResponseList(data));
  }
}
