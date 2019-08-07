package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.common.enumeration.communication.ParticipantType;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireParticipant;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireParticipantDescriptionResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireParticipantResponse;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Author: ricky.kennedy
 * Created At: 1:41 PM 7/25/2019
 */
public class QuestionnaireParticipantResponseMapperTest {

  private static final String URL_PREFIX = "urlPrefix";

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String UNIVERSITY = "itb";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID = "questionniareParticipantId";

  private static final String QUESTIONNAIRE_PARTICIPANT_ID_2 = "questionniareParticipantId2";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

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

  private static final QuestionnaireParticipant QUESTIONNAIRE_PARTICIPANT =
    QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID)
      .questionnaire(QUESTIONNAIRE)
      .member(MEMBER_1)
      .participantType(ParticipantType.APPRAISER)
      .build();

  private static final QuestionnaireParticipant QUESTIONNAIRE_PARTICIPANT_2 =
    QuestionnaireParticipant.builder()
      .id(QUESTIONNAIRE_PARTICIPANT_ID_2)
      .questionnaire(QUESTIONNAIRE)
      .member(MEMBER_2)
      .participantType(ParticipantType.APPRAISEE)
      .build();

  @Test
  public void toPagingParticipantDescriptionResponse() {
    PagingResponse<QuestionnaireParticipantDescriptionResponse> data =
      QuestionnaireParticipantResponseMapper.toPagingParticipantDescriptionResponse(
        new PageImpl<>(Arrays.asList(QUESTIONNAIRE_PARTICIPANT, QUESTIONNAIRE_PARTICIPANT_2),
          PageHelper.toPageable(1, 2), 2
        ),
        URL_PREFIX
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(MEMBER_ID_1);
    assertThat(data.getData().get(0).getParticipantId()).isEqualTo(QUESTIONNAIRE_PARTICIPANT_ID);
    assertThat(data.getData().get(1).getId()).isEqualTo(MEMBER_ID_2);
    assertThat(data.getData().get(1).getParticipantId()).isEqualTo(QUESTIONNAIRE_PARTICIPANT_ID_2);
  }

  @Test
  public void toDataResponseQuestionnaireParticipantResponse() {
    DataResponse<QuestionnaireParticipantResponse> data =
      QuestionnaireParticipantResponseMapper.toDataResponseQuestionnaireParticipantResponse(
        QUESTIONNAIRE_PARTICIPANT,
        HttpStatus.OK
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getId()).isEqualTo(QUESTIONNAIRE_PARTICIPANT_ID);
    assertThat(data.getData().getQuestionnaireId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().getMemberId()).isEqualTo(MEMBER_ID_1);
    assertThat(data.getData().getParticipantType()).isEqualTo(ParticipantType.APPRAISER.toString());

  }
}
