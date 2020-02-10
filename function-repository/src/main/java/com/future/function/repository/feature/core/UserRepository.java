package com.future.function.repository.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmailAndDeletedFalse(String email);

  Page<User> findAllByNameContainsIgnoreCaseAndRoleAndDeletedFalseOrderByNameAsc(
    String name, Role role, Pageable pageable
  );

  Page<User> findAllByBatchAndRoleAndDeletedFalse(
    Batch batch, Role role, Pageable pageable
  );

  Page<User> findAllByBatchAndRoleAndNameContainsIgnoreCaseAndDeletedFalseOrderByNameAsc(
    Batch batch, Role role, String name, Pageable pageable
  );

  List<User> findAllByBatchAndRoleAndDeletedFalse(Batch batch, Role role);

  Page<User> findAllByNameContainsIgnoreCaseAndDeletedFalseOrderByNameAsc(
    String name, Pageable pageable
  );

  boolean existsByIdAndRoleAndDeletedFalse(String id, Role role);

}
