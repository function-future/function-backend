package com.future.function.web.mapper.request.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.questionnaire.QuestionQuestionnaireRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionQuestionnaireRequestMapperTest {

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final String QUESTION_QUESTIONNAIRE_ID = "quesitonQuestionnaireId1";

  private static final String QUESTION_QUESTIONNAIRE_DESCRIPTION = "questionQuestionnaireDescription";

  private static final Questionnaire QUESTIONNAIRE = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID_1)
    .title(QUESTIONNAIRE_TITLE)
    .description(QUESTIONNAIRE_DESCRIPTION)
    .startDate(START_DATE)
    .dueDate(DUE_DATE)
    .build();

  private static final QuestionQuestionnaireRequest QUESTION_QUESTIONNAIRE_REQUEST =
    QuestionQuestionnaireRequest.builder()
      .description(QUESTION_QUESTIONNAIRE_DESCRIPTION)
      .build();

  @Mock
  private RequestValidator validator;

  @InjectMocks
  private QuestionQuestionnaireRequestMapper questionQuestionnaireRequestMapper;

  @After
  public void tearDown() {
    verifyNoMoreInteractions(validator);
  }

  @Test
  public void toQuestionQuestionnaire() {
    when(validator.validate(QUESTION_QUESTIONNAIRE_REQUEST)).thenReturn(QUESTION_QUESTIONNAIRE_REQUEST);

    QuestionQuestionnaire data =
      questionQuestionnaireRequestMapper.toQuestionQuestionnaire(
        QUESTION_QUESTIONNAIRE_REQUEST,
        null,
        QUESTIONNAIRE
      );

    assertThat(data.getQuestionnaire().getId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getDescription()).isEqualTo(QUESTION_QUESTIONNAIRE_DESCRIPTION);

    data =
      questionQuestionnaireRequestMapper.toQuestionQuestionnaire(
        QUESTION_QUESTIONNAIRE_REQUEST,
        QUESTION_QUESTIONNAIRE_ID,
        QUESTIONNAIRE
      );

    assertThat(data.getId()).isEqualTo(QUESTION_QUESTIONNAIRE_ID);

    verify(validator, times(2)).validate(QUESTION_QUESTIONNAIRE_REQUEST);

  }
}
