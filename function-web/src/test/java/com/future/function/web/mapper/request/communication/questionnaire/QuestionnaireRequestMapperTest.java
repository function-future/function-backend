package com.future.function.web.mapper.request.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.communication.questionnaire.QuestionnaireRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: ricky.kennedy
 * Created At: 3:21 PM 7/25/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireRequestMapperTest {

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String REQUEST_TITLE = "requestTitle";

  private static final String REQUEST_DESCRIPTION = "requestDesc";

  private static final Long REQUEST_START_DATE = Long.valueOf(0);

  private static final Long REQUEST_DUE_DATE = Long.valueOf(1);

  private static final QuestionnaireRequest QUESTIONNAIRE_REQUEST =
    QuestionnaireRequest.builder()
      .title(REQUEST_TITLE)
      .desc(REQUEST_DESCRIPTION)
      .startDate(REQUEST_START_DATE)
      .dueDate(REQUEST_DUE_DATE)
      .build();

  @Mock
  private RequestValidator validator;

  @InjectMocks
  private QuestionnaireRequestMapper questionnaireRequestMapper;

  @Test
  public void toQuestionnaire() {
    when(validator.validate(QUESTIONNAIRE_REQUEST)).thenReturn(QUESTIONNAIRE_REQUEST);

    Questionnaire data =
      questionnaireRequestMapper.toQuestionnaire(QUESTIONNAIRE_REQUEST, QUESTIONNAIRE_ID_1);

    assertThat(data.getId()).isEqualTo(QUESTIONNAIRE_ID_1);
    assertThat(data.getTitle()).isEqualTo(REQUEST_TITLE);
    assertThat(data.getDescription()).isEqualTo(REQUEST_DESCRIPTION);
    assertThat(data.getStartDate()).isEqualTo(REQUEST_START_DATE);
    assertThat(data.getDueDate()).isEqualTo(REQUEST_DUE_DATE);


    verify(validator).validate(QUESTIONNAIRE_REQUEST);
  }
}
