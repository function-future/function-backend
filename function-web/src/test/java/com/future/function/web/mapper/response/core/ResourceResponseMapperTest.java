package com.future.function.web.mapper.response.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import com.future.function.web.model.util.constant.FieldName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceResponseMapperTest {
  
  private static final String ID = "id";
  
  private static final String NAME = "name";
  
  private static final String FILE_URL = "file-url";
  
  private static final String THUMBNAIL_URL = "thumbnail-url";
  
  private static final FileV2 FILE_V2 = FileV2.builder()
    .id(ID)
    .name(NAME)
    .fileUrl(FILE_URL)
    .thumbnailUrl(THUMBNAIL_URL)
    .build();
  
  private static final FileV2 FILE_V2_NULL_THUMBNAIL = FileV2.builder()
    .id(ID)
    .name(NAME)
    .fileUrl(FILE_URL)
    .thumbnailUrl(null)
    .build();
  
  private static final DataResponse<FileContentWebResponse>
    CREATED_DATA_RESPONSE_NULL_THUMBNAIL = ResponseHelper.toDataResponse(
    HttpStatus.CREATED, FileContentWebResponse.builder()
      .id(ID)
      .name(NAME)
      .file(Collections.singletonMap(FieldName.FULL, FILE_URL))
      .build());
  
  private DataResponse<FileContentWebResponse> createdDataResponse =
    ResponseHelper.toDataResponse(HttpStatus.CREATED, FileContentWebResponse.builder()
      .id(ID)
      .name(NAME)
      .file(Collections.singletonMap(FieldName.FULL, FILE_URL))
      .build());
  
  @Before
  public void setUp() {
    
    JacksonTester.initFields(this, new ObjectMapper());
    
    Map<String, String> fileObject = new HashMap<>();
    fileObject.put(FieldName.FULL, FILE_URL);
    fileObject.put(FieldName.THUMBNAIL, THUMBNAIL_URL);
    
    createdDataResponse.getData()
      .setFile(fileObject);
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenFileV2ObjectByConvertingToDataResponseReturnDataResponseObject() {
    
    assertThat(
      ResourceResponseMapper.toResourceDataResponse(FILE_V2)).isEqualTo(
      createdDataResponse);
    
    assertThat(ResourceResponseMapper.toResourceDataResponse(
      FILE_V2_NULL_THUMBNAIL)).isEqualTo(CREATED_DATA_RESPONSE_NULL_THUMBNAIL);
    
  }
  
}
