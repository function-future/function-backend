package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Answer;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireSimpleSummaryResponse;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionnaireResponseMapperTest {

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTIONNAIRE_RESPONSE_SUMMARY_ID = "questionnaireResponseSummaryId1";

  private static final Answer SCORE = Answer.builder()
    .minimum(0)
    .maximum(6)
    .average(3)
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
      .scoreSummary(SCORE)
      .build();

  @Test
  public void toPagingQuestionnaireDetailResponse() {
    PagingResponse<QuestionnaireDetailResponse> data = QuestionnaireResponseMapper.toPagingQuestionnaireDetailResponse(
      new PageImpl<>(Arrays.asList(QUESTIONNAIRE),
      PageHelper.toPageable(1, 1), 1)
    );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().get(0).getTitle()).isEqualTo(QUESTIONNAIRE_TITLE);
    assertThat(data.getData().get(0).getDescription()).isEqualTo(QUESTIONNAIRE_DESCRIPTION);
    assertThat(data.getData().get(0).getStartDate()).isEqualTo(START_DATE);
    assertThat(data.getData().get(0).getDueDate()).isEqualTo(DUE_DATE);
  }

  @Test
  public void toDataResponseQuestionnaireDetailResponse() {
    DataResponse<QuestionnaireDetailResponse> data = QuestionnaireResponseMapper.toDataResponseQuestionnaireDetailResponse(
      QUESTIONNAIRE, HttpStatus.OK);

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().getTitle()).isEqualTo(QUESTIONNAIRE_TITLE);
    assertThat(data.getData().getDescription()).isEqualTo(QUESTIONNAIRE_DESCRIPTION);
    assertThat(data.getData().getStartDate()).isEqualTo(START_DATE);
    assertThat(data.getData().getDueDate()).isEqualTo(DUE_DATE);
  }

  @Test
  public void toPagingQuestionnaireSimpleSummaryResponse() {
    PagingResponse<QuestionnaireSimpleSummaryResponse> data =
      QuestionnaireResponseMapper.toPagingQuestionnaireSimpleSummaryResponse(
        new PageImpl<>(Arrays.asList(QUESTIONNAIRE_RESPONSE_SUMMARY),
          PageHelper.toPageable(1, 1), 1),
        HttpStatus.OK
      );

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(QUESTIONNAIRE_RESPONSE_SUMMARY_ID);
    assertThat(data.getData().get(0).getTitle()).isEqualTo(QUESTIONNAIRE_TITLE);
    assertThat(data.getData().get(0).getDescription()).isEqualTo(QUESTIONNAIRE_DESCRIPTION);
    assertThat(data.getData().get(0).getStartDate()).isEqualTo(START_DATE);
    assertThat(data.getData().get(0).getDueDate()).isEqualTo(DUE_DATE);
    assertThat(data.getData().get(0).getScore()).isEqualTo(SCORE.getAverage());


  }
}
