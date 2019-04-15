package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BatchRepositoryTest {
  
  private static final Long NUMBER_1 = 1L;
  
  private static final Long NUMBER_2 = 2L;
  
  @Autowired
  private BatchRepository batchRepository;
  
  @Before
  public void setUp() {
  
    Batch firstBatch = new Batch(NUMBER_1);
    firstBatch.setUpdatedAt(10L);
    Batch secondBatch = new Batch(NUMBER_2);
    secondBatch.setUpdatedAt(20L);
  
    batchRepository.save(Arrays.asList(firstBatch, secondBatch));
  }
  
  @After
  public void tearDown() {
    
    batchRepository.deleteAll();
  }
  
  @Test
  public void testGivenMethodCallByFindingBatchesReturnBatchObject() {
    
    List<Batch> foundBatch =
      batchRepository.findAllByIdIsNotNullOrderByUpdatedAtDesc();
    
    assertThat(foundBatch).isNotEqualTo(Collections.emptyList());
    assertThat(foundBatch.get(0)
                 .getNumber()).isEqualTo(NUMBER_2);
    assertThat(foundBatch.get(1)
                 .getNumber()).isEqualTo(NUMBER_1);
  }
  
  @Test
  public void testGivenMethodCallByFindingFirstBatchReturnBatchObject() {
    
    Optional<Batch> foundBatch =
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  
    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get()
                 .getNumber()).isEqualTo(NUMBER_2);
  }
  
  @Test
  public void testGivenBatchNumberByFindingBatchByNumberReturnBatchObject() {
  
    Optional<Batch> foundBatch = batchRepository.findByNumber(NUMBER_1);
    
    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get()
                 .getNumber()).isEqualTo(NUMBER_1);
  }
  
}
