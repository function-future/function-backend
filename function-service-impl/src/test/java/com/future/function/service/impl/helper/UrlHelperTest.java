package com.future.function.service.impl.helper;

import com.future.function.common.properties.core.FileProperties;
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
    
    FileProperties fileProperties = new FileProperties();
    fileProperties.setUrlPrefix("url-prefix");
    
    String fileUrl = "/file-url";
    
    String expectedFileUrl = "url-prefix/file-url";
    
    assertThat(UrlHelper.toFileUrl(fileProperties, fileUrl)).isEqualTo(
      expectedFileUrl);
  }
  
}