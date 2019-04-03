package com.future.function.repository.feature.user;

import com.future.function.model.entity.feature.user.User;
import com.future.function.common.enumeration.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for user database operations.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
  
  /**
   * Finds specific user by email.
   *
   * @param email - Email of user to be found.
   *
   * @return {@code Optional<User>} - Optional of user found, if any exists;
   * otherwise return {@code Optional.empty()}
   */
  Optional<User> findByEmail(String email);
  
  /**
   * Finds users by role and page data
   * ({@link org.springframework.data.domain.Pageable}).
   *
   * @param role     - Enum of available roles.
   * @param pageable - Pageable object for paging data.
   *
   * @return {@code Page<User>} - Page of users found in database.
   */
  Page<User> findAllByRole(Role role, Pageable pageable);
  
}
