package com.future.function.repository.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository class for user database operations.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

  /**
   * Finds specific user by email.
   *
   * @param email Email of user to be found.
   *
   * @return {@code Optional<User>} - Optional of user found, if any exists;
   * otherwise return {@link java.util.Optional#empty()}
   */
  Optional<User> findByEmailAndDeletedFalse(String email);

  /**
   * Finds users by role and page data
   * ({@link org.springframework.data.domain.Pageable}).
   *
   * @param role     Enum of available roles.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<User>} - Page of users found in database.
   */
  Page<User> findAllByRoleAndDeletedFalse(Role role, Pageable pageable);

  /**
   * Finds users by batch, role and page data
   * ({@link org.springframework.data.domain.Pageable}).
   *
   * @param batch    Batch of the users.
   * @param role     Enum of available roles.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<User>} - Page of users found in database.
   */
  Page<User> findAllByBatchAndRoleAndDeletedFalse(Batch batch, Role role, Pageable pageable);

  /**
   * Finds users by role and batch data.
   *
   * @param role  Enum of available roles.
   * @param batch Batch object obtained from batchCode.
   *
   * @return {@code List<User>} - List of users (students) found in database.
   */
  List<User> findAllByRoleAndBatchAndDeletedFalse(Role role, Batch batch);

  /**
   * Finds user by name containing search query and matches search query
   * case-insensitively.
   *
   * @param name Search query of name of user.
   *
   * @return {@code List<User>} - List or users found in database.
   */
  Page<User> findAllByNameContainsIgnoreCaseAndDeletedFalse(String name,
                                                            Pageable pageable);

}
