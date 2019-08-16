package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.service.api.feature.core.AnnouncementService;
import com.future.function.web.TestHelper;
import com.future.function.web.TestSecurityConfiguration;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.AnnouncementRequestMapper;
import com.future.function.web.mapper.response.core.AnnouncementResponseMapper;
import com.future.function.web.model.request.core.AnnouncementWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.AnnouncementWebResponse;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

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
@WebMvcTest(value = AnnouncementController.class)
public class AnnouncementControllerTest extends TestHelper {

  private static final String ID = "id";

  private static final String TITLE = "title";

  private static final String SUMMARY = "summary";

  private static final String DESCRIPTION_HTML = "description-html";

  private static final Long CREATED_AT = 1L;

  private static final Long UPDATED_AT = 2L;

  private static final Pageable PAGEABLE = new PageRequest(0, 4);

  private static final BaseResponse DELETED_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);

  private static final AnnouncementWebRequest ANNOUNCEMENT_WEB_REQUEST =
    AnnouncementWebRequest.builder()
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .files(null)
      .build();

  private static final String URL_PREFIX = "url-prefix";

  private Announcement announcement;

  private PagingResponse<AnnouncementWebResponse> pagingResponse;

  private DataResponse<AnnouncementWebResponse> retrievedDataResponse;

  private DataResponse<AnnouncementWebResponse> createdDataResponse;

  private JacksonTester<AnnouncementWebRequest>
    announcementWebRequestJacksonTester;

  @MockBean
  private AnnouncementService announcementService;

  @MockBean
  private AnnouncementRequestMapper announcementRequestMapper;

  @MockBean
  private FileProperties fileProperties;

  @Override
  @Before
  public void setUp() {

    announcement = Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .fileV2s(Collections.emptyList())
      .build();

    announcement.setCreatedAt(CREATED_AT);
    announcement.setUpdatedAt(UPDATED_AT);

    Page<Announcement> announcementPage = new PageImpl<>(
      Collections.singletonList(announcement), PAGEABLE, 1);

    pagingResponse = AnnouncementResponseMapper.toAnnouncementsPagingResponse(
      announcementPage, URL_PREFIX);

    retrievedDataResponse =
      AnnouncementResponseMapper.toAnnouncementDataResponse(announcement,
                                                            URL_PREFIX
      );

    createdDataResponse = AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.CREATED, announcement, URL_PREFIX);

    super.setUp();
  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(
      announcementService, announcementRequestMapper, fileProperties);
  }

  @Test
  public void testGivenCallToAnnouncementsApiByGettingAnnouncementsFromAnnouncementServiceReturnPagingResponseOfAnnouncements()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    List<Announcement> announcementList = Collections.singletonList(
      announcement);

    when(announcementService.getAnnouncements(PAGEABLE)).thenReturn(
      new PageImpl<>(announcementList, PAGEABLE, announcementList.size()));

    mockMvc.perform(get("/api/core/announcements").param("page", "1")
                      .param("size", "4"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(pagingResponse)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(announcementService).getAnnouncements(PAGEABLE);
    verifyZeroInteractions(announcementRequestMapper);
  }

  @Test
  public void testGivenCallToAnnouncementsApiWithoutRequestParamsByGettingAnnouncementsFromAnnouncementServiceReturnPagingResponseOfAnnouncements()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    List<Announcement> announcementList = Collections.singletonList(
      announcement);

    when(announcementService.getAnnouncements(PAGEABLE)).thenReturn(
      new PageImpl<>(announcementList, PAGEABLE, announcementList.size()));

    mockMvc.perform(get("/api/core/announcements"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(pagingResponse)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(announcementService).getAnnouncements(PAGEABLE);
    verifyZeroInteractions(announcementRequestMapper);
  }

  @Test
  public void testGivenAnnouncementIdByGettingAnnouncementFromAnnouncementServiceReturnDataResponse()
    throws Exception {

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    when(announcementService.getAnnouncement(ID)).thenReturn(announcement);

    mockMvc.perform(get("/api/core/announcements/" + ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(retrievedDataResponse)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(announcementService).getAnnouncement(ID);
    verifyZeroInteractions(announcementRequestMapper);
  }

  @Test
  public void testGivenAnnouncementIdByDeletingAnnouncementFromAnnouncementServiceReturnBaseResponse()
    throws Exception {

    super.setCookie(Role.ADMIN);

    mockMvc.perform(delete("/api/core/announcements/" + ID).cookie(cookies))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(DELETED_BASE_RESPONSE)
          .getJson()));

    verify(announcementService).deleteAnnouncement(ID);
    verifyZeroInteractions(announcementRequestMapper, fileProperties);
  }

  @Test
  public void testGivenAnnouncementDataByCreatingAnnouncementReturnDataResponse()
    throws Exception {

    super.setCookie(Role.ADMIN);

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    when(announcementService.createAnnouncement(announcement)).thenReturn(
      announcement);
    when(announcementRequestMapper.toAnnouncement(
      ANNOUNCEMENT_WEB_REQUEST)).thenReturn(announcement);

    mockMvc.perform(post("/api/core/announcements").cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(announcementWebRequestJacksonTester.write(
                        ANNOUNCEMENT_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(createdDataResponse)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(announcementService).createAnnouncement(announcement);
    verify(announcementRequestMapper).toAnnouncement(ANNOUNCEMENT_WEB_REQUEST);
  }

  @Test
  public void testGivenAnnouncementDataByUpdatingAnnouncementReturnDataResponse()
    throws Exception {

    super.setCookie(Role.ADMIN);

    when(fileProperties.getUrlPrefix()).thenReturn(URL_PREFIX);

    when(announcementService.updateAnnouncement(announcement)).
      thenReturn(announcement);
    when(announcementRequestMapper.toAnnouncement(ID,
                                                  ANNOUNCEMENT_WEB_REQUEST
    )).thenReturn(announcement);

    mockMvc.perform(put("/api/core/announcements/" + ID).cookie(cookies)
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(announcementWebRequestJacksonTester.write(
                        ANNOUNCEMENT_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(retrievedDataResponse)
          .getJson()));

    verify(fileProperties).getUrlPrefix();
    verify(announcementService).updateAnnouncement(announcement);
    verify(announcementRequestMapper).toAnnouncement(
      ID, ANNOUNCEMENT_WEB_REQUEST);
  }

}
