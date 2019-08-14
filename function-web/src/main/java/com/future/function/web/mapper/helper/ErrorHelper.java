package com.future.function.web.mapper.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorHelper {

  public static Map<String, List<String>> toErrors(
    Set<ConstraintViolation<?>> violations
  ) {

    Map<String, List<String>> mappedViolations = new HashMap<>();

    violations.forEach(violation -> addToMap(mappedViolations, violation));

    return mappedViolations;
  }

  private static void addToMap(
    Map<String, List<String>> mappedViolations, ConstraintViolation violation
  ) {

    String key = getKey(violation);
    String value = violation.getMessage();

    if (!mappedViolations.containsKey(key)) {
      mappedViolations.put(key, new ArrayList<>());
    }
    mappedViolations.get(key)
      .add(value);
  }

  private static String getKey(ConstraintViolation violation) {

    return Optional.ofNullable(violation.getPropertyPath())
      .map(String::valueOf)
      .filter(s -> !s.equals(""))
      .orElseGet(() -> String.valueOf(violation.getConstraintDescriptor()
                                        .getAttributes()
                                        .get("field")));
  }

}
