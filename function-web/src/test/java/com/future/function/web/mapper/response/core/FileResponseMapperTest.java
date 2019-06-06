package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.embedded.Version;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import com.future.function.web.model.response.feature.core.embedded.VersionWebResponse;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
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
    .build();
  
  private static final FileV2 FOLDER = FileV2.builder()
    .id(FOLDER_ID)
    .name(NAME)
    .markFolder(true)
    .parentId(PARENT_ID)
    .build();
  
  private static final FileWebResponse FILE_WEB_RESPONSE_FOLDER =
    FileWebResponse.builder()
      .id(FOLDER_ID)
      .type("FOLDER")
      .name(NAME)
      .parentId(PARENT_ID)
      .versions(Collections.emptyMap())
      .build();
  
  private static final FileWebResponse FILE_WEB_RESPONSE_FILE =
    FileWebResponse.builder()
      .id(FILE_ID)
      .file(THUMBNAIL_URL)
      .type("FILE")
      .name(NAME)
      .parentId(PARENT_ID)
      .versions(
        Collections.singletonMap(1L, new VersionWebResponse(0L, FILE_URL)))
      .build();
  
  private static final PageRequest PAGEABLE = new PageRequest(0, 5);
  
  private static final PageImpl<FileV2> PAGE = new PageImpl<>(
    Collections.singletonList(FILE), PAGEABLE, 1);
  
  private static final DataResponse<FileWebResponse> FOLDER_DATA_RESPONSE =
    DataResponse.<FileWebResponse>builder().code(200)
      .status("OK")
      .data(FILE_WEB_RESPONSE_FOLDER)
      .build();
  
  private static final PagingResponse<FileWebResponse> FILE_PAGING_RESPONSE =
    PagingResponse.<FileWebResponse>builder().code(200)
      .status("OK")
      .data(Collections.singletonList(FILE_WEB_RESPONSE_FILE))
      .paging(PageHelper.toPaging(PAGE))
      .build();
  
  @Test
  public void testGivenFileV2ByMappingToDataResponseReturnDataResponse() {
    
    DataResponse<FileWebResponse> dataResponse =
      FileResponseMapper.toFileDataResponse(FOLDER);
    
    assertThat(dataResponse).isNotNull();
    assertThat(dataResponse).isEqualTo(FOLDER_DATA_RESPONSE);
  }
  
  @Test
  public void testGivenPageOfFileV2ByMappingToPagingResponseReturnPagingResponse() {
    
    PagingResponse<FileWebResponse> pagingResponse =
      FileResponseMapper.toFilePagingResponse(PAGE);
    
    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(FILE_PAGING_RESPONSE);
  }
  
}
