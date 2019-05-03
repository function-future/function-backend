package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.ErrorResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseHelperTest {
  
  private static final BaseResponse BASE_RESPONSE = new BaseResponse(200, "OK");
  
  private static final ErrorResponse ERROR_RESPONSE = ErrorResponse.builder()
    .code(400)
    .status("BAD_REQUEST")
    .errors(Collections.emptyMap())
    .build();
  
  private static final String SINGLE_DATA = "data";
  
  private static final DataResponse<String> DATA_RESPONSE =
    DataResponse.<String>builder().code(200)
      .status("OK")
      .data(SINGLE_DATA)
      .build();
  
  private static final List<String> MULTIPLE_DATA = Arrays.asList(
    "string-1", "string-2", "string-3");
  
  private static final Paging PAGING = Paging.builder()
    .page(0)
    .size(3)
    .totalRecords(3)
    .build();
  
  private static final PagingResponse<String> PAGING_RESPONSE =
    PagingResponse.<String>builder().code(200)
      .status("OK")
      .data(MULTIPLE_DATA)
      .paging(PAGING)
      .build();
  
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenHttpStatusByCreatingBaseResponseReturnBaseResponseObject() {
    
    BaseResponse baseResponse = ResponseHelper.toBaseResponse(HttpStatus.OK);
    
    assertThat(baseResponse).isNotNull();
    assertThat(baseResponse).isEqualTo(BASE_RESPONSE);
  }
  
  @Test
  public void testGivenStringByFormattingToProperStatusFormatReturnProperStatusFormat() {
    
    String okResponseStatus = ResponseHelper.toProperStatusFormat(
      HttpStatus.OK.getReasonPhrase());
    String badRequestResponseStatus = ResponseHelper.toProperStatusFormat(
      HttpStatus.BAD_REQUEST.getReasonPhrase());
    
    assertThat(okResponseStatus).isNotBlank();
    assertThat(okResponseStatus).isEqualTo("OK");
    assertThat(badRequestResponseStatus).isNotBlank();
    assertThat(badRequestResponseStatus).isEqualTo("BAD_REQUEST");
  }
  
  @Test
  public void testGivenHttpStatusAndConstraintViolationsByCreatingErrorResponseReturnErrorResponseObject() {
    
    ErrorResponse errorResponse = ResponseHelper.toErrorResponse(
      HttpStatus.BAD_REQUEST, Collections.emptySet());
    
    assertThat(errorResponse).isNotNull();
    assertThat(errorResponse).isEqualTo(ERROR_RESPONSE);
  }
  
  @Test
  public void testGivenHttpStatusAndDataByCreatingDataResponseReturnDataResponseObject() {
    
    DataResponse<String> dataResponse = ResponseHelper.toDataResponse(
      HttpStatus.OK, SINGLE_DATA);
    
    assertThat(dataResponse).isNotNull();
    assertThat(dataResponse).isEqualTo(DATA_RESPONSE);
  }
  
  @Test
  public void testGivenHttpStatusAndDataAndPagingByCreatingPagingResponseReturnPagingResponseObject() {
    
    PagingResponse<String> pagingResponse = ResponseHelper.toPagingResponse(
      HttpStatus.OK, MULTIPLE_DATA, PAGING);
    
    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(PAGING_RESPONSE);
  }
  
}
