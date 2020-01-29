package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.util.constant.FieldName;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class BatchRepositoryTest {

  private static final String NUMBER_1 = "1";

  private static final String NUMBER_2 = "2";

  private static final Sort SORT = new Sort(
    new Sort.Order(Sort.Direction.DESC, FieldName.BaseEntity.CREATED_AT));

  private static final Pageable PAGEABLE = new PageRequest(0, 10, SORT);

  private static final String ID_1 = "id-1";

  @Autowired
  private BatchRepository batchRepository;

  @Before
  public void setUp() {

    Batch firstBatch = new Batch(ID_1, "name-1", NUMBER_1);
    firstBatch.setCreatedAt(5L);
    firstBatch.setUpdatedAt(10L);
    Batch secondBatch = new Batch("id-2", "name-2", NUMBER_2);
    secondBatch.setCreatedAt(15L);
    secondBatch.setUpdatedAt(20L);

    batchRepository.save(Arrays.asList(firstBatch, secondBatch));
  }

  @After
  public void tearDown() {

    batchRepository.deleteAll();
  }

  @Test
  public void testGivenMethodCallByFindingBatchesReturnListOfBatchObjects() {

    List<Batch> foundBatches = batchRepository.findAllByDeletedFalse();
    List<String> foundBatchCodes = foundBatches.stream()
      .map(Batch::getCode)
      .collect(Collectors.toList());

    assertThat(foundBatchCodes).contains(NUMBER_1, NUMBER_2);
  }

  @Test
  public void testGivenMethodCallByFindingFirstBatchReturnBatchObject() {

    Optional<Batch> foundBatch =
      batchRepository.findFirstByDeletedFalseOrderByUpdatedAtDesc();

    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get()
                 .getCode()).isEqualTo(NUMBER_2);
  }

  @Test
  public void testGivenBatchNumberByFindingBatchByNumberReturnBatchObject() {

    Optional<Batch> foundBatch = batchRepository.findByCodeAndDeletedFalse(
      NUMBER_1);

    assertThat(foundBatch).isNotEqualTo(Optional.empty());
    assertThat(foundBatch.get()
                 .getCode()).isEqualTo(NUMBER_1);
  }

  @Test
  public void testGivenBatchIdByFindingBatchesReturnListOfBatch() {

    List<Batch> foundBatches = batchRepository.findAllByIdAndDeletedFalse(ID_1);

    assertThat(foundBatches.get(0)
                 .getCode()).isEqualTo(NUMBER_1);
  }

}
