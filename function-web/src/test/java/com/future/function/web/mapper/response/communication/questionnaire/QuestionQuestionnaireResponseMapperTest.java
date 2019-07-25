package com.future.function.web.mapper.response.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionQuestionnaireResponse;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Author: ricky.kennedy
 * Created At: 1:57 PM 7/25/2019
 */
public class QuestionQuestionnaireResponseMapperTest {

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTION_ID = "questionId";

  private static final String QUESTION_DESCRIPTION = "questionDescription";

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

  @Test
  public void toDataResponseListQuestionQuestionnaireResponse() {
    DataResponse<List<QuestionQuestionnaireResponse>> data = QuestionQuestionnaireResponseMapper
      .toDataResponseListQuestionQuestionnaireResponse(Arrays.asList(QUESTION_QUESTIONNAIRE), HttpStatus.OK);

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().get(0).getId()).isEqualTo(QUESTION_ID);
    assertThat(data.getData().get(0).getQuestionnaireId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().get(0).getDescription()).isEqualTo(QUESTION_DESCRIPTION);
  }

  @Test
  public void toDataResponseQuestionQuestionnaireResponse() {
    DataResponse<QuestionQuestionnaireResponse> data = QuestionQuestionnaireResponseMapper
      .toDataResponseQuestionQuestionnaireResponse(QUESTION_QUESTIONNAIRE, HttpStatus.OK);

    assertThat(data).isNotNull();
    assertThat(data.getCode()).isEqualTo(200);
    assertThat(data.getData().getId()).isEqualTo(QUESTION_ID);
    assertThat(data.getData().getQuestionnaireId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getData().getDescription()).isEqualTo(QUESTION_DESCRIPTION);
  }
}
