package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.embedded.FilePath;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.FileRequestMapper;
import com.future.function.web.mapper.request.core.MultipartFileRequestMapper;
import com.future.function.web.mapper.response.core.FileResponseMapper;
import com.future.function.web.model.request.core.FileWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.DataPageResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import com.future.function.web.model.response.feature.core.FileWebResponse;
import com.future.function.web.model.response.feature.core.embedded.PathWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = FileController.class)
public class FileControllerTest extends TestHelper {

  private static final PageRequest PAGEABLE = new PageRequest(0, 10);

  private static final String PARENT_ID = "parent-id";

  private static final String ID = "id";

  private static final String URL_PREFIX = "url-prefix/";

  private static final List<FilePath> PATHS = Collections.singletonList(
    FilePath.builder()
      .id(PARENT_ID)
      .build());

  private static final FileV2 FILE = FileV2.builder()
    .id(ID)
    .parentId(PARENT_ID)
    .paths(PATHS)
    .build();

  private static final Page<FileV2> PAGE = new PageImpl<>(
    Collections.singletonList(FILE), PAGEABLE, 1);

  private static final DataPageResponse<FileWebResponse<List<FileContentWebResponse>>>
    DATA_PAGE_RESPONSE = FileResponseMapper.toMultipleFileDataResponse(
    Pair.of(PATHS, PAGE), URL_PREFIX);

  private static final DataResponse<FileWebResponse<FileContentWebResponse>>
    RETRIEVED_DATA_RESPONSE = FileResponseMapper.toSingleFileDataResponse(
    FILE, URL_PREFIX);

  private static final DataResponse<FileWebResponse<FileContentWebResponse>>
    CREATED_DATA_RESPONSE = FileResponseMapper.toSingleFileDataResponse(
    HttpStatus.CREATED, FILE, URL_PREFIX);

  private static final BaseResponse OK_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);

  private static final String JSON = "{\"name\":\"NAME\",\"type\":\"FILE\"}";

  private JacksonTester<DataPageResponse> dataPageResponseJacksonTester;

  @MockBean
  private FileService fileService;

  @MockBean
  private MultipartFileRequestMapper multipartFileRequestMapper;

  @MockBean
  private FileRequestMapper fileRequestMapper;

  @MockBean
  private FileProperties fileProperties;

  @Override
  @Before
  public void setUp() {

    super.setUp();
    super.setCookie(Role.ADMIN);
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(fileService, multipartFileRequestMapper,
                             fileRequestMapper, fileProperties
    );
  }

  @Test
  public void testGivenApiCallAndParentIdByGettingFilesAndFoldersReturnPagingResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    when(fileService.getFilesAndFolders(PARENT_ID, PAGEABLE)).thenReturn(
      Pair.of(PATHS, PAGE));

    mockMvc.perform(get("/api/core/files/" + PARENT_ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataPageResponseJacksonTester.write(DATA_PAGE_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(fileService).getFilesAndFolders(PARENT_ID, PAGEABLE);
    verifyZeroInteractions(multipartFileRequestMapper, fileRequestMapper);
  }

  @Test
  public void testGivenApiCallAndFileOrFolderIdAndParentIdByGettingFileOrFolderReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    when(fileService.getFileOrFolder(ID, PARENT_ID)).thenReturn(FILE);

    mockMvc.perform(
      get("/api/core/files/" + PARENT_ID + "/" + ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(fileService).getFileOrFolder(ID, PARENT_ID);
    verifyZeroInteractions(multipartFileRequestMapper, fileRequestMapper);
  }

  @Test
  public void testGivenApiCallAndParentIdByCreatingFileOrFolderReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    when(multipartFileRequestMapper.toStringAndByteArrayPair(
      any(MultipartFile.class))).thenReturn(Pair.of("NAME", ID.getBytes()));

    FileWebRequest request = FileWebRequest.builder()
      .name("NAME")
      .type("FILE")
      .bytes(ID.getBytes())
      .build();
    when(fileRequestMapper.toFileWebRequest(JSON, ID.getBytes())).thenReturn(
      request);

    when(
      fileService.createFileOrFolder(ADMIN_SESSION, PARENT_ID, "NAME", "NAME",
                                     ID.getBytes()
      )).thenReturn(FILE);

    mockMvc.perform(post("/api/core/files/" + PARENT_ID).cookie(cookies)
                      .contentType(MediaType.MULTIPART_FORM_DATA)
                      .param("data", JSON)
                      .param("file", ""))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(multipartFileRequestMapper).toStringAndByteArrayPair(
      any(MultipartFile.class));
    verify(fileRequestMapper).toFileWebRequest(JSON, ID.getBytes());
    verify(fileService).createFileOrFolder(
      ADMIN_SESSION, PARENT_ID, "NAME", "NAME", ID.getBytes());
  }

  @Test
  public void testGivenApiCallAndFileOrFolderIdAndParentIdByUpdatingFileOrFolderReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    when(multipartFileRequestMapper.toStringAndByteArrayPair(
      any(MultipartFile.class))).thenReturn(Pair.of("NAME", ID.getBytes()));

    FileWebRequest request = FileWebRequest.builder()
      .id(ID)
      .name("NAME")
      .type("FILE")
      .bytes(ID.getBytes())
      .build();
    when(
      fileRequestMapper.toFileWebRequest(ID, JSON, ID.getBytes())).thenReturn(
      request);

    when(fileService.updateFileOrFolder(ADMIN_SESSION, ID, PARENT_ID, "NAME",
                                        "NAME", ID.getBytes()
    )).thenReturn(FILE);

    mockMvc.perform(put("/api/core/files/" + PARENT_ID + "/" + ID).cookie(
      cookies)
                      .contentType(MediaType.MULTIPART_FORM_DATA)
                      .param("data", JSON)
                      .param("file", ""))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(RETRIEVED_DATA_RESPONSE)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(multipartFileRequestMapper).toStringAndByteArrayPair(
      any(MultipartFile.class));
    verify(fileRequestMapper).toFileWebRequest(ID, JSON, ID.getBytes());
    verify(fileService).updateFileOrFolder(
      ADMIN_SESSION, ID, PARENT_ID, "NAME", "NAME", ID.getBytes());
  }

  @Test
  public void testGivenApiCallAndFileOrFolderIdAndParentIdByDeletingFileOrFolderReturnBaseResponse()
    throws Exception {

    mockMvc.perform(
      delete("/api/core/files/" + PARENT_ID + "/" + ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(OK_BASE_RESPONSE)
          .getJson()));

    verify(fileService).deleteFileOrFolder(ADMIN_SESSION, PARENT_ID, ID);
    verifyZeroInteractions(
      multipartFileRequestMapper, fileRequestMapper, fileProperties);
  }

}
