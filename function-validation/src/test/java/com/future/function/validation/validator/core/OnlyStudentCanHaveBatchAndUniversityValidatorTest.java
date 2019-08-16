package com.future.function.validation.validator.core;

import com.future.function.common.data.core.UserData;
import com.future.function.validation.annotation.core.OnlyStudentCanHaveBatchAndUniversity;
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

  private static final String BATCH = "1";

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

    verifyNoMoreInteractions(annotation, userData);
  }

  @Test
  public void testGivenRoleNonStudentAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRole()).thenReturn(NONSTUDENT);
    when(userData.getBatch()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isFalse();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

  @Test
  public void testGivenRoleUnknownAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRole()).thenReturn(UNKNOWN);

    assertThat(validator.isValid(userData, null)).isFalse();

    verify(userData).getRole();
  }

  @Test
  public void testGivenRoleNonStudentAndNotNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRole()).thenReturn(NONSTUDENT);
    when(userData.getBatch()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isFalse();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

  @Test
  public void testGivenRoleNonStudentAndNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRole()).thenReturn(NONSTUDENT);
    when(userData.getBatch()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isFalse();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

  @Test
  public void testGivenRoleNonStudentAndNullBatchAndNullUniversityByValidatingUserObjectReturnTrue() {

    when(userData.getRole()).thenReturn(NONSTUDENT);
    when(userData.getBatch()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isTrue();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

  @Test
  public void testGivenRoleStudentAndNotNullBatchAndNotNullUniversityByValidatingUserObjectReturnTrue() {

    when(userData.getRole()).thenReturn(STUDENT);
    when(userData.getBatch()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isTrue();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

  @Test
  public void testGivenRoleStudentAndNotNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRole()).thenReturn(STUDENT);
    when(userData.getBatch()).thenReturn(BATCH);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isFalse();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

  @Test
  public void testGivenRoleStudentAndNullBatchAndNotNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRole()).thenReturn(STUDENT);
    when(userData.getBatch()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(UNIVERSITY);

    assertThat(validator.isValid(userData, null)).isFalse();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

  @Test
  public void testGivenRoleStudentAndNullBatchAndNullUniversityByValidatingUserObjectReturnFalse() {

    when(userData.getRole()).thenReturn(STUDENT);
    when(userData.getBatch()).thenReturn(null);
    when(userData.getUniversity()).thenReturn(null);

    assertThat(validator.isValid(userData, null)).isFalse();

    verify(userData).getRole();
    verify(userData).getBatch();
    verify(userData).getUniversity();
  }

}
