package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.Course;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.CourseRepository;
import com.future.function.service.api.feature.core.ResourceService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceImplTest {
  
  private static final String ID = "id";
  
  private static final String TITLE = "title";
  
  private static final String DESCRIPTION = "description";
  
  private static final String FILE_ID = "file-id";
  
  private static final FileV2 FILE_V2 = FileV2.builder()
    .id(FILE_ID)
    .build();
  
  private Course course;
  
  @Mock
  private CourseRepository courseRepository;
  
  @Mock
  private ResourceService resourceService;
  
  @InjectMocks
  private CourseServiceImpl courseService;
  
  @Before
  public void setUp() {
    course = Course.builder()
      .id(ID)
      .title(TITLE)
      .description(DESCRIPTION)
      .file(FILE_V2)
      .build();
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(courseRepository, resourceService);
  }
  
  @Test
  public void testGivenCourseByCreatingCourseReturnCreatedCourse() {
    
    when(courseRepository.save(course)).thenReturn(course);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(courseRepository.findOne(ID)).thenReturn(course);
    
    Course createdCourse = courseService.createCourse(course);
    
    assertThat(createdCourse).isEqualTo(course);
    
    verify(courseRepository).save(course);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), true);
    verify(resourceService).getFile(FILE_ID);
    verify(courseRepository).findOne(ID);
    verifyZeroInteractions(resourceService);
  }
  
  @Test
  public void testGivenInvalidCourseByCreatingCourseReturnUnsupportedOperationException() {
    
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(courseRepository.save(course)).thenReturn(null);
    
    catchException(() -> courseService.createCourse(course));
    
    assertThat(caughtException().getClass()).isEqualTo(
      UnsupportedOperationException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Create Course Failed");
    
    verify(courseRepository).save(course);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), true);
    verify(resourceService).getFile(FILE_ID);
    verifyZeroInteractions(resourceService);
  }
  
  @Test
  public void testGivenCourseByUpdatingCourseReturnUpdatedCourse() {
    
    when(courseRepository.findOne(ID)).thenReturn(course);
    when(resourceService.getFile(FILE_ID)).thenReturn(FILE_V2);
    when(courseRepository.save(course)).thenReturn(course);
    
    Course updatedCourse = courseService.updateCourse(course);
    
    assertThat(updatedCourse).isEqualTo(course);
    
    verify(courseRepository).findOne(ID);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), false);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), true);
    verify(resourceService, times(2)).getFile(FILE_ID);
    verify(courseRepository).save(course);
  }
  
  @Test
  public void testGivenCourseIdByGettingCourseReturnCourseObject() {
    
    when(courseRepository.findOne(ID)).thenReturn(course);
    
    Course retrievedCourse = courseService.getCourse(ID);
    
    assertThat(retrievedCourse).isEqualTo(course);
    
    verify(courseRepository).findOne(ID);
    verifyZeroInteractions(resourceService);
  }
  
  @Test
  public void testGivenNonExistingCourseIdByGettingCourseReturnNotFoundException() {
    
    when(courseRepository.findOne(ID)).thenReturn(null);
    
    catchException(() -> courseService.getCourse(ID));
    
    assertThat(caughtException().getClass()).isEqualTo(NotFoundException.class);
    assertThat(caughtException().getMessage()).isEqualTo(
      "Get Course Not Found");
    
    verify(courseRepository).findOne(ID);
    verifyZeroInteractions(resourceService);
  }
  
  @Test
  public void testGivenPageableByGettingCoursesReturnPageOfCourse() {
    
    Pageable pageable = new PageRequest(0, 5);
    Page<Course> coursePage = new PageImpl<>(
      Collections.singletonList(course), pageable, 1);
    when(courseRepository.findAll(pageable)).thenReturn(coursePage);
    
    Page<Course> retrievedCourses = courseService.getCourses(pageable);
    
    assertThat(retrievedCourses).isEqualTo(coursePage);
    
    verify(courseRepository).findAll(pageable);
    verifyZeroInteractions(resourceService);
  }
  
  @Test
  public void testGivenCourseIdByDeletingCourseReturnSuccessfulDeletion() {
    
    when(courseRepository.findOne(ID)).thenReturn(course);
    
    courseService.deleteCourse(ID);
    
    verify(courseRepository).findOne(ID);
    verify(resourceService).markFilesUsed(
      Collections.singletonList(FILE_ID), false);
    verify(resourceService).getFile(FILE_ID);
    verify(courseRepository).delete(course);
  }
  
}
