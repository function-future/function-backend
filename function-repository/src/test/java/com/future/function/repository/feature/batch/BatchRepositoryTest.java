package com.future.function.repository.feature.batch;

import com.future.function.model.entity.feature.batch.Batch;
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
  
  private Batch batch;
  
  @Before
  public void setUp() throws Exception {
    
    batch = Batch.builder()
      .number(NUMBER)
      .build();
    batchRepository.save(batch);
  }
  
  @After
  public void tearDown() throws Exception {
    
    batchRepository.deleteAll();
  }
  
  @Test
  public void testGivenBatchNumberByFindingBatchByNumberReturnBatchObject() {
    
    Optional<Batch> foundBatch = batchRepository.findByNumberAndDeletedIsFalse(
      NUMBER);
    
    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get()
                 .getNumber()).isEqualTo(NUMBER);
  }
  
}
