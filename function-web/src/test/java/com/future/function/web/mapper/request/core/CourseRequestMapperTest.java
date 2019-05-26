package com.future.function.web.mapper.request.core;

import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.validation.RequestValidator;
import com.future.function.web.model.request.core.CourseWebRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseRequestMapperTest {
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final Course COURSE_WITH_EMPTY_FILE = Course.builder()
    .id(ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .file(new FileV2())
    .build();
  
  private static final String MATERIAL = "material-id";
  
  private static final FileV2 FILE = FileV2.builder()
    .id(MATERIAL)
    .build();
  
  private static final Course COURSE_WITH_FILE = Course.builder()
    .id(ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .file(FILE)
    .build();
  
  private static final CourseWebRequest COURSE_WEB_REQUEST_WITHOUT_MATERIAL =
    CourseWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .build();
  
  private static final CourseWebRequest COURSE_WEB_REQUEST_WITH_MATERIAL =
    CourseWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .material(Collections.singletonList(MATERIAL))
      .build();
  
  private static final CourseWebRequest COURSE_WEB_REQUEST_WITH_EMPTY_MATERIAL =
    CourseWebRequest.builder()
      .title(TITLE)
      .description(DESCRIPTION)
      .material(Collections.emptyList())
      .build();
  
  @Mock
  private RequestValidator validator;
  
  @InjectMocks
  private CourseRequestMapper courseRequestMapper;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(validator);
  }
  
  @Test
  public void testGivenCourseWebRequestByParsingToCourseClassReturnCourseObject() {
    
    when(validator.validate(COURSE_WEB_REQUEST_WITHOUT_MATERIAL)).thenReturn(
      COURSE_WEB_REQUEST_WITHOUT_MATERIAL);
    
    Course parsedCourse = courseRequestMapper.toCourse(
      COURSE_WEB_REQUEST_WITHOUT_MATERIAL);
    
    assertThat(parsedCourse).isNotNull();
    assertThat(parsedCourse.getId()).isNotBlank();
    assertThat(parsedCourse.getTitle()).isEqualTo(TITLE);
    assertThat(parsedCourse.getDescription()).isEqualTo(DESCRIPTION);
    
    verify(validator).validate(COURSE_WEB_REQUEST_WITHOUT_MATERIAL);
  }
  
  @Test
  public void testGivenCourseIdAndCourseWebRequestByParsingToCourseClassReturnCourseObject() {
    
    when(validator.validate(COURSE_WEB_REQUEST_WITHOUT_MATERIAL)).thenReturn(
      COURSE_WEB_REQUEST_WITHOUT_MATERIAL);
    
    Course parsedCourse = courseRequestMapper.toCourse(ID,
                                                       COURSE_WEB_REQUEST_WITHOUT_MATERIAL
    );
    
    assertThat(parsedCourse).isNotNull();
    assertThat(parsedCourse).isEqualTo(COURSE_WITH_EMPTY_FILE);
    
    verify(validator).validate(COURSE_WEB_REQUEST_WITHOUT_MATERIAL);
  }
  
  @Test
  public void testGivenCourseWebRequestWithMaterialByParsingToCourseClassReturnCourseObject() {
    
    when(validator.validate(COURSE_WEB_REQUEST_WITH_MATERIAL)).thenReturn(
      COURSE_WEB_REQUEST_WITH_MATERIAL);
    
    Course parsedCourse = courseRequestMapper.toCourse(
      COURSE_WEB_REQUEST_WITH_MATERIAL);
    
    assertThat(parsedCourse).isNotNull();
    assertThat(parsedCourse.getId()).isNotBlank();
    assertThat(parsedCourse.getTitle()).isEqualTo(TITLE);
    assertThat(parsedCourse.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(parsedCourse.getFile()).isEqualTo(FILE);
    
    verify(validator).validate(COURSE_WEB_REQUEST_WITH_MATERIAL);
  }
  
  @Test
  public void testGivenCourseWebRequestWithEmptyMaterialByParsingToCourseClassReturnCourseObject() {
    
    when(validator.validate(COURSE_WEB_REQUEST_WITH_EMPTY_MATERIAL)).thenReturn(
      COURSE_WEB_REQUEST_WITH_EMPTY_MATERIAL);
    
    Course parsedCourse = courseRequestMapper.toCourse(
      COURSE_WEB_REQUEST_WITH_EMPTY_MATERIAL);
    
    assertThat(parsedCourse).isNotNull();
    assertThat(parsedCourse.getId()).isNotBlank();
    assertThat(parsedCourse.getTitle()).isEqualTo(TITLE);
    assertThat(parsedCourse.getDescription()).isEqualTo(DESCRIPTION);
    assertThat(parsedCourse.getFile()).isEqualTo(new FileV2());
    
    verify(validator).validate(COURSE_WEB_REQUEST_WITH_EMPTY_MATERIAL);
  }
  
}
