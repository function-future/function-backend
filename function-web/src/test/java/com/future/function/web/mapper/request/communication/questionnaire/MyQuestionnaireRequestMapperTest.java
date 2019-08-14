package com.future.function.web.mapper.request.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.web.model.request.communication.questionnaire.QuestionResponseRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MyQuestionnaireRequestMapperTest {

  private static final String THUMBNAIL_URL = "thumbnail";

  private static final String MEMBER_ID_1 = "memberId1";

  private static final String MEMBER_ID_2 = "memberId2";

  private static final String MEMBER_NAME_1 = "member1";

  private static final String MEMBER_NAME_2 = "member1";

  private static final String BATCH_ID = "batchId";

  private static final String UNIVERSITY = "itb";

  private static final String QUESTION_ID = "questionId";

  private static final String COMMENT = "comment";

  private static final Float AVG = Float.valueOf(3);

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

  private static final QuestionQuestionnaire QUESTION =
    QuestionQuestionnaire.builder()
      .id(QUESTION_ID)
      .build();

  private static final QuestionResponseRequest QUESTION_RESPONSE_REQUEST =
    QuestionResponseRequest.builder()
      .idQuestion(QUESTION_ID)
      .comment(COMMENT)
      .score(AVG)
      .build();

  @Mock
  private QuestionnaireService questionnaireService;

  @InjectMocks
  private MyQuestionnaireRequestMapper myQuestionnaireRequestMapper;

  @Test
  public void toListQuestionResponse() {
    when(questionnaireService.getQuestionQuestionnaire(QUESTION_ID))
      .thenReturn(QUESTION);

    List<QuestionResponse> data = myQuestionnaireRequestMapper.toListQuestionResponse(
      Arrays.asList(QUESTION_RESPONSE_REQUEST),
      MEMBER_1,
      MEMBER_2
    );

    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAppraiser().getId()).isEqualTo(MEMBER_ID_1);
    assertThat(data.get(0).getAppraisee().getId()).isEqualTo(MEMBER_ID_2);
    assertThat(data.get(0).getQuestion().getId()).isEqualTo(QUESTION_ID);

    verify(questionnaireService).getQuestionQuestionnaire(QUESTION_ID);
  }
}
