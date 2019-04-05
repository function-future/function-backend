package com.future.function.common.enumeration;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Enum containing available roles.
 */
public enum Role {
  ADMIN,
  JUDGE,
  MENTOR,
  STUDENT,
  UNKNOWN;
  
  public static Role toRole(String name) {
    
    return Optional.ofNullable(name)
      .map(Role::compareWithRoles)
      .orElse(UNKNOWN);
  }
  
  private static Role compareWithRoles(String name) {
    
    return Optional.of(Arrays.stream(Role.values())
                         .map(Enum::name)
                         .collect(Collectors.toList()))
      .filter(roleNames -> roleNames.contains(name))
      .map(roleNames -> Role.valueOf(name))
      .orElse(UNKNOWN);
  }
  
}
