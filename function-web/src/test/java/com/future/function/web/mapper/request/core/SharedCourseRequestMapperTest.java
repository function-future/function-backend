package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.SharedCourseWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SharedCourseRequestMapperTest {
  
  private static final String COURSE_ID = "course-id";
  
  private static final String COURSE_TITLE = "course-title";
  
  private static final String COURSE_DESCRIPTION = "course-description";
  
  private static final CourseWebRequest COURSE_WEB_REQUEST =
    CourseWebRequest.builder()
      .title(COURSE_TITLE)
      .description(COURSE_DESCRIPTION)
      .material(Collections.emptyList())
      .build();
  
  private static final Course COURSE = Course.builder()
    .id(COURSE_ID)
    .title(COURSE_TITLE)
    .description(COURSE_DESCRIPTION)
    .file(new FileV2())
    .build();
  
  private static final String BATCH_CODE = "batch-code";
  
  private static final List<String> COURSE_IDS = Collections.singletonList(
    COURSE_ID);
  
  private static final SharedCourseWebRequest SHARED_COURSE_WEB_REQUEST =
    SharedCourseWebRequest.builder()
      .courses(COURSE_IDS)
      .originBatch(BATCH_CODE)
      .build();
  
  @Mock
  private CourseRequestMapper courseRequestMapper;
  
  @Mock
  private RequestValidator validator;
  
  @InjectMocks
  private SharedCourseRequestMapper sharedCourseRequestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(courseRequestMapper, validator);
  }
  
  @Test
  public void testGivenCourseIdAndCourseWebRequestByParsingToCourseReturnCourse() {
    
    when(
      courseRequestMapper.toCourse(COURSE_ID, COURSE_WEB_REQUEST)).thenReturn(
      COURSE);
    
    Course course = sharedCourseRequestMapper.toCourse(COURSE_ID,
                                                       COURSE_WEB_REQUEST
    );
    
    assertThat(course).isNotNull();
    assertThat(course).isEqualTo(COURSE);
    
    verify(courseRequestMapper).toCourse(COURSE_ID, COURSE_WEB_REQUEST);
  }
  
  @Test
  public void testGivenSharedCourseWebRequestByParsingToCourseIdsAndBatchCodePairReturnPair() {
    
    when(validator.validate(SHARED_COURSE_WEB_REQUEST)).thenReturn(
      SHARED_COURSE_WEB_REQUEST);
    
    Pair<List<String>, String> expectedCourseIdsAndBatchCodePair = Pair.of(
      COURSE_IDS, BATCH_CODE);
    
    Pair<List<String>, String> courseIdsAndBatchCodePair =
      sharedCourseRequestMapper.toCourseIdsAndOriginBatchCodePair(
        SHARED_COURSE_WEB_REQUEST);
    
    assertThat(courseIdsAndBatchCodePair).isEqualTo(
      expectedCourseIdsAndBatchCodePair);
    
    verify(validator).validate(SHARED_COURSE_WEB_REQUEST);
    verifyZeroInteractions(courseRequestMapper);
  }
  
}
