package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface class for user logic operations declaration.
 */
public interface UserService {

  /**
   * Retrieves a user from database given the users's userId. If not found,
   * then throw {@link com.future.function.common.exception.NotFoundException}
   * exception.
   *
   * @param userId Id of user to be retrieved.
   *
   * @return {@code User} - The user object found in database.
   */
  User getUser(String userId);

  /**
   * Retrieves a user from database given the users's userId. If not found,
   * then throw
   * {@link com.future.function.common.exception.UnauthorizedException}
   * exception.
   *
   * @param email    Email of user to be retrieved.
   * @param password Password of user.
   *
   * @return {@code User} - The user object found in database.
   */
  User getUserByEmailAndPassword(String email, String password);

  /**
   * Retrieves users from database given role.
   *
   * @param role     Role enum of to-be-retrieved users.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<User>} - Page of users found in database.
   */
  Page<User> getUsers(Role role, Pageable pageable);

  /**
   * Retrieves users with STUDENT role from database given batch.
   *
   * @param batchCode code represents the batch of to-be-retrieved students.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<User>} - Page of users found in database.
   */
  Page<User> getStudentsWithinBatch(String batchCode, Pageable pageable);

  /**
   * Creates user object and saves any other data related to the user.
   *
   * @param user User data of new user.
   *
   * @return {@code User} - The user object of the saved data.
   */
  User createUser(User user);

  /**
   * Updates user object and saves any other data related to the user. If not
   * found, then return the request object.
   *
   * @param user User data of existing user.
   *
   * @return {@code User} - The user object of the saved data.
   */
  User updateUser(User user);

  /**
   * Deletes user object from database. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param userId Id of user to be deleted.
   */
  void deleteUser(String userId);

  /**
   * Retrieves users from database given role.
   *
   * @param batchCode Batch code for students.
   *
   * @return {@code List<User>} - List of users found in database.
   */
  List<User> getStudentsByBatchCode(String batchCode);

  /**
   * Retrieves user from database with specified email.
   *
   * @param email Email of user to be retrieved.
   *
   * @return {@code User} - User found in database.
   */
  User getUserByEmail(String email);

  void changeUserPassword(String email, String oldPassword, String newPassword);

  User changeProfilePicture(User user);

  /**
   * Retrieves users from database which contains and case-insensitively
   * matches given name.
   *
   * @param name Part of name of user(s) to be retrieved.
   *
   * @return {@code List<User>} - List of users found in database.
   */
  Page<User> getUsersByNameContainsIgnoreCase(String name, Pageable pageable);

}
