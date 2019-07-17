package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;
import com.future.function.service.api.feature.core.AccessService;
import com.future.function.service.api.feature.core.MenuService;
import com.future.function.service.api.feature.core.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailServiceImplTest {
  
  private static final User USER = new User();
  
  @Mock
  private UserService userService;
  
  @Mock
  private MenuService menuService;
  
  @Mock
  private AccessService accessService;
  
  @InjectMocks
  private UserDetailServiceImpl userDetailService;
  
  @Before
  public void setUp() {}
  
  @After
  public void tearDown() {
    
    verifyNoMoreInteractions(userService, menuService, accessService);
  }
  
  @Test
  public void testGivenUserByChangingProfilePictureReturnUpdatedUser() {
    
    when(userService.changeProfilePicture(USER)).thenReturn(USER);
    
    User updatedUser = userDetailService.changeProfilePicture(USER);
    
    assertThat(updatedUser).isNotNull();
    assertThat(updatedUser).isEqualTo(new User());
    
    verify(userService).changeProfilePicture(USER);
    
    verifyZeroInteractions(menuService, accessService);
  }
  
  @Test
  public void testGivenEmailByGettingUserByEmailReturnUser() {
    
    String email = "email";
    
    when(userService.getUserByEmail(email)).thenReturn(USER);
    
    User foundUser = userDetailService.getUserByEmail(email);
    
    assertThat(foundUser).isNotNull();
    assertThat(foundUser).isEqualTo(USER);
    
    verify(userService).getUserByEmail(email);
    
    verifyZeroInteractions(menuService, accessService);
  }
  
  @Test
  public void testGivenEmailAndOldAndNewPasswordByChangingPasswordReturnSuccessfulUpdate() {
    
    String email = "email";
    String oldPassword = "old-password";
    String newPassword = "new-password";
    
    userDetailService.changeUserPassword(email, oldPassword, newPassword);
    
    verify(userService).changeUserPassword(email, oldPassword, newPassword);
    
    verifyZeroInteractions(menuService, accessService);
  }
  
  @Test
  public void testGivenRoleByGettingSectionsByRoleReturnSectionsMap() {
    
    when(menuService.getSectionsByRole(Role.MENTOR)).thenReturn(
      Collections.emptyMap());
    
    Map<String, Object> sections = userDetailService.getSectionsByRole(
      Role.MENTOR);
    
    assertThat(sections).isNotNull();
    assertThat(sections).isEqualTo(Collections.emptyMap());
    
    verify(menuService).getSectionsByRole(Role.MENTOR);
    
    verifyZeroInteractions(userService, accessService);
  }
  
  @Test
  public void testGivenUrlAndRoleByGettingComponentsByUrlAndRoleReturnComponentsMap() {
  
    String url = "url";
    
    when(
      accessService.getComponentsByUrlAndRole(url, Role.MENTOR)).thenReturn(
      Collections.emptyMap());
    
    Map<String, Object> components =
      userDetailService.getComponentsByUrlAndRole(url, Role.MENTOR);
    
    assertThat(components).isNotNull();
    assertThat(components).isEqualTo(Collections.emptyMap());
    
    verify(accessService).getComponentsByUrlAndRole(url, Role.MENTOR);
    
    verifyZeroInteractions(userService, menuService);
  }
  
}