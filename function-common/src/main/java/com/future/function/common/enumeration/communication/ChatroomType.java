package com.future.function.common.enumeration.communication;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: PriagungSatyagama
 * Created At: 21:52 30/05/2019
 */
public enum ChatroomType {
  PUBLIC,
  PRIVATE,
  GROUP;

  public static ChatroomType fromString(String name) {
    return Optional.ofNullable(name)
            .filter(ChatroomType::isNameEqualAnyType)
            .map(ChatroomType::valueOf)
            .orElse(ChatroomType.PUBLIC);
  }

  private static boolean isNameEqualAnyType(String name) {
    return Stream.of(ChatroomType.values()).anyMatch(type -> name.equals(type.name()));
  }
}
