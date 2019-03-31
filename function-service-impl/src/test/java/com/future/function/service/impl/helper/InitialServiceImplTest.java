package com.future.function.service.impl.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InitialServiceImplTest {

  @InjectMocks
  private InitialServiceImpl initialService;

  /**
   * Define setUp before any test, if none required keep it empty.
   */
  @Before
  public void setUp() throws Exception {

  }

  /**
   * Define tearDown after any test, if none required keep it empty.
   */
  @After
  public void tearDown() throws Exception {

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
