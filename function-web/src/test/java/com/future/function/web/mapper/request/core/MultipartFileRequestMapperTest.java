package com.future.function.web.mapper.request.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MultipartFileRequestMapper.class, MultipartFile.class })
public class MultipartFileRequestMapperTest {
  
  @InjectMocks
  private MultipartFileRequestMapper multipartFileRequestMapper;
  
  private MultipartFile multipartFile;
  
  @Before
  public void setUp() {
    
    mockStatic(MultipartFile.class);
    multipartFile = mock(MultipartFile.class);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(multipartFile);
  }
  
  @Test
  public void testGivenMultipartFileByParsingToByteArrayReturnByteArray()
    throws Exception {
    
    byte[] data = "Hello".getBytes();
    when(multipartFile.getBytes()).thenReturn(data);
    
    byte[] bytes = multipartFileRequestMapper.toByteArray(multipartFile);
    
    assertThat(bytes).isNotEmpty();
    assertThat(bytes).isEqualTo(data);
    
    verify(multipartFile).getBytes();
  }
  
  @Test
  public void testGivenMultipartFileByParsingToByteArrayReturnEmptyByteArray()
    throws Exception {
    
    when(multipartFile.getBytes()).thenThrow(new IOException());
    
    byte[] bytes = multipartFileRequestMapper.toByteArray(multipartFile);
    
    assertThat(bytes).isEmpty();
    
    verify(multipartFile).getBytes();
  }
  
  @Test
  public void testGivenMultipartFileByParsingToPairOfStringAndByteArrayReturnPairObject()
    throws IOException {
    
    String name = "name.txt";
    when(multipartFile.getOriginalFilename()).thenReturn(name);
    
    byte[] data = "Hello".getBytes();
    when(multipartFile.getBytes()).thenReturn(data);
    
    Pair<String, byte[]> expectedPair = Pair.of(name, data);
    
    Pair<String, byte[]> pair =
      multipartFileRequestMapper.toStringAndByteArrayPair(multipartFile);
    
    assertThat(pair).isEqualTo(expectedPair);
    
    verify(multipartFile).getOriginalFilename();
    verify(multipartFile).getBytes();
  }
  
}