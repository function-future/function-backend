package com.future.function.validation.validator.core;

import com.future.function.common.data.core.BatchData;
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
  
  private static final String ID = "id";
  
  private static final String CODE = "code";
  
  @Mock
  private UniqueBatchCode annotation;
  
  @Mock
  private BatchData batchData;
  
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
    
    verifyNoMoreInteractions(annotation, batchData, batchRepository);
  }
  
  @Test
  public void testGivenNullIdAndNewCodeByValidatingUniqueBatchCodeReturnTrue() {
    
    when(batchData.getCode()).thenReturn(CODE);
    when(batchRepository.findByCode(CODE)).thenReturn(Optional.empty());
    
    assertThat(validator.isValid(batchData, null)).isTrue();
    
    verify(batchData).getCode();
    verify(batchRepository).findByCode(CODE);
  }
  
  @Test
  public void testGivenBatchIdAndExistingCodeByValidatingUniqueBatchCodeReturnTrue() {
    
    when(batchData.getId()).thenReturn(ID);
    when(batchData.getCode()).thenReturn(CODE);
    
    Batch batch = Batch.builder()
      .id(ID)
      .code(CODE)
      .build();
    when(batchRepository.findByCode(CODE)).thenReturn(Optional.of(batch));
    
    assertThat(validator.isValid(batchData, null)).isTrue();
    
    verify(batchData).getId();
    verify(batchData).getCode();
    verify(batchRepository).findByCode(CODE);
  }
  
  @Test
  public void testGivenDifferentBatchIdAndExistingCodeByValidatingUniqueBatchCodeReturnTrue() {
    
    when(batchData.getId()).thenReturn(ID + "-1");
    when(batchData.getCode()).thenReturn(CODE);
    
    Batch batch = Batch.builder()
      .id(ID)
      .code(CODE)
      .build();
    when(batchRepository.findByCode(CODE)).thenReturn(Optional.of(batch));
    
    assertThat(validator.isValid(batchData, null)).isFalse();
    
    verify(batchData).getId();
    verify(batchData).getCode();
    verify(batchRepository).findByCode(CODE);
  }
  
}
