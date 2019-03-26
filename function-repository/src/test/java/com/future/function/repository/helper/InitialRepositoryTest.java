package com.future.function.repository.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.future.function.model.entity.sample.InitialEntity;
import com.future.function.repository.TestApplication;

/**
 * For repository test, we will use embedded MongoDB from de.flapdoodle.embed.mongo.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class InitialRepositoryTest {

  @Autowired
  private InitialRepository initialRepository;

  /**
   * Define setUp before any test, if none required keep it empty.
   */
  @Before
  public void setUp() throws Exception {

    initialRepository.save(InitialEntity.builder()
        .build());
  }

  /**
   * Define tearDown after any test, always empty the database.
   */
  @After
  public void tearDown() throws Exception {

    initialRepository.deleteAll();
  }

  /**
   * Use the following test naming convention:
   * {@code public void testGiven(Condition)By(ExpectedBehavior)Return(Result)}
   * <p>
   * Use Assertions instead of JUnit.
   */
  @Test
  public void testExampleGivenDetailedProperNamingConventionInfoByWritingUnitTestReturnValidTestName() {

    String s = null;
    assertThat(s).isNull();
  }
}
