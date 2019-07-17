package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.AccessService;
import com.future.function.service.api.feature.core.MenuService;
import com.future.function.service.api.feature.core.UserDetailService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserDetailServiceImpl implements UserDetailService {
  
  private final UserService userService;
  
  private final MenuService menuService;
  
  private final AccessService accessService;
  
  public UserDetailServiceImpl(
    UserService userService, MenuService menuService,
    AccessService accessService
  ) {
    
    this.userService = userService;
    this.menuService = menuService;
    this.accessService = accessService;
  }
  
  @Override
  public User changeProfilePicture(User user) {
    
    return userService.changeProfilePicture(user);
  }
  
  @Override
  public User getUserByEmail(String email) {
    
    return userService.getUserByEmail(email);
  }
  
  @Override
  public void changeUserPassword(
    String email, String oldPassword, String newPassword
  ) {
    
    userService.changeUserPassword(email, oldPassword, newPassword);
  }
  
  @Override
  public Map<String, Object> getSectionsByRole(Role role) {
    
    return menuService.getSectionsByRole(role);
  }
  
  @Override
  public Map<String, Object> getComponentsByUrlAndRole(String url, Role role) {
    
    return accessService.getComponentsByUrlAndRole(url, role);
  }
  
}
