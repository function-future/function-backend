package com.future.function.validation.validator.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.feature.core.BatchRepository;
import com.future.function.validation.annotation.core.UniqueBatchCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UniqueBatchCodeValidatorTest {
  
  @Mock
  private UniqueBatchCode annotation;
  
  @Mock
  private BatchRepository batchRepository;
  
  @InjectMocks
  private UniqueBatchCodeValidator validator;
  
  @Before
  public void setUp() {
    
    validator.initialize(annotation);
  }
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(annotation, batchRepository);
  }
  
  @Test
  public void testGivenNullValueByValidatingUniqueBatchCodeReturnFalse() {
    
    assertThat(validator.isValid(null, null)).isFalse();
  }
  
  @Test
  public void testGivenExistingCodeByValidatingUniqueBatchCodeReturnFalse() {
    
    String code = "code";
    when(batchRepository.findByCode(code)).thenReturn(
      Optional.of(new Batch("id", "name", code)));
    
    assertThat(validator.isValid(code, null)).isFalse();
    
    verify(batchRepository).findByCode(code);
  }
  
  @Test
  public void testGivenNonExistingCodeByValidatingUniqueBatchCodeReturnTrue() {
    
    String code = "code";
    when(batchRepository.findByCode(code)).thenReturn(Optional.empty());
    
    assertThat(validator.isValid(code, null)).isTrue();
    
    verify(batchRepository).findByCode(code);
  }
  
}