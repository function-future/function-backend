package com.future.function.service.impl.feature.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.communication.questionnaire.UserQuestionnaireSummaryRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionnaireResultServiceImplTest {

  private static final Pageable PAGEABLE = new PageRequest(0, 10);

  private static final String USER_QUESTIONNAIRE_SUMMARY_ID_1 =
    "userQuestionnaireSummaryId1";

  private static final String BATCH_ID = "batch_id";

  private static final String BATCH_CODE = "batch_code";

  private static final String KEYWORD = "keyword";

  private UserQuestionnaireSummary userQuestionnaireSummary1;

  private Batch batch;

  @Mock
  private UserQuestionnaireSummaryRepository userQuestionnaireSummaryRepository;

  @InjectMocks
  private QuestionnaireResultServiceImpl questionnaireResultService;

  @Before
  public void setUp() {

    batch = Batch.builder()
      .id(BATCH_ID)
      .code(BATCH_CODE)
      .build();

    userQuestionnaireSummary1 = UserQuestionnaireSummary.builder()
      .id(USER_QUESTIONNAIRE_SUMMARY_ID_1)
      .batch(batch)
      .build();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(userQuestionnaireSummaryRepository);
  }

  @Test
  public void getAppraisalsQuestionnaireSummaryByBatch() {

    when(
      userQuestionnaireSummaryRepository.findAllByRoleOrRoleAndBatchAndDeletedFalse(
        Role.MENTOR, Role.STUDENT, batch, PAGEABLE)).thenReturn(
      new PageImpl<>(Collections.singletonList(userQuestionnaireSummary1),
                     PAGEABLE, 1
      ));

    Page<UserQuestionnaireSummary> userQuestionnaireSummaries =
      questionnaireResultService.getAppraisalsQuestionnaireSummaryByBatch(
        batch, PAGEABLE);

    assertThat(userQuestionnaireSummaries.getTotalElements()).isEqualTo(1);
    assertThat(userQuestionnaireSummaries.getContent()
                 .get(0)
                 .getId()).isEqualTo(USER_QUESTIONNAIRE_SUMMARY_ID_1);


    verify(
      userQuestionnaireSummaryRepository).findAllByRoleOrRoleAndBatchAndDeletedFalse(
      Role.MENTOR, Role.STUDENT, batch, PAGEABLE);
  }

  @Test
  public void getAppraisalsQuestionnaireSummary() {

    when(
      userQuestionnaireSummaryRepository.findAllByUserName(KEYWORD)).thenReturn(
      Arrays.asList(userQuestionnaireSummary1));

    List<UserQuestionnaireSummary> userQuestionnaireSummaries =
      questionnaireResultService.getAppraisalsQuestionnaireSummary(
        batch, KEYWORD, PAGEABLE);

    assertThat(userQuestionnaireSummaries.size()).isEqualTo(1);
    assertThat(userQuestionnaireSummaries.get(0)
                 .getId()).isEqualTo(USER_QUESTIONNAIRE_SUMMARY_ID_1);

    verify(userQuestionnaireSummaryRepository).findAllByUserName(KEYWORD);

  }

  @Test
  public void getAppraisalsQuestionnaireSummaryById() {

    when(userQuestionnaireSummaryRepository.findOne(
      USER_QUESTIONNAIRE_SUMMARY_ID_1)).thenReturn(userQuestionnaireSummary1);

    UserQuestionnaireSummary userQuestionnaireSummary =
      questionnaireResultService.getAppraisalsQuestionnaireSummaryById(
        USER_QUESTIONNAIRE_SUMMARY_ID_1);

    assertThat(userQuestionnaireSummary.getId()).isEqualTo(
      USER_QUESTIONNAIRE_SUMMARY_ID_1);

    verify(userQuestionnaireSummaryRepository).findOne(
      USER_QUESTIONNAIRE_SUMMARY_ID_1);
  }

}
