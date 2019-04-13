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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BatchRepositoryTest {
  
  private static final Long NUMBER = 1L;
  
  @Autowired
  private BatchRepository batchRepository;
  
  @Before
  public void setUp() {
  
    batchRepository.save(new Batch(NUMBER));
  }
  
  @After
  public void tearDown() {
    
    batchRepository.deleteAll();
  }
  
  @Test
  public void testGivenMethodCallByFindingFirstBatchReturnBatchObject() {
    
    Optional<Batch> foundBatch =
      batchRepository.findFirstByIdIsNotNullOrderByUpdatedAtDesc();
  
    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get()
                 .getNumber()).isEqualTo(NUMBER);
  }
  
  @Test
  public void testGivenBatchNumberByFindingBatchByNumberReturnBatchObject() {
    
    Optional<Batch> foundBatch = batchRepository.findByNumber(NUMBER);
    
    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get()
                 .getNumber()).isEqualTo(NUMBER);
  }
  
}
