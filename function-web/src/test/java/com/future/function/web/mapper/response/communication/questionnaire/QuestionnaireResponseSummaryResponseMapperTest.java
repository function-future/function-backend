package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.Answer;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionAnswerResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireSummaryResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSummaryDescriptionResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionnaireResponseSummaryResponseMapperTest {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTIONNAIRE_RESPONSE_SUMMARY_ID = "questionnaireResponseSummaryId1";

  private static final String QUESTIONNAIRE_RESPONSE_SUMMARY_ID_2 = "questionnaireResponseSummaryId2";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String UNIVERSITY = "itb";

  private static final String QUESTION_ID = "questionId";

  private static final String QUESTION_DESCRIPTION = "questionDescription";

  private static final String QUESTION_RESPONSE_SUMMARY_ID = "questionQuestionnaireSummaryResponseid1";

  private static final String QUESTION_RESPONSE_ID = "questionResponseId";

  private static final Answer SCORE = Answer.builder()
    .minimum(0)
    .maximum(6)
    .average(3)
    .build();

  private static final User MEMBER_1 = User.builder()
    .id(MEMBER_ID_1)
    .name(MEMBER_NAME_1)
    .pictureV2(FileV2.builder().thumbnailUrl(THUMBNAIL_URL).build())
    .batch(Batch.builder().id(BATCH_ID).build())
    .role(Role.STUDENT)
    .university(UNIVERSITY)
    .build();

  private static final User MEMBER_2 = User.builder()
    .id(MEMBER_ID_2)
    .name(MEMBER_NAME_2)
    .pictureV2(FileV2.builder().thumbnailUrl(THUMBNAIL_URL).build())
    .role(Role.MENTOR)
    .build();

  private static final Questionnaire QUESTIONNAIRE = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID_1)
    .title(QUESTIONNAIRE_TITLE)
    .description(QUESTIONNAIRE_DESCRIPTION)
    .startDate(START_DATE)
    .dueDate(DUE_DATE)
    .build();

  private static final QuestionnaireResponseSummary QUESTIONNAIRE_RESPONSE_SUMMARY =
    QuestionnaireResponseSummary.builder()
      .id(QUESTIONNAIRE_RESPONSE_SUMMARY_ID)
      .questionnaire(QUESTIONNAIRE)
      .appraisee(MEMBER_1)
      .scoreSummary(SCORE)
      .build();

  private static final QuestionnaireResponseSummary QUESTIONNAIRE_RESPONSE_SUMMARY_2 =
    QuestionnaireResponseSummary.builder()
      .id(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_2)
      .questionnaire(QUESTIONNAIRE)
      .appraisee(MEMBER_2)
      .scoreSummary(SCORE)
      .build();

  private static final QuestionQuestionnaire QUESTION_QUESTIONNAIRE =
    QuestionQuestionnaire.builder()
      .id(QUESTION_ID)
      .questionnaire(QUESTIONNAIRE)
      .description(QUESTION_DESCRIPTION)
      .build();

  private static final QuestionResponseSummary QUESTION_RESPONSE_SUMMARY =
    QuestionResponseSummary.builder()
      .id(QUESTION_RESPONSE_SUMMARY_ID)
      .question(QUESTION_QUESTIONNAIRE)
      .questionnaire(QUESTIONNAIRE)
      .scoreSummary(SCORE)
      .build();

  private static final QuestionResponse QUESTION_RESPONSE =
    QuestionResponse.builder()
      .id(QUESTION_RESPONSE_ID)
      .score(Float.valueOf(3))
      .appraiser(MEMBER_1)
      .question(QUESTION_QUESTIONNAIRE)
      .build();

  @Test
  public void toDataResponseQuestionnaireDataSummaryDescription() {
    DataResponse<QuestionnaireSummaryDescriptionResponse> data =
      QuestionnaireResponseSummaryResponseMapper
        .toDataResponseQuestionnaireDataSummaryDescription(
          QUESTIONNAIRE_RESPONSE_SUMMARY,
          URL_PREFIX
        );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getAppraisee().getId()).isEqualTo(MEMBER_ID_1);
    assertThat(data.getData().getQuestionnaireDetail().getId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().getRating()).isEqualTo(SCORE.getAverage());

    data = QuestionnaireResponseSummaryResponseMapper
        .toDataResponseQuestionnaireDataSummaryDescription(
          QUESTIONNAIRE_RESPONSE_SUMMARY_2,
          URL_PREFIX
        );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getAppraisee().getId()).isEqualTo(MEMBER_ID_2);
    assertThat(data.getData().getQuestionnaireDetail().getId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().getRating()).isEqualTo(SCORE.getAverage());
  }

  @Test
  public void toDataResponseQuestionQuestionnaireSummaryResponseList() {
    DataResponse<List<QuestionQuestionnaireSummaryResponse>> data =
      QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionQuestionnaireSummaryResponseList(
        Arrays.asList(QUESTION_RESPONSE_SUMMARY)
      );
    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(QUESTION_RESPONSE_SUMMARY_ID);
  }

  @Test
  public void toDataResponseQuestionQuestionnaireSummaryResponse() {

    DataResponse<QuestionQuestionnaireSummaryResponse> data =
      QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionQuestionnaireSummaryResponse(
        QUESTION_RESPONSE_SUMMARY
      );
    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getId()).isEqualTo(QUESTION_RESPONSE_SUMMARY_ID);
  }

  @Test
  public void toDataResponseQuestionAnswerDetailResponse() {
    DataResponse<List<QuestionAnswerResponse>> data =
      QuestionnaireResponseSummaryResponseMapper.toDataResponseQuestionAnswerDetailResponse(
        Arrays.asList(QUESTION_RESPONSE),
        URL_PREFIX
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getName()).isEqualTo(MEMBER_NAME_1);
    assertThat(data.getData().get(0).getAvatar()).isEqualTo(URL_PREFIX.concat(THUMBNAIL_URL));


  }
}
