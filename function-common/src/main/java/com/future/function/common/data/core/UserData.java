package com.future.function.common.data.core;

/**
 * Interface for accessing data of object of {@code UserWebRequest} class type.
 * <p>
 * Useful in validation layer for validation purposes of level {@link
 * java.lang.annotation.ElementType#TYPE}.
 */
public interface UserData {
  
  /**
   * Method to get role of a user web request object, which should be an enum's
   * {@code String} value.
   *
   * @return {@code String} - Any {@code String} value, but is expected to be
   * one among values of
   * {@link com.future.function.common.enumeration.core.Role}.
   */
  String getRole();
  
  /**
   * Method to get batch of a user object, available only for user with role
   * STUDENT.
   *
   * @return {@code Long} - Either batch code or null, depending on
   * whether batch code exists in user web request object or not.
   */
  Long getBatch();
  
  /**
   * Method to get university name of a user web request object, available only
   * for user with role STUDENT.
   *
   * @return {@code String} - {@code String} value of university.
   */
  String getUniversity();
  
}
