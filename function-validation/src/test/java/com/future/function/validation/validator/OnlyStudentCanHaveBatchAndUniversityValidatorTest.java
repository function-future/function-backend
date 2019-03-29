package com.future.function.validation.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.future.function.common.data.UserData;

@RunWith(MockitoJUnitRunner.class)
public class OnlyStudentCanHaveBatchAndUniversityValidatorTest {

  private static final String NONSTUDENT = "NONSTUDENT";

  private static final String STUDENT = "STUDENT";

  private static final Long BATCH = 1L;

  private static final String UNIVERSITY = "university";

  @Mock
  private UserData userData;

  @InjectMocks
  private OnlyStudentCanHaveBatchAndUniversityValidator validator;

  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {

    verify(userData, times(1)).getRoleAsString();
    verify(userData, times(1)).getBatchNumber();
    verify(userData, times(1)).getUniversity();

    verifyNoMoreInteractions(userData);
  }

  @Test
  public void testGivenRoleNonStudentAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isFalse();
  }

  @Test
  public void testGivenRoleNonStudentAndNotNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isFalse();
  }

  @Test
  public void testGivenRoleNonStudentAndNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isFalse();
  }

  @Test
  public void testGivenRoleNonStudentAndNullBatchAndNullUniversityByValidatingUserObjectReturnTrue() {

    when(userData.getRoleAsString()).thenReturn(NONSTUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isTrue();
  }

  @Test
  public void testGivenRoleStudentAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnTrue() {

    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isTrue();
  }

  @Test
  public void testGivenRoleStudentAndNotNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isFalse();
  }

  @Test
  public void testGivenRoleStudentAndNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isFalse();
  }

  @Test
  public void testGivenRoleStudentAndNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRoleAsString()).thenReturn(STUDENT);
    when(userData.getBatchNumber()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isFalse();
  }

}