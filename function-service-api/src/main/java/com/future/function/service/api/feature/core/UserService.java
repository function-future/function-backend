package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;
import java.util.Observer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

  User getUser(String userId);

  User getUserByEmailAndPassword(String email, String password);

  Page<User> getUsers(Role role, String name, Pageable pageable);

  Page<User> getStudentsWithinBatch(String batchCode, Pageable pageable);

  User createUser(User user);

  User updateUser(User user);

  void deleteUser(String userId);

  List<User> getStudentsByBatchCode(String batchCode);

  User getUserByEmail(String email);

  void changeUserPassword(String email, String oldPassword, String newPassword);

  User changeProfilePicture(User user);

  Page<User> getUsersByNameContainsIgnoreCase(String name, Pageable pageable);

  void addObserver(Observer observer);

}
