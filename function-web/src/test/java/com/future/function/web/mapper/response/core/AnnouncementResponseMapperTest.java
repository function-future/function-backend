package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Announcement;
import com.future.function.model.entity.feature.core.File;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.core.AnnouncementWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnouncementResponseMapperTest {
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String SUMMARY = "summary";
  
  private static final String DESCRIPTION_HTML = "description-html";
  
  private static final Pageable PAGEABLE = new PageRequest(0, 4);
  
  private static final Paging PAGING = Paging.builder()
    .currentPage(0)
    .pageSize(4)
    .totalPages(1)
    .totalRecords(1)
    .build();
  
  private Announcement announcement;
  
  private AnnouncementWebResponse announcementWebResponse;
  
  private Page<Announcement> announcementPage;
  
  private DataResponse<AnnouncementWebResponse> retrievedDataResponse;
  
  private DataResponse<AnnouncementWebResponse> createdDataResponse;
  
  private PagingResponse<AnnouncementWebResponse> pagingResponse;
  
  @Before
  public void setUp() {
    
    announcement = Announcement.builder()
      .id(ID)
      .title(TITLE)
      .summary(SUMMARY)
      .descriptionHtml(DESCRIPTION_HTML)
      .file(new File())
      .build();
    
    announcement.setCreatedAt(1L);
    announcement.setUpdatedAt(2L);
    
    announcementWebResponse = AnnouncementWebResponse.builder()
      .announcementId(ID)
      .announcementTitle(TITLE)
      .announcementSummary(SUMMARY)
      .announcementDescriptionHtml(DESCRIPTION_HTML)
      .announcementFileUrl(null)
      .createdAt(1L)
      .updatedAt(2L)
      .build();
    
    retrievedDataResponse =
      DataResponse.<AnnouncementWebResponse>builder().code(200)
        .status("OK")
        .data(announcementWebResponse)
        .build();
    
    createdDataResponse = DataResponse.<AnnouncementWebResponse>builder().code(
      201)
      .status("CREATED")
      .data(announcementWebResponse)
      .build();
    
    announcementPage = new PageImpl<>(Collections.singletonList(announcement),
                                      PAGEABLE, 1
    );
    
    pagingResponse = PagingResponse.<AnnouncementWebResponse>builder().code(200)
      .status("OK")
      .data(Collections.singletonList(announcementWebResponse))
      .paging(PAGING)
      .build();
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenAnnouncementDataByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<AnnouncementWebResponse> createdDataResponse =
      AnnouncementResponseMapper.toAnnouncementDataResponse(
        HttpStatus.CREATED, announcement);
    
    assertThat(createdDataResponse).isNotNull();
    assertThat(createdDataResponse).isEqualTo(this.createdDataResponse);
    
    announcement.setFile(null);
    DataResponse<AnnouncementWebResponse> retrievedDataResponse =
      AnnouncementResponseMapper.toAnnouncementDataResponse(announcement);
    
    assertThat(retrievedDataResponse).isNotNull();
    assertThat(retrievedDataResponse).isEqualTo(this.retrievedDataResponse);
  }
  
  @Test
  public void testGivenAnnouncementsDataByMappingToPagingResponseReturnPagingResponseObject() {
    
    PagingResponse<AnnouncementWebResponse> pagingResponse =
      AnnouncementResponseMapper.toAnnouncementsPagingResponse(
        announcementPage);
    
    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(this.pagingResponse);
  }
  
}
