package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.validation.RequestValidator;
import com.future.function.web.mapper.request.WebRequestMapper;
import com.future.function.web.model.request.core.CourseWebRequest;
import com.future.function.web.model.request.core.shared.SharedCourseWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseRequestMapperTest {
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final String JSON = "json";
  
  private static final Long ONE = 1L;
  
  private static final Long TWO = 2L;
  
  private static final Course COURSE = Course.builder()
    .id(ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .build();
  
  private static final CourseWebRequest COURSE_WEB_REQUEST =
    CourseWebRequest.builder()
      .courseTitle(TITLE)
      .courseDescription(DESCRIPTION)
      .batchNumbers(Collections.singletonList(ONE))
      .build();
  
  private static final SharedCourseWebRequest SHARED_COURSE_WEB_REQUEST =
    SharedCourseWebRequest.builder()
      .originBatch(ONE)
      .targetBatch(TWO)
      .build();
  
  @Mock
  private WebRequestMapper requestMapper;
  
  @Mock
  private RequestValidator validator;
  
  @InjectMocks
  private CourseRequestMapper courseRequestMapper;
  
  @Before
  public void setUp() {
    
    when(requestMapper.toWebRequestObject(JSON,
                                          CourseWebRequest.class
    )).thenReturn(COURSE_WEB_REQUEST);
    when(validator.validate(COURSE_WEB_REQUEST)).thenReturn(COURSE_WEB_REQUEST);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(requestMapper, validator);
  }
  
  @Test
  public void testGivenJsonDataAsStringByParsingToCourseClassReturnPairObject() {
    
    Pair<Course, List<Long>> parsedData =
      courseRequestMapper.toCourseAndBatchNumbers(JSON);
    
    assertThat(parsedData).isNotNull();
    assertThat(parsedData.getFirst()
                 .getId()).isNotBlank();
    assertThat(parsedData.getFirst()
                 .getTitle()).isEqualTo(TITLE);
    assertThat(parsedData.getFirst()
                 .getDescription()).isEqualTo(DESCRIPTION);
    assertThat(parsedData.getSecond()).isEqualTo(
      Collections.singletonList(ONE));
    
    verify(requestMapper).toWebRequestObject(JSON, CourseWebRequest.class);
    verify(validator).validate(COURSE_WEB_REQUEST);
  }
  
  @Test
  public void testGivenIdAndJsonDataAsStringByParsingToCourseClassReturnPairObject() {
    
    Pair<Course, List<Long>> parsedData =
      courseRequestMapper.toCourseAndBatchNumbers(ID, JSON);
    
    assertThat(parsedData).isNotNull();
    assertThat(parsedData.getFirst()).isEqualTo(COURSE);
    assertThat(parsedData.getSecond()).isEqualTo(
      Collections.singletonList(ONE));
    
    verify(requestMapper).toWebRequestObject(JSON, CourseWebRequest.class);
    verify(validator).validate(COURSE_WEB_REQUEST);
  }
  
  @Test
  public void testGivenJsonDataAsObjectByParsingToListOfLongReturnListOfLong() {
    
    when(validator.validate(SHARED_COURSE_WEB_REQUEST)).thenReturn(
      SHARED_COURSE_WEB_REQUEST);
    
    List<Long> batchNumbers = Arrays.asList(ONE, TWO);
    
    List<Long> parsedBatchNumbers = courseRequestMapper.toCopyCoursesData(
      SHARED_COURSE_WEB_REQUEST);
    
    assertThat(parsedBatchNumbers).isNotNull();
    assertThat(parsedBatchNumbers).isEqualTo(batchNumbers);
    
    verify(validator).validate(SHARED_COURSE_WEB_REQUEST);
  }
  
}