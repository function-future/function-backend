package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSimpleSummaryResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionnaireResponseMapper {


  public static PagingResponse<QuestionnaireDetailResponse> toPagingQuestionnaireDetailResponse(
          Page<Questionnaire> data
  ) {
    return ResponseHelper.toPagingResponse(HttpStatus.OK, toQuestionnaireDetailResponseList(data), PageHelper.toPaging(data));
  }

  private static List<QuestionnaireDetailResponse> toQuestionnaireDetailResponseList(Page<Questionnaire> data){
    return data.getContent()
            .stream()
            .map(questionnaire -> toQuestionnaireDetailResponse(questionnaire))
            .collect(Collectors.toList());
  }

  private static QuestionnaireDetailResponse toQuestionnaireDetailResponse(Questionnaire questionnaire){
    return QuestionnaireDetailResponse.builder()
            .id(questionnaire.getId())
            .title(questionnaire.getTitle())
            .description(questionnaire.getDescription())
            .startDate(questionnaire.getStartDate())
            .dueDate(questionnaire.getDueDate())
            .build();
  }

  public static DataResponse<QuestionnaireDetailResponse> toDataResponseQuestionnaireDetailResponse(Questionnaire questionnaire, HttpStatus httpStatus) {

    return ResponseHelper.toDataResponse(httpStatus,toQuestionnaireDetailResponse(questionnaire));
  }

  public static PagingResponse<QuestionnaireSimpleSummaryResponse> toPagingQuestionnaireSimpleSummaryResponse(
          Page<QuestionnaireResponseSummary> data,
          HttpStatus httpStatus) {
    return ResponseHelper.toPagingResponse(httpStatus, toQuestionnaireSimpleSummaryResponseList(data), PageHelper.toPaging(data));
  }

  private static List<QuestionnaireSimpleSummaryResponse> toQuestionnaireSimpleSummaryResponseList(Page<QuestionnaireResponseSummary> data) {
    return data.getContent()
            .stream()
            .map(questionnaireResponseSummary -> toQuestionnaireSimpleSummaryResponse(questionnaireResponseSummary))
            .collect(Collectors.toList());
  }

  private static QuestionnaireSimpleSummaryResponse toQuestionnaireSimpleSummaryResponse(QuestionnaireResponseSummary questionnaireResponseSummary) {
    return QuestionnaireSimpleSummaryResponse.builder()
            .id(questionnaireResponseSummary.getId())
            .desc(questionnaireResponseSummary.getQuestionnaire().getDescription())
            .status(questionnaireResponseSummary.getQuestionnaire().getDueDate() < System.currentTimeMillis() ? "FINISHED" : "ON_GOING")
            .duedate(questionnaireResponseSummary.getQuestionnaire().getDueDate())
            .score(questionnaireResponseSummary.getScoreSummary().getAverage())
            .build();
  }
}
