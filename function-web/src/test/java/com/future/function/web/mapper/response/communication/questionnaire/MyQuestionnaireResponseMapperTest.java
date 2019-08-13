package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraisalDataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.AppraiseeResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireResponse;
import com.future.function.web.model.response.feature.core.BatchWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MyQuestionnaireResponseMapperTest {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String NO_BATCH = "No-Batch";

  private static final String UNIVERSITY = "itb";

  private static final String APPRAISEE_RESPONSE_ID = "appraiseeId";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID = "questionniareParticipantId";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID_2 = "questionniareParticipantId2";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTION_ID = "questionId";

  private static final String QUESTION_DESCRIPTION = "questionDescription";

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

  private static final BatchWebResponse BATCH_WEB_RESPONSE = BatchWebResponse.builder()
    .id(BATCH_ID)
    .build();

  private static final AppraiseeResponse APPRAISEE_RESPONSE = AppraiseeResponse.builder()
    .id(APPRAISEE_RESPONSE_ID)
    .name(MEMBER_NAME_1)
    .avatar(THUMBNAIL_URL)
    .batch(BATCH_WEB_RESPONSE)
    .role(Role.STUDENT.toString())
    .university(UNIVERSITY)
    .build();

  private static final QuestionnaireParticipant QUESTIONNAIRE_PARTICIPANT =
    QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID)
      .member(MEMBER_1)
      .build();

  private static final QuestionnaireParticipant QUESTIONNAIRE_PARTICIPANT_2 =
    QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID_2)
      .member(MEMBER_2)
      .build();

  private static final Questionnaire QUESTIONNAIRE = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID_1)
    .title(QUESTIONNAIRE_TITLE)
    .description(QUESTIONNAIRE_DESCRIPTION)
    .startDate(START_DATE)
    .dueDate(DUE_DATE)
    .build();

  private static final QuestionQuestionnaire QUESTION_QUESTIONNAIRE =
    QuestionQuestionnaire.builder()
      .id(QUESTION_ID)
      .questionnaire(QUESTIONNAIRE)
      .description(QUESTION_DESCRIPTION)
      .build();


  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {
  }

  @Test
  public void toDataResponseAppraiseeResponseList() {

    DataResponse<List<AppraiseeResponse>> data =
      MyQuestionnaireResponseMapper.toDataResponseAppraiseeResponseList(
        Arrays.asList(QUESTIONNAIRE_PARTICIPANT),
        URL_PREFIX
    );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(MEMBER_ID_1);
    assertThat(data.getData().get(0).getName()).isEqualTo(MEMBER_NAME_1);
    assertThat(data.getData().get(0).getBatch().getId()).isEqualTo(BATCH_ID);
    assertThat(data.getData().get(0).getAvatar()).isEqualTo(URL_PREFIX.concat(THUMBNAIL_URL));
    assertThat(data.getData().get(0).getRole()).isEqualTo(Role.STUDENT.toString());
    assertThat(data.getData().get(0).getUniversity()).isEqualTo(UNIVERSITY);

    data = MyQuestionnaireResponseMapper.toDataResponseAppraiseeResponseList(
      Arrays.asList(QUESTIONNAIRE_PARTICIPANT_2),
      URL_PREFIX
    );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(MEMBER_ID_2);
    assertThat(data.getData().get(0).getName()).isEqualTo(MEMBER_NAME_2);
    assertThat(data.getData().get(0).getBatch().getId()).isEqualTo(NO_BATCH);
    assertThat(data.getData().get(0).getAvatar()).isEqualTo(URL_PREFIX.concat(THUMBNAIL_URL));
    assertThat(data.getData().get(0).getRole()).isEqualTo(Role.MENTOR.toString());

  }

  @Test
  public void toDataResponseQuestionnaireSummaryDescriptionResponse() {

    DataResponse<AppraisalDataResponse> data =
      MyQuestionnaireResponseMapper
        .toDataResponseQuestionnaireSummaryDescriptionResponse(QUESTIONNAIRE, MEMBER_1, URL_PREFIX);

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getQuestionnaireDetail().getId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().getAppraisee().getId()).isEqualTo(MEMBER_ID_1);

  }

  @Test
  public void toDataResponseQuestionQuestionnaireResponseList() {
    DataResponse<List<QuestionQuestionnaireResponse>> data =
      MyQuestionnaireResponseMapper.toDataResponseQuestionQuestionnaireResponseList(
        Arrays.asList(QUESTION_QUESTIONNAIRE)
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(QUESTION_ID);
    assertThat(data.getData().get(0).getQuestionnaireId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().get(0).getDescription()).isEqualTo(QUESTION_DESCRIPTION);
  }

}
