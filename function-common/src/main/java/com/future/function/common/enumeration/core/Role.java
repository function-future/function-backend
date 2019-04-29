package com.future.function.common.enumeration.core;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enum containing available roles in the application.
 * <p>
 * This enum class is useful in many features, for example in authentication
 * and in user creation. Originally, there are 5 roles: Admin, Judge, Mentor,
 * Student, and Guest. However, since Guest could be treated as users with no
 * role, the role's name is changed to {@code UNKNOWN} for more general
 * purposes, such as invalid role input when creating user.
 */
public enum Role {
  ADMIN,
  JUDGE,
  MENTOR,
  STUDENT,
  UNKNOWN;
  
  /**
   * Converts a String to a specific role. Customized to prevent
   * {@link IllegalStateException} from being thrown when using
   * {@link #valueOf(String)} method. If the given parameter is not
   * recognized as a value of Role enums, then {@link #UNKNOWN} will be
   * returned.
   *
   * @param name The name of the role (in form of String) to be converted
   *             to Role enum value.
   *
   * @return {@link Role} - The Role enum value of the given parameter.
   */
  public static Role toRole(String name) {
    
    return Optional.ofNullable(name)
      .filter(Role::isNameEqualsAnyRole)
      .map(Role::valueOf)
      .orElse(UNKNOWN);
  }
  
  /**
   * Compares whether the given name is equal to any Role enum in this enum
   * class.
   *
   * @param name The name of the role (in form of String) to be converted
   *             to Role enum value.
   *
   * @return {@code boolean} - Result of whether any role is equal to the
   * given parameter.
   */
  private static boolean isNameEqualsAnyRole(String name) {
  
    return !Stream.of(Role.values())
             .filter(role -> name.equals(role.name()))
             .collect(Collectors.toList())
      .isEmpty();
  }
  
}
