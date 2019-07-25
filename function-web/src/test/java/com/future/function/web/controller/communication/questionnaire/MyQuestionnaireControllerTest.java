package com.future.function.web.controller.communication.questionnaire;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.communication.questionnaire.Questionnaire;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.service.api.feature.communication.questionnaire.MyQuestionnaireService;
import com.future.function.service.api.feature.communication.questionnaire.QuestionnaireService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.request.communication.questionnaire.MyQuestionnaireRequestMapper;
import com.future.function.web.mapper.response.communication.questionnaire.QuestionnaireResponseMapper;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.communication.questionnaire.QuestionnaireDetailResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Author: ricky.kennedy
 * Created At: 4:12 PM 7/25/2019
 */
@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(MyQuestionnaireController.class)
public class MyQuestionnaireControllerTest extends TestHelper {

  private static final String USER_ID = "userId";

  private static final User USER = User.builder().id(USER_ID).build();

  private static final Pageable PAGEABLE = PageHelper.toPageable(1, 10);

  private static final String QUESTIONNAIRE_ID_1 = "questionnaireId1";

  private static final String QUESTIONNAIRE_TITLE = "questionnaireTitle";

  private static final String QUESTIONNAIRE_DESCRIPTION = "questionnaireDescription";

  private static final Long START_DATE = Long.valueOf(0);

  private static final Long DUE_DATE = Long.valueOf(1);

  private static final Questionnaire QUESTIONNAIRE = Questionnaire.builder()
    .id(QUESTIONNAIRE_ID_1)
    .title(QUESTIONNAIRE_TITLE)
    .description(QUESTIONNAIRE_DESCRIPTION)
    .startDate(START_DATE)
    .dueDate(DUE_DATE)
    .build();

  private static final PageImpl<Questionnaire> QUESTIONNAIRE_PAGE =
    new PageImpl<>(Collections.singletonList(QUESTIONNAIRE), PAGEABLE, 1);

  @MockBean
  private MyQuestionnaireService myQuestionnaireService;

  @MockBean
  private QuestionnaireService questionnaireService;

  @MockBean
  private UserService userService;

  @MockBean
  private MyQuestionnaireRequestMapper myQuestionnaireRequestMapper;

  @Override
  @Before
  public void setUp() {
    super.setUp();
    super.setCookie(Role.STUDENT);
  }

  @After
  public void tearDown() {
    verifyNoMoreInteractions(
      myQuestionnaireService,
      questionnaireService,
      userService,
      myQuestionnaireRequestMapper);
  }

  @Test
  public void getMyQuestionnaires() throws Exception {
    when(userService.getUser(any(String.class))).thenReturn(USER);
    when(myQuestionnaireService
      .getQuestionnairesByMemberLoginAsAppraiser(USER, PAGEABLE))
      .thenReturn(QUESTIONNAIRE_PAGE);
    PagingResponse<QuestionnaireDetailResponse> response =
      QuestionnaireResponseMapper.toPagingQuestionnaireDetailResponse(QUESTIONNAIRE_PAGE);

    mockMvc.perform(get("/api/communication/my-questionnaires").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(pagingResponseJacksonTester.write(response).getJson()));

    verify(userService).getUser(any(String.class));
    verify(myQuestionnaireService)
      .getQuestionnairesByMemberLoginAsAppraiser(USER, PAGEABLE);
  }

  @Test
  public void getListAprraisees() {
  }

  @Test
  public void getQuestionnaireData() {
  }

  @Test
  public void getQuestion() {
  }

  @Test
  public void addQuestionnaireResponse() {
  }
}
