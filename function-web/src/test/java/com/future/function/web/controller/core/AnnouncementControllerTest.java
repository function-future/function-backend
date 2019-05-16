package com.future.function.web.controller.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.service.api.feature.core.AnnouncementService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AnnouncementController.class)
public class AnnouncementControllerTest {
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String SUMMARY = "summary";
  
  private static final String DESCRIPTION_HTML = "description-html";
  
  private static final Long CREATED_AT = 1L;
  
  private static final Long UPDATED_AT = 2L;
  
  private static final Pageable PAGEABLE = new PageRequest(0, 4);
  
  private static final BaseResponse DELETED_BASE_RESPONSE =
    ResponseHelper.toBaseResponse(HttpStatus.OK);
  
  private static final String DATA =
    "{\"title\":" + TITLE + "," + "\"summary\":" + SUMMARY +
    ",\"description\":" + DESCRIPTION_HTML + "}";
  
  private static final AnnouncementWebRequest ANNOUNCEMENT_WEB_REQUEST =
    AnnouncementWebRequest.builder()
      .title(TITLE)
      .summary(SUMMARY)
      .description(DESCRIPTION_HTML)
      .files(null)
      .build();
  
  private Announcement announcement;
  
  private PagingResponse<AnnouncementWebResponse> pagingResponse;
  
  private DataResponse<AnnouncementWebResponse> retrievedDataResponse;
  
  private DataResponse<AnnouncementWebResponse> createdDataResponse;
  
  private JacksonTester<PagingResponse<AnnouncementWebResponse>>
    pagingResponseJacksonTester;
  
  private JacksonTester<DataResponse<AnnouncementWebResponse>>
    dataResponseJacksonTester;
  
  private JacksonTester<BaseResponse> baseResponseJacksonTester;
  
  private JacksonTester<AnnouncementWebRequest>
    announcementWebRequestJacksonTester;
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private AnnouncementService announcementService;
  
  @MockBean
  private AnnouncementRequestMapper announcementRequestMapper;
  
  @Before
  public void setUp() {
    
    JacksonTester.initFields(this, new ObjectMapper());
    
    announcement = Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(SUMMARY)
      .descriptionHtml(DESCRIPTION_HTML)
      .fileV2s(Collections.emptyList())
      .build();
    
    announcement.setCreatedAt(CREATED_AT);
    announcement.setUpdatedAt(UPDATED_AT);
    
    Page<Announcement> announcementPage = new PageImpl<>(
      Collections.singletonList(announcement), PAGEABLE, 1);
    
    pagingResponse = AnnouncementResponseMapper.toAnnouncementsPagingResponse(
      announcementPage);
    
    retrievedDataResponse =
      AnnouncementResponseMapper.toAnnouncementDataResponse(announcement);
    
    createdDataResponse = AnnouncementResponseMapper.toAnnouncementDataResponse(
      HttpStatus.CREATED, announcement);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(announcementService, announcementRequestMapper);
  }
  
  @Test
  public void testGivenCallToAnnouncementsApiByGettingAnnouncementsFromAnnouncementServiceReturnPagingResponseOfAnnouncements()
    throws Exception {
    
    List<Announcement> announcementList = Collections.singletonList(
      announcement);
    
    given(announcementService.getAnnouncements(PAGEABLE)).willReturn(
      new PageImpl<>(announcementList, PAGEABLE, announcementList.size()));
    
    mockMvc.perform(get("/api/core/announcements").param("page", "1")
                      .param("size", "4"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(pagingResponse)
          .getJson()));
    
    verify(announcementService).getAnnouncements(PAGEABLE);
  }
  
  @Test
  public void testGivenCallToAnnouncementsApiWithoutRequestParamsByGettingAnnouncementsFromAnnouncementServiceReturnPagingResponseOfAnnouncements()
    throws Exception {
    
    List<Announcement> announcementList = Collections.singletonList(
      announcement);
    
    given(announcementService.getAnnouncements(PAGEABLE)).willReturn(
      new PageImpl<>(announcementList, PAGEABLE, announcementList.size()));
    
    mockMvc.perform(get("/api/core/announcements"))
      .andExpect(status().isOk())
      .andExpect(content().json(
        pagingResponseJacksonTester.write(pagingResponse)
          .getJson()));
    
    verify(announcementService).getAnnouncements(PAGEABLE);
  }
  
  @Test
  public void testGivenAnnouncementIdByGettingAnnouncementFromAnnouncementServiceReturnDataResponse()
    throws Exception {
    
    given(announcementService.getAnnouncement(ID)).willReturn(announcement);
    
    mockMvc.perform(get("/api/core/announcements/" + ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(retrievedDataResponse)
          .getJson()));
    
    verify(announcementService).getAnnouncement(ID);
  }
  
  @Test
  public void testGivenAnnouncementIdByDeletingAnnouncementFromAnnouncementServiceReturnBaseResponse()
    throws Exception {
    
    mockMvc.perform(delete("/api/core/announcements/" + ID))
      .andExpect(status().isOk())
      .andExpect(content().json(
        baseResponseJacksonTester.write(DELETED_BASE_RESPONSE)
          .getJson()));
    
    verify(announcementService).deleteAnnouncement(ID);
  }
  
  @Test
  public void testGivenAnnouncementDataByCreatingAnnouncementReturnDataResponse()
    throws Exception {
    
    given(announcementService.createAnnouncement(announcement)).willReturn(
      announcement);
    given(announcementRequestMapper.toAnnouncement(
      ANNOUNCEMENT_WEB_REQUEST)).willReturn(announcement);
    
    mockMvc.perform(post("/api/core/announcements").contentType(
      MediaType.APPLICATION_JSON_VALUE)
                      .content(announcementWebRequestJacksonTester.write(
                        ANNOUNCEMENT_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isCreated())
      .andExpect(content().json(
        dataResponseJacksonTester.write(createdDataResponse)
          .getJson()));
    
    verify(announcementService).createAnnouncement(announcement);
    verify(announcementRequestMapper).toAnnouncement(ANNOUNCEMENT_WEB_REQUEST);
  }
  
  @Test
  public void testGivenAnnouncementDataByUpdatingAnnouncementReturnDataResponse()
    throws Exception {
    
    given(announcementService.updateAnnouncement(announcement)).
      willReturn(announcement);
    given(announcementRequestMapper.toAnnouncement(ID,
                                                   ANNOUNCEMENT_WEB_REQUEST
    )).willReturn(announcement);
    
    mockMvc.perform(put("/api/core/announcements/" + ID).contentType(
      MediaType.APPLICATION_JSON_VALUE)
                      .content(announcementWebRequestJacksonTester.write(
                        ANNOUNCEMENT_WEB_REQUEST)
                                 .getJson()))
      .andExpect(status().isOk())
      .andExpect(content().json(
        dataResponseJacksonTester.write(retrievedDataResponse)
          .getJson()));
    
    verify(announcementService).updateAnnouncement(announcement);
    verify(announcementRequestMapper).toAnnouncement(ID, ANNOUNCEMENT_WEB_REQUEST);
  }
  
}
