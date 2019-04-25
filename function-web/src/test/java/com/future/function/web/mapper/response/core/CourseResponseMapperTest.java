package com.future.function.web.mapper.response.core;

import com.future.function.model.entity.feature.core.Course;
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

import static org.assertj.core.api.Assertions.assertThat;

public class CourseResponseMapperTest {
  
  private static final String COURSE_ID = "course-id";
  
  private static final String COURSE_TITLE = "course-title";
  
  private static final String COURSE_DESCRIPTION = "course-description";
  
  private static final Course COURSE = Course.builder()
    .id(COURSE_ID)
    .title(COURSE_TITLE)
    .description(COURSE_DESCRIPTION)
    .build();
  
  private static final CourseWebResponse COURSE_WEB_RESPONSE =
    CourseWebResponse.builder()
      .courseId(COURSE_ID)
      .courseTitle(COURSE_TITLE)
      .courseDescription(COURSE_DESCRIPTION)
      .courseFileUrl(null)
      .courseThumbnailUrl(null)
      .build();
  
  private static final DataResponse<CourseWebResponse> CREATED_DATA_RESPONSE =
    DataResponse.<CourseWebResponse>builder().code(201)
      .status("CREATED")
      .data(COURSE_WEB_RESPONSE)
      .build();
  
  private static final DataResponse<CourseWebResponse> RETRIEVED_DATA_RESPONSE =
    DataResponse.<CourseWebResponse>builder().code(200)
      .status("OK")
      .data(COURSE_WEB_RESPONSE)
      .build();
  
  private static final Pageable PAGEABLE = new PageRequest(0, 2);
  
  private static final Page<Course> PAGE = new PageImpl<>(
    Collections.singletonList(COURSE), PAGEABLE, 1);
  
  private static final Paging PAGING = Paging.builder()
    .currentPage(0)
    .pageSize(2)
    .totalPages(1)
    .totalRecords(1)
    .build();
  
  private static final PagingResponse<CourseWebResponse> PAGING_RESPONSE =
    PagingResponse.<CourseWebResponse>builder().code(200)
      .status("OK")
      .data(Collections.singletonList(COURSE_WEB_RESPONSE))
      .paging(PAGING)
      .build();
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenCourseDataByMappingToDataResponseReturnDataResponseObject() {
    
    DataResponse<CourseWebResponse> createdDataResponse =
      CourseResponseMapper.toCourseDataResponse(HttpStatus.CREATED, COURSE);
    
    assertThat(createdDataResponse).isNotNull();
    assertThat(createdDataResponse).isEqualTo(CREATED_DATA_RESPONSE);
    
    DataResponse<CourseWebResponse> retrievedDataResponse =
      CourseResponseMapper.toCourseDataResponse(COURSE);
    
    assertThat(retrievedDataResponse).isNotNull();
    assertThat(retrievedDataResponse).isEqualTo(RETRIEVED_DATA_RESPONSE);
    
  }
  
  @Test
  public void testGivenCoursesDataByMappingToPagingResponseReturnPagingResponseObject() {
    
    PagingResponse<CourseWebResponse> pagingResponse =
      CourseResponseMapper.toCoursesPagingResponse(PAGE);
    
    assertThat(pagingResponse).isNotNull();
    assertThat(pagingResponse).isEqualTo(PAGING_RESPONSE);
  }
  
}