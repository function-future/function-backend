package com.future.function.common.data.core;

import java.util.List;

/**
 * Interface for accessing data of object of {@code SharedCourseWebRequest}
 * class type.
 * <p>
 * Useful in validation layer for validation purposes of level {@link
 * java.lang.annotation.ElementType#TYPE}.
 */
public interface CourseData {
  
  /**
   * Method to get batch numbers present in request.
   *
   * @return {@code Long} - Origin batch number in the request. Could be of null
   * value, which would be considered invalid.
   */
  List<Long> getBatchNumbers();
  
}
