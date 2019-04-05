package com.future.function.common.enumeration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileOriginTest {
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenMethodCallToCheckIsFileOriginResourceByGettingIsFileOriginResourceReturnBoolean() {
    
    boolean isUserFileOriginAsResource = FileOrigin.USER.isAsResource();
    
    assertThat(isUserFileOriginAsResource).isTrue();
  }
  
  @Test
  public void testGivenMethodCallToGetLowercaseValueOfFileOriginByGettingLowercaseValueReturnLowercaseValue() {
    
    String userLowCaseValue = FileOrigin.USER.lowCaseValue();
    
    assertThat(userLowCaseValue).isNotBlank();
    assertThat(userLowCaseValue).isEqualTo("user");
  }
  
}