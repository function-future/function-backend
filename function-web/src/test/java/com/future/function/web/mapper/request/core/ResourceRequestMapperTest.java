package com.future.function.web.mapper.request.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResourceRequestMapperTest {
  
  private static final String FILE_NAME_TXT = "sample.txt";
  
  private static final String FILE_NAME_NO_EXTENSION = "sample";
  
  @Mock
  private HttpServletRequest servletRequest;
  
  @Mock
  private ServletContext servletContext;
  
  @InjectMocks
  private ResourceRequestMapper resourceRequestMapper;
  
  @Before
  public void setUp() {
    
    when(servletRequest.getServletContext()).thenReturn(servletContext);
  }
  
  @After
  public void tearDown() {
    
    verify(servletRequest).getServletContext();
    
    verifyNoMoreInteractions(servletRequest, servletContext);
  }
  
  @Test
  public void testGivenFileNameTxtAndHttpServletRequestByGettingMediaTypeReturnMediaTypeTextPlain() {
    
    when(servletContext.getMimeType(FILE_NAME_TXT)).thenReturn(
      MediaType.TEXT_PLAIN_VALUE);
    
    MediaType mediaType = resourceRequestMapper.getMediaType(
      FILE_NAME_TXT, servletRequest);
    
    assertThat(mediaType).isNotNull();
    assertThat(mediaType).isEqualTo(MediaType.TEXT_PLAIN);
    
    verify(servletContext).getMimeType(FILE_NAME_TXT);
  }
  
  @Test
  public void testGivenFileNameNoExtensionAndHttpServletRequestByGettingMediaTypeReturnMediaTypeOctetStream() {
    
    when(servletContext.getMimeType(FILE_NAME_NO_EXTENSION)).thenReturn(null);
    
    MediaType mediaType = resourceRequestMapper.getMediaType(
      FILE_NAME_NO_EXTENSION, servletRequest);
    
    assertThat(mediaType).isNotNull();
    assertThat(mediaType).isEqualTo(MediaType.APPLICATION_OCTET_STREAM);
    
    verify(servletContext).getMimeType(FILE_NAME_NO_EXTENSION);
  }
  
}