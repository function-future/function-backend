package com.future.function.validation.validator;

import com.future.function.common.data.UserData;
import com.future.function.validation.annotation.OnlyStudentCanHaveBatchAndUniversity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlyStudentCanHaveBatchAndUniversityValidatorTest {
  
  private static final String NONSTUDENT = "NONSTUDENT";
  
  private static final String UNKNOWN = "UNKNOWN";
  
  private static final String STUDENT = "STUDENT";
  
  private static final Long BATCH = 1L;
  
  private static final String UNIVERSITY = "university";
  
  @Mock
  private UserData userData;
  
  @Mock
  private OnlyStudentCanHaveBatchAndUniversity annotation;
  
  @InjectMocks
  private OnlyStudentCanHaveBatchAndUniversityValidator validator;
  
  @Before
  public void setUp() {
  
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(userData);
  }
  
  @Test
  public void testGivenRoleNonStudentAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {
    
    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);
    
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
  @Test
  public void testGivenRoleUnknownAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {
    
    when(userData.getRoleAsString()).thenReturn(UNKNOWN);
    
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData).getRoleAsString();
  }
  
  @Test
  public void testGivenRoleNonStudentAndNotNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {
    
    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(null);
    
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
  @Test
  public void testGivenRoleNonStudentAndNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {
    
    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);
    
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
  @Test
  public void testGivenRoleNonStudentAndNullBatchAndNullUniversityByValidatingUserObjectReturnTrue() {
    
    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(null);
    
    assertThat(validator.isValid(userData, null)).isTrue();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
  @Test
  public void testGivenRoleStudentAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnTrue() {
    
    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);
    
    assertThat(validator.isValid(userData, null)).isTrue();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
  @Test
  public void testGivenRoleStudentAndNotNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {
    
    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(null);
    
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
  @Test
  public void testGivenRoleStudentAndNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {
    
    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);
    
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
  @Test
  public void testGivenRoleStudentAndNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {
    
    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(null);
    
    assertThat(validator.isValid(userData, null)).isFalse();
  
    verify(userData).getRoleAsString();
    verify(userData).getBatchNumber();
    verify(userData).getUniversity();
  }
  
}
