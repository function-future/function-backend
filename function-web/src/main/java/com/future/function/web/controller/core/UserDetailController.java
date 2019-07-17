package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.AccessService;
import com.future.function.service.api.feature.core.MenuService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.UserDetailRequestMapper;
import com.future.function.web.mapper.response.core.UserResponseMapper;
import com.future.function.web.model.request.core.ChangePasswordWebRequest;
import com.future.function.web.model.request.core.ChangeProfilePictureWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/core/user")
public class UserDetailController {

  private final UserService userService;

  private final MenuService menuService;

  private final AccessService accessService;

  private final UserDetailRequestMapper userDetailRequestMapper;

  @Autowired
  public UserDetailController(
    UserService userService, MenuService menuService,
    AccessService accessService, UserDetailRequestMapper userDetailRequestMapper
  ) {

    this.userService = userService;
    this.menuService = menuService;
    this.accessService = accessService;
    this.userDetailRequestMapper = userDetailRequestMapper;
  }
  
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/profile/picture")
  public DataResponse<UserWebResponse> changeProfilePicture(
    @WithAnyRole(roles = { Role.STUDENT, Role.MENTOR, Role.JUDGE, Role.ADMIN })
      Session session,
    @RequestBody
      ChangeProfilePictureWebRequest request
  ) {
  
    return UserResponseMapper.toUserDataResponse(
      userService.changeProfilePicture(
        userDetailRequestMapper.toUser(request, session.getEmail())));
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/profile")
  public DataResponse<UserWebResponse> getProfile(
    @WithAnyRole(roles = { Role.STUDENT, Role.MENTOR, Role.JUDGE, Role.ADMIN })
      Session session
  ) {

    return UserResponseMapper.toUserDataResponse(
      userService.getUserByEmail(session.getEmail()));
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/password")
  public BaseResponse changePassword(
    @WithAnyRole(roles = { Role.STUDENT, Role.MENTOR, Role.JUDGE, Role.ADMIN })
      Session session,
    @RequestBody
      ChangePasswordWebRequest request
  ) {

    Pair<String, String> oldAndNewPasswordPair =
      userDetailRequestMapper.toOldAndNewPasswordPair(request);

    userService.changeUserPassword(session.getEmail(),
                                   oldAndNewPasswordPair.getFirst(),
                                   oldAndNewPasswordPair.getSecond()
    );

    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/menu-list")
  public Map<String, Object> getMenuList(
    @WithAnyRole(noUnauthorized = true)
      Session session
  ) {

    return menuService.getSectionsByRole(session.getRole());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/access-list")
  public Map<String, Object> getAccessList(
    @RequestParam(defaultValue = "")
      String url,
    @WithAnyRole(noUnauthorized = true)
      Session session
  ) {

    return accessService.getComponentsByUrlAndRole(url, session.getRole());
  }

}
