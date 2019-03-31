package com.future.function.repository.feature.user;

import com.future.function.model.entity.feature.user.User;
import com.future.function.model.util.constant.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  
  Optional<User> findByEmail(String email);
  
  Page<User> findAllByRole(Role role, Pageable pageable);
  
}
