package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.Answer;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.UserSummaryResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionnaireResultsResponseMapperTest {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String NO_BATCH = "No-Batch";

  private static final String UNIVERSITY = "itb";

  private static final String USER_SUMMARY_ID_1 = "userSummaryId1";

  private static final String USER_SUMMARY_ID_2 = "userSummaryId2";

  private static final Answer SCORE = Answer.builder()
    .minimum(0)
    .maximum(6)
    .average(3)
    .build();

  private static final User MEMBER_1 = User.builder()
    .id(MEMBER_ID_1)
    .name(MEMBER_NAME_1)
    .pictureV2(FileV2.builder()
                 .thumbnailUrl(THUMBNAIL_URL)
                 .build())
    .batch(Batch.builder()
             .id(BATCH_ID)
             .build())
    .role(Role.STUDENT)
    .university(UNIVERSITY)
    .build();

  private static final User MEMBER_2 = User.builder()
    .id(MEMBER_ID_2)
    .name(MEMBER_NAME_2)
    .pictureV2(FileV2.builder()
                 .thumbnailUrl(THUMBNAIL_URL)
                 .build())
    .role(Role.MENTOR)
    .build();

  private static final UserQuestionnaireSummary USER_SUMMARY_1 =
    UserQuestionnaireSummary.builder()
      .id(USER_SUMMARY_ID_1)
      .appraisee(MEMBER_1)
      .scoreSummary(SCORE)
      .build();

  private static final UserQuestionnaireSummary USER_SUMMARY_2 =
    UserQuestionnaireSummary.builder()
      .id(USER_SUMMARY_ID_2)
      .appraisee(MEMBER_2)
      .scoreSummary(SCORE)
      .build();

  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {

  }

  @Test
  public void toPagingUserSummaryResponse() {

    PagingResponse<UserSummaryResponse> data =
      QuestionnaireResultsResponseMapper.toPagingUserSummaryResponse(
        new PageImpl<>(
          Arrays.asList(USER_SUMMARY_1, USER_SUMMARY_2),
          PageHelper.toPageable(1, 2), 2
        ), URL_PREFIX);

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData()
                 .get(0)
                 .getId()).isEqualTo(USER_SUMMARY_ID_1);
    assertThat(data.getData()
                 .get(0)
                 .getMember()
                 .getId()).isEqualTo(MEMBER_ID_1);
    assertThat(data.getData()
                 .get(0)
                 .getRating()).isEqualTo(SCORE.getAverage());
    assertThat(data.getData()
                 .get(1)
                 .getId()).isEqualTo(USER_SUMMARY_ID_2);
    assertThat(data.getData()
                 .get(1)
                 .getMember()
                 .getId()).isEqualTo(MEMBER_ID_2);
    assertThat(data.getData()
                 .get(1)
                 .getRating()).isEqualTo(SCORE.getAverage());
  }

  @Test
  public void toDataResponseUserSummaryResponse() {

    DataResponse<UserSummaryResponse> data =
      QuestionnaireResultsResponseMapper.toDataResponseUserSummaryResponse(
        USER_SUMMARY_1, URL_PREFIX);

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData()
                 .getId()).isEqualTo(USER_SUMMARY_ID_1);
    assertThat(data.getData()
                 .getMember()
                 .getId()).isEqualTo(MEMBER_ID_1);
    assertThat(data.getData()
                 .getRating()).isEqualTo(SCORE.getAverage());
  }

}
