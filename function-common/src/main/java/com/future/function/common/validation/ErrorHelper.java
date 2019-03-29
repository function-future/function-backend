package com.future.function.common.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class ErrorHelper {

  private static void addToMap(Map<String, List<String>> mappedViolations, ConstraintViolation violation) {

    String key = Optional.ofNullable(violation.getPropertyPath())
        .map(String::valueOf)
        .filter(s -> !s.equals(""))
        .orElse(String.valueOf(violation.getConstraintDescriptor()
            .getAttributes()
            .get("field")));
    String value = violation.getMessage();

    if (!mappedViolations.containsKey(key)) {
      mappedViolations.put(key, new ArrayList<>());
    }
    mappedViolations.get(key)
        .add(value);
  }

  public static Map<String, List<String>> toErrors(Set<ConstraintViolation<?>> violations) {

    Map<String, List<String>> mappedViolations = new HashMap<>();

    violations.forEach(violation -> addToMap(mappedViolations, violation));

    return mappedViolations;
  }

  public static String toProperStatusFormat(String status) {

    return status.toUpperCase()
        .replace(" ", "_");
  }
}
