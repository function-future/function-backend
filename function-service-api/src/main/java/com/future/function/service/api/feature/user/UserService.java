package com.future.function.service.api.feature.user;

import com.future.function.model.entity.feature.user.User;
import com.future.function.model.util.constant.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface class for user logic operations declaration.
 */
public interface UserService {
  
  User getUser(String email);
  
  Page<User> getUsers(Role role, Pageable pageable);
  
  User createUser(User user, MultipartFile image);
  
  User updateUser(User user, MultipartFile image);
  
  void deleteUser(String email);
  
}
