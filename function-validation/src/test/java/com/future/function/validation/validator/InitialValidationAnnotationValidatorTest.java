package com.future.function.validation.validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InitialValidationAnnotationValidatorTest {
  
  @InjectMocks
  private InitialValidationAnnotationValidator validator;
  
  /**
   * Define setUp before any test, if none required keep it empty.
   */
  @Before
  public void setUp() throws Exception {
  
  }
  
  /**
   * Define tearDown after any test, if none required keep it empty.
   */
  @After
  public void tearDown() throws Exception {
  
  }
  
  /**
   * Use the following test naming convention:
   * {@code public void testGiven(Condition)By(ExpectedBehavior)Return(Result)}
   * <p>
   * Use Assertions instead of JUnit.
   */
  @Test
  public void testGivenNotEmptyStringByValidatingStringIsEmptyReturnFalse() {
  
  }
  
  /**
   * Use the following test naming convention:
   * {@code public void testGiven(Condition)By(ExpectedBehavior)Return(Result)}
   * <p>
   * Use Assertions instead of JUnit.
   */
  @Test
  public void testGivenNotEmptyStringByValidatingStringIsEmptyReturnTrue() {
  
  }
  
  /**
   * For {@code initialize()} method, simply name the test method as {@code
   * testInitialize()}.
   */
  @Test
  public void testInitialize() {
  
  }
  
}
