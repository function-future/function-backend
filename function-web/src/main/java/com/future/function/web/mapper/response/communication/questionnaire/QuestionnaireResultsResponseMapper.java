package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.BatchResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.MemberResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.UserSummaryResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionnaireResultsResponseMapper {
  public static PagingResponse<UserSummaryResponse> toPagingUserSummaryResponse(
    Page<UserQuestionnaireSummary> data
  ) {
    return ResponseHelper.toPagingResponse(HttpStatus.OK, toUserSummaryResponseList(data), PageHelper.toPaging(data));
  }

  public static DataResponse<List<UserSummaryResponse>> toDataResponseUserSummaryResponse(
    Page<UserQuestionnaireSummary> data
  ) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, toUserSummaryResponseList(data));
  }

  private static List<UserSummaryResponse> toUserSummaryResponseList(Page<UserQuestionnaireSummary> data) {
    return data.getContent()
            .stream()
            .map(userSummary ->  toUserSummaryResponse(userSummary))
            .collect(Collectors.toList());
  }

  private static UserSummaryResponse toUserSummaryResponse(UserQuestionnaireSummary userSummary) {
    return UserSummaryResponse.builder()
            .id(userSummary.getId())
            .member(toMemberResponse(userSummary.getAppraisee()))
            .rating(userSummary.getScoreSummary().getAverage())
            .build();
  }

  private static MemberResponse toMemberResponse(User appraisee) {
    return MemberResponse.builder()
            .id(appraisee.getId())
            .role(appraisee.getRole().toString())
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


}
