package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
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

import java.util.List;

/**
 * Controller class for user APIs.
 */
@RestController
@RequestMapping(value = "/api/core/users")
public class UserController {
  
  private UserRequestMapper userRequestMapper;
  
  private UserService userService;
  
  @Autowired
  public UserController(
    UserService userService, UserRequestMapper userRequestMapper
  ) {
    
    this.userService = userService;
    this.userRequestMapper = userRequestMapper;
  }
  
  /**
   * Creates new user in database.
   *
   * @param data Data of new user in JSON format.
   *
   * @return {@code DataResponse<UserWebResponse>} - The created user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> createUser(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @RequestBody
      UserWebRequest data
  ) {
    
    return UserResponseMapper.toUserDataResponse(
      HttpStatus.CREATED,
      userService.createUser(userRequestMapper.toUser(data))
    );
  }
  
  /**
   * Deletes user from database.
   *
   * @param userId Id of to be deleted user.
   *
   * @return {@code BaseResponse} - Indicating successful deletion.
   */
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
  
  /**
   * Retrieves a user based on given parameter.
   *
   * @param userId Id of user to be retrieved.
   *
   * @return {@code DataResponse<UserWebResponse>} - The retrieved user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{userId:.+}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> getUser(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @PathVariable
      String userId
  ) {
    
    return UserResponseMapper.toUserDataResponse(userService.getUser(userId));
  }
  
  /**
   * Retrieves users based on given parameters.
   *
   * @param role Specified role for data to be retrieved.
   * @param page Current page of data.
   *
   * @return {@code PagingResponse<UserWebResponse>} - The retrieved users data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.PagingResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PagingResponse<UserWebResponse> getUsers(
    @WithAnyRole(roles = Role.ADMIN)
      Session session,
    @RequestParam(required = false)
      String role,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page
  ) {
    
    return UserResponseMapper.toUsersPagingResponse(
      userService.getUsers(Role.toRole(role), PageHelper.toPageable(page, 10)));
  }
  
  /**
   * Updates existing user in database.
   *
   * @param userId Id of to-be-updated user.
   * @param data   Data of existing user in JSON format.
   *
   * @return {@code DataResponse<UserWebResponse>} - The updated user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
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
      userService.updateUser(userRequestMapper.toUser(userId, data)));
  }
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/search")
  public DataResponse<List<UserWebResponse>> getUsersByName(
    @WithAnyRole(roles = { Role.ADMIN, Role.JUDGE, Role.MENTOR, Role.STUDENT })
      Session session,
    @RequestParam(defaultValue = "")
      String name
  ) {
    
    return UserResponseMapper.toUsersDataResponse(
      userService.getUsersByNameContainsIgnoreCase(name));
  }
  
}
