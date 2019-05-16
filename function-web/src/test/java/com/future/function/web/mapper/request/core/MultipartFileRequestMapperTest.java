package com.future.function.web.mapper.request.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MultipartFileRequestMapperTest {
  
  private static final String NAME = "name";
  
  private static final String ORIGINAL_NAME = NAME + ".txt";
  
  private static final byte[] CONTENT = new byte[0];
  
  @InjectMocks
  private MultipartFileRequestMapper multipartFileRequestMapper;
  
  private MultipartFile multipartFile;
  
  @Before
  public void setUp() {
    
    multipartFile = new MockMultipartFile(NAME, ORIGINAL_NAME,
                                          MediaType.IMAGE_PNG_VALUE, CONTENT
    );
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenMultipartFileByParsingToByteArrayReturnByteArray() {
    
    byte[] bytes = multipartFileRequestMapper.toByteArray(multipartFile);
    
    assertThat(bytes).isEqualTo(CONTENT);
  }
  
  @Test
  public void testGivenMultipartFileByParsingToByteArrayReturnEmptyByteArray() {
    
    byte[] bytes = multipartFileRequestMapper.toByteArray(multipartFile);
    
    assertThat(bytes).isEmpty();
  }
  
  @Test
  public void testGivenMultipartFileByParsingToPairOfStringAndByteArrayReturnPairObject() {
    
    Pair<String, byte[]> expectedPair = Pair.of(ORIGINAL_NAME, CONTENT);
    
    Pair<String, byte[]> pair =
      multipartFileRequestMapper.toStringAndByteArrayPair(multipartFile);
    
    assertThat(pair).isEqualTo(expectedPair);
  }
  
  @Test
  public void testGivenNullByParsingToByteArrayReturnEmptyByteArray() {
    
    byte[] pair = multipartFileRequestMapper.toByteArray(null);
    
    assertThat(pair).isEqualTo(CONTENT);
  }
  
}