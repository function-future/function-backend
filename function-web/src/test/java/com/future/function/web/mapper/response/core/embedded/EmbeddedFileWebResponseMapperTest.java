package com.future.function.web.mapper.response.core.embedded;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.model.response.feature.core.embedded.EmbeddedFileWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedFileWebResponseMapperTest {
  
  private static final String ID = "id";
  
  private static final String FILE_URL = "/file-url";
  
  private static final String THUMBNAIL_URL = "/thumbnail-url";
  
  private static final String URL_PREFIX = "url-prefix";
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void test() {
    
    FileV2 fileV2 = FileV2.builder()
      .id(ID)
      .fileUrl(FILE_URL)
      .thumbnailUrl(THUMBNAIL_URL)
      .build();
    
    EmbeddedFileWebResponse expectedEmbeddedFileWebResponse =
      EmbeddedFileWebResponse.builder()
        .id(ID)
        .file(EmbeddedFileWebResponse.File.builder()
                .full(URL_PREFIX + FILE_URL)
                .thumbnail(URL_PREFIX + THUMBNAIL_URL)
                .build())
        .build();
    
    List<EmbeddedFileWebResponse> embeddedFileWebResponses =
      EmbeddedFileWebResponseMapper.toEmbeddedFileWebResponses(
        Collections.singletonList(fileV2), URL_PREFIX);
    
    assertThat(embeddedFileWebResponses).isEqualTo(
      Collections.singletonList(expectedEmbeddedFileWebResponse));
  }
  
}
