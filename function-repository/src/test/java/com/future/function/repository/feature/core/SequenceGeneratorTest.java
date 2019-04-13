package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.Sequence;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.repository.TestApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class SequenceGeneratorTest {
  
  private static final String SEQUENCE_NAME = "test-sequence";
  
  private static final long SEQUENCE_NUMBER = 2;
  
  @Autowired
  private MongoOperations mongoOperations;
  
  @Autowired
  private SequenceGenerator sequenceGenerator;
  
  @Before
  public void setUp() {
    
    Sequence sequence = Sequence.builder()
      .id(SEQUENCE_NAME)
      .sequenceNumber(SEQUENCE_NUMBER)
      .build();
    
    mongoOperations.save(sequence, DocumentName.SEQUENCE);
  }
  
  @After
  public void tearDown() {
    
    mongoOperations.dropCollection(DocumentName.SEQUENCE);
  }
  
  @Test
  public void testGivenMethodCallToIncrementSequenceByIncrementingSequenceReturnIncrementedResult() {
    
    long incrementedSequence = sequenceGenerator.increment(SEQUENCE_NAME);
    
    assertThat(incrementedSequence).isEqualTo(SEQUENCE_NUMBER + 1);
  }
  
}
