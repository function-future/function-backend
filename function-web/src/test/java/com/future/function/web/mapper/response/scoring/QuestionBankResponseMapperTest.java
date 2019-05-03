package com.future.function.web.mapper.response.scoring;

import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.scoring.QuestionBankWebResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionBankResponseMapperTest {

  private static final String QUESTION_BANK_ID = "questionbank-id";
  private static final String QUESTION_BANK_DESCRIPTION = "questionbank-description";

  private Paging paging;
  private Pageable pageable;
  private QuestionBank questionBank;
  private Page<QuestionBank> questionBankPage;
  private QuestionBankWebResponse questionBankWebResponse;

  private DataResponse<QuestionBankWebResponse> questionBankWebResponseDataResponse;
  private PagingResponse<QuestionBankWebResponse> questionBankWebResponsePagingResponse;

  @Before
  public void setUp() throws Exception {

    questionBank = QuestionBank
            .builder()
            .id(QUESTION_BANK_ID)
            .description(QUESTION_BANK_DESCRIPTION)
            .build();

    questionBankWebResponse = QuestionBankWebResponse
            .builder()
            .id(QUESTION_BANK_ID)
            .description(QUESTION_BANK_DESCRIPTION)
            .build();

    questionBankWebResponseDataResponse = DataResponse
            .<QuestionBankWebResponse>builder()
            .data(questionBankWebResponse)
            .code(HttpStatus.OK.value())
            .status(ResponseHelper.toProperStatusFormat(HttpStatus.OK.getReasonPhrase()))
            .build();

    pageable = new PageRequest(0, 10);

    questionBankPage = new PageImpl<>(Collections.singletonList(questionBank), pageable, 10);

    paging = Paging
            .builder()
            .currentPage(questionBankPage.getNumber())
            .pageSize(questionBankPage.getSize())
            .totalRecords(questionBankPage.getTotalElements())
            .totalPages(questionBankPage.getTotalPages())
            .build();

    questionBankWebResponsePagingResponse = PagingResponse
            .<QuestionBankWebResponse>builder()
            .data(Collections.singletonList(questionBankWebResponse))
            .code(HttpStatus.OK.value())
            .status(ResponseHelper.toProperStatusFormat(HttpStatus.OK.getReasonPhrase()))
            .paging(paging)
            .build();

  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testToQuestionBankWebResponseWithHttpStatusOk() {
    DataResponse<QuestionBankWebResponse> actual = QuestionBankResponseMapper.toQuestionBankWebResponse(questionBank);
    assertThat(actual).isEqualTo(questionBankWebResponseDataResponse);
  }

  @Test
  public void testToQuestionBankResponseWithHttpStatusCreated() {
    DataResponse<QuestionBankWebResponse> actual = QuestionBankResponseMapper.toQuestionBankWebResponse(HttpStatus.CREATED, questionBank);
    assertThat(actual.getData()).isEqualTo(questionBankWebResponseDataResponse.getData());
    assertThat(actual.getCode()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  public void testToPagingQuestionBankResponseWithHttpStatusOk() {
    PagingResponse<QuestionBankWebResponse> actual = QuestionBankResponseMapper.toPagingQuestionBankWebResponse(questionBankPage);
    assertThat(actual.getPaging()).isEqualTo(paging);
    assertThat(actual.getData()).isEqualTo(Collections.singletonList(questionBankWebResponsePagingResponse.getData()));
  }
}