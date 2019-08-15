package com.future.function.web.controller.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.service.api.feature.scoring.QuestionBankService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.scoring.QuestionBankRequestMapper;
import com.future.function.web.mapper.response.scoring.QuestionBankResponseMapper;
import com.future.function.web.model.request.scoring.QuestionBankWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.scoring.QuestionBankWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(QuestionBankController.class)
public class QuestionBankControllerTest extends TestHelper {

  private static final String QUESTION_BANK_ID = "questionbank-id";

  private static final String QUESTION_BANK_DESCRIPTION =
    "questionbank-description";

  private static final String QUESTION_BANK_CREATE_REQUEST_JSON =
    "{\n" + "\"description\": \"" + QUESTION_BANK_DESCRIPTION + "\"}";

  private static final String QUESTION_BANK_UPDATE_REQUEST_JSON =
    "{\n" + "\"id\": \"" + QUESTION_BANK_ID + "\",\n" + "\"description\": \"" +
    QUESTION_BANK_DESCRIPTION + "\"}";

  private Pageable pageable;

  private QuestionBank questionBank;

  private List<QuestionBank> questionBankList;

  private Page<QuestionBank> questionBankPage;

  private QuestionBankWebRequest request;

  private DataResponse<QuestionBankWebResponse> DATA_RESPONSE;

  private DataResponse<QuestionBankWebResponse> CREATED_DATA_RESPONSE;

  private PagingResponse<QuestionBankWebResponse> PAGING_RESPONSE;

  private BaseResponse BASE_RESPONSE;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private QuestionBankService questionBankService;

  @MockBean
  private QuestionBankRequestMapper requestMapper;

  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.ADMIN);

    questionBank = QuestionBank.builder()
      .id(QUESTION_BANK_ID)
      .description(QUESTION_BANK_DESCRIPTION)
      .build();

    request = QuestionBankWebRequest.builder()
      .description(QUESTION_BANK_DESCRIPTION)
      .build();

    pageable = new PageRequest(0, 10);

    questionBankList = Collections.singletonList(questionBank);

    questionBankPage = new PageImpl<>(questionBankList, pageable, 10);

    DATA_RESPONSE = QuestionBankResponseMapper.toQuestionBankWebResponse(
      questionBank);

    CREATED_DATA_RESPONSE =
      QuestionBankResponseMapper.toQuestionBankWebResponse(
        HttpStatus.CREATED, questionBank);

    PAGING_RESPONSE =
      QuestionBankResponseMapper.toPagingQuestionBankWebResponse(
        questionBankPage);

    BASE_RESPONSE = ResponseHelper.toBaseResponse(HttpStatus.OK);

  }

  @After
  public void tearDown() throws Exception {

    verifyNoMoreInteractions(questionBankService, requestMapper);
  }

  @Test
  public void testFindQuestionBankById() throws Exception {

    when(questionBankService.findById(QUESTION_BANK_ID)).thenReturn(
      questionBank);

    mockMvc.perform(
      get("/api/scoring/question-banks/" + QUESTION_BANK_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));

    verify(questionBankService).findById(QUESTION_BANK_ID);
  }

  @Test
  public void testDeleteQuestionBankById() throws Exception {

    mockMvc.perform(
      delete("/api/scoring/question-banks/" + QUESTION_BANK_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(baseResponseJacksonTester.write(BASE_RESPONSE)
                                  .getJson()));

    verify(questionBankService).deleteById(QUESTION_BANK_ID);
  }

  @Test
  public void testFindQuestionBankWithPagingParameters() throws Exception {

    when(questionBankService.findAllByPageable(pageable)).thenReturn(
      questionBankPage);

    mockMvc.perform(get("/api/scoring/question-banks").cookie(cookies)
                      .param("page", "1")
                      .param("size", "10"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));

    verify(questionBankService).findAllByPageable(pageable);
  }

  @Test
  public void testFindQuestionBankWithNoPagingParameters() throws Exception {

    when(questionBankService.findAllByPageable(pageable)).thenReturn(
      questionBankPage);

    mockMvc.perform(get("/api/scoring/question-banks").cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(PAGING_RESPONSE)
          .getJson()));

    verify(questionBankService).findAllByPageable(pageable);
  }

  @Test
  public void testCreateQuestionBank() throws Exception {

    when(questionBankService.createQuestionBank(questionBank)).thenReturn(
      questionBank);
    when(requestMapper.toQuestionBank(request)).thenReturn(questionBank);

    mockMvc.perform(post("/api/scoring/question-banks").cookie(cookies)
                      .content(QUESTION_BANK_CREATE_REQUEST_JSON)
                      .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));

    verify(questionBankService).createQuestionBank(questionBank);
    verify(requestMapper).toQuestionBank(request);
  }

  @Test
  public void testUpdateQuestionBank() throws Exception {

    when(questionBankService.updateQuestionBank(questionBank)).thenReturn(
      questionBank);
    when(requestMapper.toQuestionBank(QUESTION_BANK_ID, request)).thenReturn(
      questionBank);

    mockMvc.perform(put(
      "/api/scoring/question-banks/" + QUESTION_BANK_ID).cookie(cookies)
                      .content(QUESTION_BANK_UPDATE_REQUEST_JSON)
                      .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(status().isOk())
      .andExpect(content().json(dataResponseJacksonTester.write(DATA_RESPONSE)
                                  .getJson()));

    verify(questionBankService).updateQuestionBank(questionBank);
    verify(requestMapper).toQuestionBank(QUESTION_BANK_ID, request);
  }

}
