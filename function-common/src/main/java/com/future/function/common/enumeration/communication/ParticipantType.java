package com.future.function.common.enumeration.communication;

import java.util.Optional;
import java.util.stream.Stream;

public enum ParticipantType {
  APPRAISER,
  APPRAISEE;

  public static ParticipantType fromString(String type) {
    return Optional.of(type)
            .filter(ParticipantType::isNameEqualAnyType)
            .map(ParticipantType::valueOf)
            .get();
  }

  private static boolean isNameEqualAnyType(String name) {
    return Stream.of(ParticipantType.values()).anyMatch(type -> name.equals(type.name()));
  }
}
