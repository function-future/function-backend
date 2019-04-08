package com.future.function.common.enumeration.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {}
  
  @Test
  public void testGivenProperRoleStringByConvertingStringToRoleReturnProperRoleAsRole() {
    
    Role adminRole = Role.toRole("ADMIN");
    
    assertThat(adminRole).isNotNull();
    assertThat(adminRole).isEqualTo(Role.ADMIN);
    
    Role judgeRole = Role.toRole("JUDGE");
    
    assertThat(judgeRole).isNotNull();
    assertThat(judgeRole).isEqualTo(Role.JUDGE);
    
    Role mentorRole = Role.toRole("MENTOR");
    
    assertThat(mentorRole).isNotNull();
    assertThat(mentorRole).isEqualTo(Role.MENTOR);
    
    Role studentRole = Role.toRole("STUDENT");
    
    assertThat(studentRole).isNotNull();
    assertThat(studentRole).isEqualTo(Role.STUDENT);
  }
  
  @Test
  public void testGivenImproperRoleStringByConvertingStringToRoleReturnProperRoleUnknown() {
    
    Role nullRole = Role.toRole(null);
    
    assertThat(nullRole).isNotNull();
    assertThat(nullRole).isEqualTo(Role.UNKNOWN);
    
    Role unknownRole = Role.toRole("ANY ROLE");
    
    assertThat(unknownRole).isNotNull();
    assertThat(unknownRole).isEqualTo(Role.UNKNOWN);
  }
  
}
