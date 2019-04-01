package com.future.function.web.mapper.helper;

import com.future.function.web.model.response.base.BaseResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseHelperTest {
  
  private static final BaseResponse BASE_RESPONSE = new BaseResponse(
    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenHttpStatusByCreatingBaseResponseReturnBaseResponseObject() {
    
    BaseResponse baseResponse = ResponseHelper.toBaseResponse(HttpStatus.OK);
    
    assertThat(baseResponse).isNotNull();
    assertThat(baseResponse.getCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(baseResponse.getStatus()).isEqualTo("OK");
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
  
}
