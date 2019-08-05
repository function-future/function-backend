package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.request.core.ResourceRequestMapper;
import com.future.function.web.mapper.response.core.ResourceResponseMapper;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.FileContentWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(TestSecurityConfiguration.class)
@WebMvcTest(value = ResourceController.class)
public class ResourceControllerTest extends TestHelper {

  private static final byte[] BYTES = new byte[0];

  private static final String NAME = "name";

  private static final String ORIGINAL_NAME = NAME + ".txt";

  private static final String ORIGIN = "ANNOUNCEMENT";

  private static final String ID = "id";

  private static final String FILE_URL = "file-url";

  private static final FileV2 FILE_V2_NULL_THUMBNAIL = FileV2.builder()
    .id(ID)
    .name(NAME)
    .fileUrl(FILE_URL)
    .thumbnailUrl(null)
    .build();

  private static final DataResponse<FileContentWebResponse>
    CREATED_DATA_RESPONSE_NULL_THUMBNAIL =
    ResourceResponseMapper.toResourceDataResponse(FILE_V2_NULL_THUMBNAIL);

  @MockBean
  private ResourceService resourceService;

  @MockBean
  private ResourceRequestMapper resourceRequestMapper;

  @Override
  @Before
  public void setUp() {

    super.setUp();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(resourceService, resourceRequestMapper);
  }

  @Test
  public void testGivenApiCallByStoringFileReturnDataResponseObject()
    throws Exception {
    
    super.setCookie(Role.MENTOR);

    Pair<String, byte[]> pair = Pair.of(NAME, BYTES);

    when(resourceRequestMapper.toStringAndByteArrayPair(
      any(MultipartFile.class))).thenReturn(pair);
    when(resourceService.storeAndSaveFile(null, NAME, BYTES,
                                          FileOrigin.ANNOUNCEMENT
    )).thenReturn(FILE_V2_NULL_THUMBNAIL);

    mockMvc.perform(post("/api/core/resources").cookie(cookies)
                      .contentType(MediaType.MULTIPART_FORM_DATA)
                      .param("file", "")
                      .param("origin", ORIGIN))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(CREATED_DATA_RESPONSE_NULL_THUMBNAIL)
          .getJson()));

    verify(resourceRequestMapper).toStringAndByteArrayPair(
      any(MultipartFile.class));
    verify(resourceService).storeAndSaveFile(
      null, NAME, BYTES, FileOrigin.ANNOUNCEMENT);
  }

  @Test
  public void testGivenApiCallAndFileNameByGettingFileAsByteArrayReturnByteArray()
    throws Exception {

    when(resourceRequestMapper.getMediaType(eq(ORIGINAL_NAME),
                                            any(HttpServletRequest.class)
    )).thenReturn(MediaType.TEXT_PLAIN);
    when(resourceService.getFileAsByteArray(Role.UNKNOWN, ORIGINAL_NAME,
                                            FileOrigin.ANNOUNCEMENT, null
    )).thenReturn(BYTES);

    mockMvc.perform(
      get("/api/core/resources/" + ORIGIN + "/" + ORIGINAL_NAME))
      .andExpect(status().isOk())
      .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                                 "attachment; filename=\"" + ORIGINAL_NAME +
                                 "\""
      ))
      .andExpect(content().bytes(BYTES));

    verify(resourceRequestMapper).getMediaType(
      eq(ORIGINAL_NAME), any(HttpServletRequest.class));
    verify(resourceService).getFileAsByteArray(
      Role.UNKNOWN, ORIGINAL_NAME, FileOrigin.ANNOUNCEMENT, null);
    verifyZeroInteractions(resourceRequestMapper);
  }

}
