package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
   * Retrieves users from database given role.
   *
   * @param role     Role enum of to-be-retrieved users.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<User>} - Page of users found in database.
   */
  Page<User> getUsers(Role role, Pageable pageable);
  
  /**
   * Creates user object and saves any other data related to the user.
   *
   * @param user  User data of new user.
   *
   * @return {@code User} - The user object of the saved data.
   */
  User createUser(User user);
  
  /**
   * Updates user object and saves any other data related to the user. If not
   * found, then return the request object.
   *
   * @param user  User data of existing user.
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
  
}
