package com.future.function.web.mapper.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlHelperTest {
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenFilePropertiesAndFileUrlByConstructingFileUrlReturnFileUrlString() {
    
    String urlPrefix = "url-prefix";
    String fileUrl = "/file-url";
    
    String expectedFileUrl = "url-prefix/file-url";
    
    assertThat(UrlHelper.toFileUrl(urlPrefix, fileUrl)).isEqualTo(
      expectedFileUrl);
  }
  
}