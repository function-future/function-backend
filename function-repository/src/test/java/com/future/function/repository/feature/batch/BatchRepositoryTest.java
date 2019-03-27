package com.future.function.repository.feature.batch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.future.function.model.entity.feature.batch.Batch;
import com.future.function.repository.TestApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BatchRepositoryTest {

  @Autowired
  private BatchRepository batchRepository;

  private Batch batch;

  private static final Long NUMBER = 1L;

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

    Optional<Batch> foundBatch = batchRepository.findByNumber(NUMBER);

    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get().getNumber()).isEqualTo(NUMBER);
  }
}