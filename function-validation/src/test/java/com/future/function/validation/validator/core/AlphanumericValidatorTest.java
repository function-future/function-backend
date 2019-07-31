package com.future.function.validation.validator.core;

import com.future.function.validation.annotation.core.Alphanumeric;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class AlphanumericValidatorTest {
  
  private static final String VALID_CODE = "Code1";
  
  private static final String INVALID_CODE = "Code Code 1";
  
  @Mock
  private Alphanumeric annotation;
  
  @InjectMocks
  private AlphanumericValidator validator;
  
  @Before
  public void setUp() throws Exception {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation);
  }
  
  @Test
  public void testGivenValidStringAsNameByValidatingNameReturnTrue() {
    
    assertThat(validator.isValid(VALID_CODE, null)).isTrue();
  }
  
  @Test
  public void testGivenValidStringAsNameByValidatingNameReturnFalse() {
    
    assertThat(validator.isValid(INVALID_CODE, null)).isFalse();
  }
  
  @Test
  public void testGivenNullStringAsNameByValidatingNameReturnFalse() {
    
    assertThat(validator.isValid(null, null)).isFalse();
  }
  
}
