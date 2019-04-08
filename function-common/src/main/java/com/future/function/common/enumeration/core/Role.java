package com.future.function.common.enumeration.core;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    
    return Optional.of(Role.values())
      .map(Role::toListValues)
      .filter(roleNames -> roleNames.contains(name))
      .map(roleNames -> Role.valueOf(name))
      .orElse(UNKNOWN);
  }
  
  private static List<String> toListValues(Role[] values) {
    
    return Stream.of(values)
           .map(Enum::name)
           .collect(Collectors.toList());
  }
  
}
