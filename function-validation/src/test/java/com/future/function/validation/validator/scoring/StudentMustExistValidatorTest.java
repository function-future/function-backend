package com.future.function.validation.validator.scoring;

import com.future.function.common.enumeration.core.Role;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.validation.annotation.scoring.StudentMustExist;
import javax.validation.ConstraintValidatorContext;
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
public class StudentMustExistValidatorTest {

  private static final String STUDENT_ID = "student-id";

  private StudentMustExist annotation;

  private ConstraintValidatorContext context;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private StudentMustExistValidator validator;

  @Before
  public void setUp() throws Exception {
    when(userRepository.existsByIdAndRoleAndDeletedFalse(STUDENT_ID, Role.STUDENT))
        .thenReturn(true);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(userRepository);
  }

  @Test
  public void initialize() {
    validator.initialize(annotation);
  }

  @Test
  public void isValid() {
    boolean result = validator.isValid(STUDENT_ID, context);
    assertThat(result).isTrue();
    verify(userRepository).existsByIdAndRoleAndDeletedFalse(STUDENT_ID, Role.STUDENT);
  }

  @Test
  public void isNotValid() {
    boolean result = validator.isValid("id", context);
    assertThat(result).isFalse();
    verify(userRepository).existsByIdAndRoleAndDeletedFalse("id", Role.STUDENT);
  }
}
