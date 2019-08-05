package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.base.paging.Paging;
import com.future.function.web.model.response.feature.core.CourseWebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseResponseMapperTest {
  
  private static final String ID = "course-id";
  
  private static final String TITLE = "course-title";
  
  private static final String DESCRIPTION = "course-description";
  
  private static final String FILE_URL = "file-url";
  
  private static final String THUMBNAIL_URL = "thumbnail-url";
  
  private static final FileV2 FILE = FileV2.builder()
    .fileUrl(FILE_URL)
    .thumbnailUrl(THUMBNAIL_URL)
    .build();
  
  private static final Course COURSE = Course.builder()
    .id(ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .file(FILE)
    .build();
  
  private static final CourseWebResponse COURSE_WEB_RESPONSE_WITH_THUMBNAIL =
    CourseWebResponse.builder()
      .id(ID)
      .title(TITLE)
      .description(DESCRIPTION)
      .material(THUMBNAIL_URL)
      .build();
  
  private CourseWebResponse courseWebResponseWithoutThumbnail;
  
  private DataResponse<CourseWebResponse> createdDataResponse;
  
  private static final DataResponse<List<CourseWebResponse>>
    CREATED_DATA_RESPONSE_LIST =
    DataResponse.<List<CourseWebResponse>>builder().code(201)
      .status("CREATED")
      .data(Collections.singletonList(COURSE_WEB_RESPONSE_WITH_THUMBNAIL))
      .build();
  
  private DataResponse<CourseWebResponse> retrievedDataResponse;
  
  private static final Pageable PAGEABLE = new PageRequest(0, 2);
  
  private static final Page<Course> PAGE = new PageImpl<>(
    Collections.singletonList(COURSE), PAGEABLE, 1);
  
  private static final Paging PAGING = Paging.builder()
    .page(1)
    .size(2)
    .totalRecords(1)
    .build();
  
  private static final PagingResponse<CourseWebResponse> PAGING_RESPONSE =
    PagingResponse.<CourseWebResponse>builder().code(200)
      .status("OK")
      .data(Collections.singletonList(COURSE_WEB_RESPONSE_WITH_THUMBNAIL))
      .paging(PAGING)
      .build();
  
  @Before
  public void setUp() {
  
    courseWebResponseWithoutThumbnail =
      CourseWebResponse.builder()
        .id(ID)
        .title(TITLE)
        .description(DESCRIPTION)
        .material(FILE_URL)
        .materialId(FILE.getId())
        .build();
  
    createdDataResponse = DataResponse.<CourseWebResponse>builder().code(201)
      .status("CREATED")
      .data(courseWebResponseWithoutThumbnail)
      .build();
  
    retrievedDataResponse =
      DataResponse.<CourseWebResponse>builder().code(200)
        .status("OK")
        .data(courseWebResponseWithoutThumbnail)
        .build();
  }
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenCourseDataByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<CourseWebResponse> createdDataResponse =
      CourseResponseMapper.toCourseDataResponse(HttpStatus.CREATED, COURSE);
    
    assertThat(createdDataResponse).isNotNull();
    assertThat(createdDataResponse).isEqualTo(this.createdDataResponse);
    
    DataResponse<CourseWebResponse> retrievedDataResponse =
      CourseResponseMapper.toCourseDataResponse(COURSE);
    
    assertThat(retrievedDataResponse).isNotNull();
    assertThat(retrievedDataResponse).isEqualTo(this.retrievedDataResponse);
  }
  
  @Test
  public void testGivenCoursesDataByMappingToPagingResponseReturnPagingResponseObject() {
    
    PagingResponse<CourseWebResponse> pagingResponse =
      CourseResponseMapper.toCoursesPagingResponse(PAGE);
    
    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(PAGING_RESPONSE);
  }
  
  @Test
  public void testGivenListOfCoursesByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<List<CourseWebResponse>> dataResponse =
      CourseResponseMapper.toCoursesDataResponse(Collections.singletonList(COURSE));
  
    assertThat(dataResponse).isNotNull();
    assertThat(dataResponse).isEqualTo(CREATED_DATA_RESPONSE_LIST);
  }
  
}
