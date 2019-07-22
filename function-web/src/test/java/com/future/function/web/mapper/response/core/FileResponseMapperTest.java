package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.entity.feature.core.embedded.Version;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.response.core.embedded.AuthorWebResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.DataPageResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import com.future.function.web.model.response.feature.core.embedded.FileContentWebResponse;
import com.future.function.web.model.response.feature.core.embedded.VersionWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FileResponseMapperTest {
  
  private static final String FILE_ID = "file-id";
  
  private static final String FOLDER_ID = "folder-id";
  
  private static final String NAME = "name";
  
  private static final String FILE_PATH = "file-path";
  
  private static final String FILE_URL = "file-url";
  
  private static final String THUMBNAIL_PATH = "thumbnail-path";
  
  private static final String THUMBNAIL_URL = "thumbnail-url";
  
  private static final String PARENT_ID = "parent-id";
  
  private static final Map<Long, Version> VERSIONS = Collections.singletonMap(
    1L, new Version(0L, FILE_PATH, FILE_URL));
  
  private static final User USER = User.builder()
    .id("user-id")
    .name("name")
    .build();
  
  private static final List<String> PATHS = Collections.singletonList(
    PARENT_ID);
  
  private static final FileV2 FILE = FileV2.builder()
    .id(FILE_ID)
    .name(NAME)
    .filePath(FILE_PATH)
    .fileUrl(FILE_URL)
    .thumbnailPath(THUMBNAIL_PATH)
    .thumbnailUrl(THUMBNAIL_URL)
    .markFolder(false)
    .versions(VERSIONS)
    .parentId(PARENT_ID)
    .paths(PATHS)
    .user(USER)
    .build();
  
  private static final FileV2 FOLDER = FileV2.builder()
    .id(FOLDER_ID)
    .name(NAME)
    .markFolder(true)
    .parentId(PARENT_ID)
    .paths(PATHS)
    .build();
  
  private static final PageRequest PAGEABLE = new PageRequest(0, 5);
  
  private static final PageImpl<FileV2> PAGE = new PageImpl<>(
    Collections.singletonList(FILE), PAGEABLE, 1);
  
  private static final FileContentWebResponse FOLDER_CONTENT_WEB_RESPONSE =
    FileContentWebResponse.builder()
      .id(FOLDER_ID)
      .type("FOLDER")
      .name(NAME)
      .parentId(PARENT_ID)
      .versions(Collections.emptyMap())
      .build();
  
  private static final FileWebResponse<FileContentWebResponse>
    FOLDER_WEB_RESPONSE =
    FileWebResponse.<FileContentWebResponse>builder().paths(
      Collections.singletonList(PARENT_ID))
      .content(FOLDER_CONTENT_WEB_RESPONSE)
      .build();
  
  private static final DataResponse<FileWebResponse<FileContentWebResponse>>
    FOLDER_DATA_RESPONSE =
    DataResponse.<FileWebResponse<FileContentWebResponse>>builder().code(200)
      .status("OK")
      .data(FOLDER_WEB_RESPONSE)
      .build();
  
  private static final FileContentWebResponse FILE_CONTENT_WEB_RESPONSE =
    FileContentWebResponse.builder()
      .id(FILE_ID)
      .file(THUMBNAIL_URL)
      .type("FILE")
      .name(NAME)
      .parentId(PARENT_ID)
      .versions(
        Collections.singletonMap(1L, new VersionWebResponse(0L, FILE_URL)))
      .author(AuthorWebResponseMapper.buildAuthorWebResponse(USER))
      .build();
  
  private static final FileWebResponse<List<FileContentWebResponse>>
    FILE_DATA_RESPONSE =
    FileWebResponse.<List<FileContentWebResponse>>builder().paths(PATHS)
      .content(Collections.singletonList(FILE_CONTENT_WEB_RESPONSE))
      .build();
  
  private static final DataPageResponse<FileWebResponse<List<FileContentWebResponse>>>
    FILE_DATA_PAGE_RESPONSE =
    DataPageResponse.<FileWebResponse<List<FileContentWebResponse>>>builder().code(
      200)
      .status("OK")
      .data(FILE_DATA_RESPONSE)
      .paging(PageHelper.toPaging(PAGE))
      .build();
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenFileOrFolderByMappingToDataResponseReturnDataResponse() {
    
    DataResponse<FileWebResponse<FileContentWebResponse>> dataResponse =
      FileResponseMapper.toSingleFileDataResponse(FOLDER);
    
    assertThat(dataResponse).isNotNull();
    assertThat(dataResponse).isEqualTo(FOLDER_DATA_RESPONSE);
  }
  
  @Test
  public void testGivenPairOfListOfStringAndPageByMappingToDataPageResponseReturnDataResponse() {
    
    DataPageResponse<FileWebResponse<List<FileContentWebResponse>>>
      dataPageResponse = FileResponseMapper.toMultipleFileDataResponse(
      Pair.of(PATHS, PAGE));
    
    assertThat(dataPageResponse).isNotNull();
    assertThat(dataPageResponse).isEqualTo(FILE_DATA_PAGE_RESPONSE);
  }
  
}