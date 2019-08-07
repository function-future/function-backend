package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.MemberResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.UserSummaryResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionnaireResultsResponseMapper {

  private static final String NO_BATCH = "No Batch";

  public static PagingResponse<UserSummaryResponse> toPagingUserSummaryResponse(
    Page<UserQuestionnaireSummary> data
  ) {
    return ResponseHelper.toPagingResponse(HttpStatus.OK, toUserSummaryResponseList(data), PageHelper.toPaging(data));
  }

  public static DataResponse<UserSummaryResponse> toDataResponseUserSummaryResponse(
          UserQuestionnaireSummary data
  ) {
    return ResponseHelper.toDataResponse(HttpStatus.OK, toUserSummaryResponse(data));
  }

  private static List<UserSummaryResponse> toUserSummaryResponseList(Page<UserQuestionnaireSummary> data) {
    return data.getContent()
            .stream()
            .map(QuestionnaireResultsResponseMapper::toUserSummaryResponse)
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

  private static String getThumnailUrl(User user) {
    return Optional.ofNullable(user)
      .map(User::getPictureV2)
      .map(FileV2::getThumbnailUrl)
      .orElse(null);
  }
}
