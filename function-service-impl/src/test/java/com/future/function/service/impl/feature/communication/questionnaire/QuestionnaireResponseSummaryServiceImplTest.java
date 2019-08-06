package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.QuestionQuestionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponse;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionResponseSummary;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.communication.questionnaire.QuestionnaireResponseSummary;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.communication.questionnaire.QuestionQuestionnaireRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionResponseSummaryRepository;
import com.future.function.repository.feature.communication.questionnaire.QuestionnaireResponseSummaryRepository;
import com.future.function.service.api.feature.core.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Author: RickyKennedy
 * Created At: 8:31 PM 7/23/2019
 */
@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireResponseSummaryServiceImplTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String USER_ID_1 = "userId1";

  private static final String QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1 = "questionnaireResponseSummaryId1";

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTION_RESPONSE_SUMMARY_ID_1 = "questionResponseSummaryId1";

  private static final String QUESTION_RESPONSE_ID_1 = "questionResponseId1";

  private static final String QUESTION_ID_1 = "questionId1";

  private User user1;

  private QuestionnaireResponseSummary questionnaireResponseSummary1;

  private Questionnaire questionnaire1;

  private QuestionResponseSummary questionResponseSummary1;

  private QuestionResponse questionResponse1;

  private QuestionQuestionnaire question1;

  @Mock
  private QuestionnaireResponseSummaryRepository questionnaireResponseSummaryRepository;

  @Mock
  private QuestionResponseSummaryRepository questionResponseSummaryRepository;

  @Mock
  private QuestionResponseRepository questionResponseRepository;

  @Mock
  private QuestionQuestionnaireRepository questionQuestionnaireRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private QuestionnaireResponseSummaryServiceImpl questionnaireResponseSummarySevice;

  @Before
  public void setUp() {
    user1 = User.builder()
            .id(USER_ID_1)
            .build();

    questionnaire1 = Questionnaire.builder().id(QUESTIONNAIRE_ID_1).build();

    questionnaireResponseSummary1 =
      QuestionnaireResponseSummary.builder()
        .id(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1)
        .questionnaire(questionnaire1)
        .appraisee(user1)
        .build();

    question1 = QuestionQuestionnaire.builder().id(QUESTION_ID_1).build();

    questionResponseSummary1 =
      QuestionResponseSummary.builder()
        .id(QUESTION_RESPONSE_SUMMARY_ID_1)
        .question(question1)
        .appraisee(user1)
        .build();

    questionResponse1 = QuestionResponse.builder().id(QUESTION_RESPONSE_ID_1).build();

  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(
      questionnaireResponseSummaryRepository,
      questionResponseSummaryRepository,
      questionResponseRepository,
      questionQuestionnaireRepository,
      userService);
  }

  @Test
  public void getQuestionnairesSummariesBasedOnAppraisee() {
    when(
      questionnaireResponseSummaryRepository
        .findAllByAppraiseeAndDeletedFalse(user1,PAGEABLE))
      .thenReturn(
        new PageImpl<>
          (Collections.singletonList(questionnaireResponseSummary1),PAGEABLE,1)
      );

    Page<QuestionnaireResponseSummary> questionnaireResonseSummaryPage =
      questionnaireResponseSummarySevice
        .getQuestionnairesSummariesBasedOnAppraisee(user1, PAGEABLE);

    assertThat(questionnaireResonseSummaryPage.getTotalElements()) .isEqualTo(1);
    assertThat(
      questionnaireResonseSummaryPage.getContent().get(0).getId())
      .isEqualTo(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1);

    verify(questionnaireResponseSummaryRepository)
      .findAllByAppraiseeAndDeletedFalse(user1,PAGEABLE);
  }

  @Test
  public void getQuestionnaireResponseSummaryById() {
    when(
      questionnaireResponseSummaryRepository
        .findOne(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1))
      .thenReturn(questionnaireResponseSummary1);

    QuestionnaireResponseSummary questionnaireResponseSummaryResult =
      questionnaireResponseSummarySevice
        .getQuestionnaireResponseSummaryById(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1);

    assertThat(questionnaireResponseSummaryResult.getId())
      .isEqualTo(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1);

    verify(questionnaireResponseSummaryRepository)
      .findOne(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1);
  }

  @Test
  public void getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee() {
    when(questionnaireResponseSummaryRepository.findOne(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1))
      .thenReturn(questionnaireResponseSummary1);
    when(questionResponseSummaryRepository.findAllByQuestionnaireAndAppraiseeAndDeletedFalse(questionnaire1, user1))
      .thenReturn(Arrays.asList(questionResponseSummary1));

    List<QuestionResponseSummary> questionResponseSummaries =
      questionnaireResponseSummarySevice
        .getQuestionsDetailsFromQuestionnaireResponseSummaryIdAndAppraisee(
          QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1, user1);

    assertThat(questionResponseSummaries.size()).isEqualTo(1);
    assertThat(questionResponseSummaries.get(0).getId()).isEqualTo(QUESTION_RESPONSE_SUMMARY_ID_1);

    verify(questionnaireResponseSummaryRepository).findOne(QUESTIONNAIRE_RESPONSE_SUMMARY_ID_1);
    verify(questionResponseSummaryRepository).findAllByQuestionnaireAndAppraiseeAndDeletedFalse(questionnaire1, user1);
  }

  @Test
  public void getQuestionResponseSummaryById() {
    when(questionResponseSummaryRepository.findOne(QUESTION_RESPONSE_SUMMARY_ID_1)).thenReturn(questionResponseSummary1);

    QuestionResponseSummary questionResponseSummaryResult =
      questionnaireResponseSummarySevice.getQuestionResponseSummaryById(QUESTION_RESPONSE_SUMMARY_ID_1);

    assertThat(questionResponseSummaryResult.getId()).isEqualTo(QUESTION_RESPONSE_SUMMARY_ID_1);

    verify(questionResponseSummaryRepository).findOne(QUESTION_RESPONSE_SUMMARY_ID_1);

  }

  @Test
  public void getQuestionResponseByQuestionResponseSummaryId() {
    when(questionResponseSummaryRepository.findOne(QUESTION_RESPONSE_SUMMARY_ID_1)).thenReturn(questionResponseSummary1);
    when(questionResponseRepository.findAllByQuestionQuestionnaireAndAppraiseeAndDeletedFalse(question1, user1))
      .thenReturn(Arrays.asList(questionResponse1));
    when(questionQuestionnaireRepository.findOne(QUESTION_ID_1)).thenReturn(question1);
    when(userService.getUser(USER_ID_1)).thenReturn(user1);

    List<QuestionResponse> questionResponses =
      questionnaireResponseSummarySevice.getQuestionResponseByQuestionResponseSummaryId(QUESTION_RESPONSE_SUMMARY_ID_1);

    assertThat(questionResponses.size()).isEqualTo(1);
    assertThat(questionResponses.get(0).getId()).isEqualTo(QUESTION_RESPONSE_ID_1);


    verify(questionResponseSummaryRepository).findOne(QUESTION_RESPONSE_SUMMARY_ID_1);
    verify(questionResponseRepository).findAllByQuestionQuestionnaireAndAppraiseeAndDeletedFalse(question1, user1);
    verify(questionQuestionnaireRepository).findOne(QUESTION_ID_1);
    verify(userService).getUser(USER_ID_1);
  }
}
