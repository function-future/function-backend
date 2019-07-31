package com.future.function.web.mapper.request.core;

import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.FileWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileRequestMapperTest {
  
  @Mock
  private WebRequestMapper requestMapper;
  
  @Mock
  private RequestValidator validator;
  
  @InjectMocks
  private FileRequestMapper fileRequestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(requestMapper, validator);
  }
  
  @Test
  public void testGivenJsonAndByteArrayByParsingToFileWebRequestClassReturnFileWebRequestObject() {
    
    String json = "json";
    String name = "name";
    String type = "type";
    byte[] bytes = new byte[] {};
    
    FileWebRequest returnedRequest = FileWebRequest.builder()
      .name(name)
      .type(type)
      .bytes(bytes)
      .build();
    
    when(
      requestMapper.toWebRequestObject(json, FileWebRequest.class)).thenReturn(
      returnedRequest);
    when(validator.validate(returnedRequest)).thenReturn(returnedRequest);
    
    FileWebRequest expectedRequest = FileWebRequest.builder()
      .name(name)
      .type(type)
      .bytes(bytes)
      .build();
    
    assertThat(fileRequestMapper.toFileWebRequest(json, bytes)).isEqualTo(
      expectedRequest);
    
    verify(requestMapper).toWebRequestObject(json, FileWebRequest.class);
    verify(validator).validate(returnedRequest);
  }
  
  @Test
  public void testGivenIdAndJsonAndByteArrayByParsingToFileWebRequestClassReturnFileWebRequestObject() {
    
    String id = "id";
    String json = "json";
    String name = "name";
    String type = "type";
    byte[] bytes = new byte[] {};
    
    FileWebRequest returnedRequest = FileWebRequest.builder()
      .id(id)
      .name(name)
      .type(type)
      .bytes(bytes)
      .build();
    
    when(
      requestMapper.toWebRequestObject(json, FileWebRequest.class)).thenReturn(
      returnedRequest);
    when(validator.validate(returnedRequest)).thenReturn(returnedRequest);
    
    FileWebRequest expectedRequest = FileWebRequest.builder()
      .id(id)
      .name(name)
      .type(type)
      .bytes(bytes)
      .build();
    
    assertThat(fileRequestMapper.toFileWebRequest(id, json, bytes)).isEqualTo(
      expectedRequest);
    
    verify(requestMapper).toWebRequestObject(json, FileWebRequest.class);
    verify(validator).validate(returnedRequest);
  }
  
}
