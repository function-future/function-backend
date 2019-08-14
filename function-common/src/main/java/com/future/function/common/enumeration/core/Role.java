package com.future.function.common.enumeration.core;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Role {
  ADMIN,
  JUDGE,
  MENTOR,
  STUDENT,
  UNKNOWN;
  
  public static Role toRole(String name) {
    
    return Optional.ofNullable(name)
      .filter(Role::isNameEqualsAnyRole)
      .map(Role::valueOf)
      .orElse(UNKNOWN);
  }
  
  private static boolean isNameEqualsAnyRole(String name) {
  
    return !Stream.of(Role.values())
      .filter(role -> name.equals(role.name()))
      .collect(Collectors.toList())
      .isEmpty();
  }
  
}
