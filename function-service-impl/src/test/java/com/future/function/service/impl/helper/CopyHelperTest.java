package com.future.function.service.impl.helper;

import com.future.function.model.entity.feature.core.User;
import com.future.function.service.impl.helper.dummy.Dummy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CopyHelperTest {
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenSourceAndTargetByCopyingPropertiesReturnSuccessfulCopy() {
    
    Dummy dummySource = Dummy.builder()
      .id("dummy-source-id")
      .user(User.builder()
              .email("dummy-source-user-email")
              .build())
      .build();
    dummySource.setCreatedAt(1L);
    dummySource.setCreatedBy("dummy-source-user-email");
    dummySource.setUpdatedAt(2L);
    
    Dummy expectedDummyTarget = Dummy.builder()
      .build();
    expectedDummyTarget.setUpdatedAt(2L);
    
    Dummy dummyTarget = new Dummy();
    
    CopyHelper.copyProperties(dummySource, dummyTarget);
    
    assertThat(dummyTarget).isEqualTo(expectedDummyTarget);
  }
  
}
