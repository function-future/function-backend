package com.future.function.validation.validator.scoring;

import com.future.function.repository.feature.scoring.AssignmentRepository;
import com.future.function.validation.annotation.scoring.AssignmentMustExist;
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
public class AssignmentMustExistValidatorTest {

  private static final String ASSIGNMENT_ID = "assignment-id";

  private AssignmentMustExist annotation;

  private ConstraintValidatorContext context;

  @Mock
  private AssignmentRepository assignmentRepository;

  @InjectMocks
  private AssignmentMustExistValidator validator;

  @Before
  public void setUp() throws Exception {
    when(assignmentRepository.existsByIdAndDeletedFalse(ASSIGNMENT_ID)).thenReturn(true);
  }

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(assignmentRepository);
  }

  @Test
  public void initialize() {
    validator.initialize(annotation);
  }

  @Test
  public void isValid() {
    boolean result = validator.isValid(ASSIGNMENT_ID, context);
    assertThat(result).isTrue();
    verify(assignmentRepository).existsByIdAndDeletedFalse(ASSIGNMENT_ID);
  }

  @Test
  public void isNotValid() {
    boolean result = validator.isValid("id", context);
    assertThat(result).isFalse();
    verify(assignmentRepository).existsByIdAndDeletedFalse("id");
  }
}
