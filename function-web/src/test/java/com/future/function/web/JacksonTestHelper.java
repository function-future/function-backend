package com.future.function.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import org.junit.Before;
import org.springframework.boot.test.json.JacksonTester;

public class JacksonTestHelper {
  
  protected JacksonTester<BaseResponse> baseResponseJacksonTester;
  
  protected JacksonTester<DataResponse> dataResponseJacksonTester;
  
  protected JacksonTester<PagingResponse> pagingResponseJacksonTester;
  
  @Before
  public void setUp() {
    
    JacksonTester.initFields(this, new ObjectMapper());
  }
  
}
