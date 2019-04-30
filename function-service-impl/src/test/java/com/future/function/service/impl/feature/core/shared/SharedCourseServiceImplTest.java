package com.future.function.service.impl.feature.core.shared;

import com.future.function.repository.feature.core.shared.SharedCourseRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.CourseService;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SharedCourseServiceImplTest {
  
  @Mock
  private CourseService courseService;
  
  @Mock
  private BatchService batchService;
  
  @Mock
  private SharedCourseRepository sharedCourseRepository;
  
  @InjectMocks
  private SharedCourseServiceImpl sharedCourseService;
  
  @Before
  public void setUp() {
  
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(
      courseService, batchService, sharedCourseRepository);
  }
  
}