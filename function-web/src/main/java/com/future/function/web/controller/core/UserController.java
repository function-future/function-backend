package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.properties.core.FileProperties;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.session.annotation.WithAnyRole;
import com.future.function.session.model.Session;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.UserRequestMapper;
import com.future.function.web.mapper.response.core.UserResponseMapper;
import com.future.function.web.model.request.core.UserWebRequest;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/core/users")
public class UserController {

  private final FileProperties fileProperties;

  private UserRequestMapper userRequestMapper;

  private UserService userService;

  @Autowired
  public UserController(
    UserService userService, UserRequestMapper userRequestMapper,
    FileProperties fileProperties
  ) {

    this.userService = userService;
    this.userRequestMapper = userRequestMapper;
    this.fileProperties = fileProperties;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> createUser(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @RequestBody
      UserWebRequest data
  ) {

    return UserResponseMapper.toUserDataResponse(HttpStatus.CREATED,
                                                 userService.createUser(
                                                   userRequestMapper.toUser(
                                                     data)),
                                                 fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{userId:.+}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteUser(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String userId
  ) {

    userService.deleteUser(userId);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{userId:.+}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> getUser(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String userId
  ) {

    return UserResponseMapper.toUserDataResponse(
      userService.getUser(userId), fileProperties.getUrlPrefix());
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<UserWebResponse> getUsers(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @RequestParam(required = false)
      String role,
    @RequestParam(defaultValue = "")
      String name,
    @RequestParam(defaultValue = "1")
      int page,
    @RequestParam(defaultValue = "10")
      int size
  ) {

    return UserResponseMapper.toUsersPagingResponse(userService.getUsers(
      Role.toRole(role), name, PageHelper.toPageable(page, size)),
                                                    fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{userId:.+}",
              consumes = MediaType.APPLICATION_JSON_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> updateUser(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String userId,
    @RequestBody
      UserWebRequest data
  ) {

    return UserResponseMapper.toUserDataResponse(
      userService.updateUser(userRequestMapper.toUser(userId, data)),
      fileProperties.getUrlPrefix()
    );
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/_search")
  public PagingResponse<UserWebResponse> getUsersByName(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestParam(defaultValue = "")
      String name,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page,
    @RequestParam(required = false,
                  defaultValue = "10")
      int size
  ) {

    return UserResponseMapper.toUsersPagingResponse(
      userService.getUsersByNameContainsIgnoreCase(name,
                                                   PageHelper.toPageable(page,
                                                                         size
                                                   )
      ), fileProperties.getUrlPrefix());
  }

}
