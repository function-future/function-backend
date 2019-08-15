package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;

import java.util.Map;

public interface UserDetailService {

  User changeProfilePicture(User user);

  User getUserByEmail(String email);

  void changeUserPassword(
    String email, String oldPassword, String newPassword
  );

  Map<String, Object> getSectionsByRole(Role role);

  Map<String, Object> getComponentsByUrlAndRole(String url, Role role);

}
