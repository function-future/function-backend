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

  Page<User> findAllByRoleAndNameContainsIgnoreCaseAndDeletedFalse(
    Role role, String name, Pageable pageable
  );

  Page<User> findAllByBatchAndRoleAndDeletedFalse(
    Batch batch, Role role, Pageable pageable
  );

  List<User> findAllByRoleAndBatchAndDeletedFalse(Role role, Batch batch);

  Page<User> findAllByNameContainsIgnoreCaseAndDeletedFalse(
    String name, Pageable pageable
  );

}
