package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.File;
import com.future.function.repository.feature.core.CourseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceImplTest {
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final Course COURSE = Course.builder()
    .id(ID)
    .title(TITLE)
    .description(DESCRIPTION)
    .file(new File())
    .build();
  
  @Mock
  private CourseRepository courseRepository;
  
  @InjectMocks
  private CourseServiceImpl courseService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(courseRepository);
  }
  
  @Test
  public void testGivenCourseAndMultipartFileByCreatingCourseReturnCreatedCourse() {
    
    when(courseRepository.save(COURSE)).thenReturn(COURSE);
    when(courseRepository.findOne(ID)).thenReturn(COURSE);
    
    Course createdCourse = courseService.createCourse(COURSE, null);
    
    assertThat(createdCourse).isEqualTo(COURSE);
    
    verify(courseRepository).save(COURSE);
    verify(courseRepository).findOne(ID);
  }
  
  @Test
  public void testGivenInvalidCourseAndMultipartFileByCreatingCourseReturnUnsupportedOperationException() {
    
    when(courseRepository.save(COURSE)).thenReturn(null);
    
    catchException(() -> courseService.createCourse(COURSE, null));
    
    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Create Course Failed");
    
    verify(courseRepository).save(COURSE);
  }
  
  @Test
  public void testGivenCourseAndMultipartFileByUpdatingCourseReturnUpdatedCourse() {
    
    when(courseRepository.findOne(ID)).thenReturn(COURSE);
    when(courseRepository.save(COURSE)).thenReturn(COURSE);
    
    Course updatedCourse = courseService.updateCourse(COURSE, null);
    
    assertThat(updatedCourse).isEqualTo(COURSE);
    
    verify(courseRepository).findOne(ID);
    verify(courseRepository).save(COURSE);
  }
  
  @Test
  public void testGivenInvalidCourseAndMultipartFileByUpdatingCourseReturnUnsupportedOperationException() {
    
    when(courseRepository.findOne(ID)).thenReturn(null);
    
    catchException(() -> courseService.updateCourse(COURSE, null));
    
    assertThat(caughtException().getClass()).isEqualTo(UnsupportedOperationException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Update Course Failed");
    
    verify(courseRepository).findOne(ID);
  }
  
  @Test
  public void testGivenCourseIdByDeletingCourseReturnSuccessfulDeletion() {
    
    courseService.deleteCourse(ID);
    
    verify(courseRepository).delete(ID);
  }
  
}
