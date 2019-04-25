package com.future.function.web.controller.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.core.UserRequestMapper;
import com.future.function.web.mapper.response.core.UserResponseMapper;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.core.UserWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
   * @param data  Data of new user in JSON format.
   * @param image Profile image of the new user.
   *
   * @return {@code DataResponse<UserWebResponse>} - The created user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> createUser(
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile image
  ) {
    
    return UserResponseMapper.toUserDataResponse(
      HttpStatus.CREATED,
      userService.createUser(userRequestMapper.toUser(data), image)
    );
  }
  
  /**
   * Deletes user from database.
   *
   * @param email Email of to be deleted user.
   *
   * @return {@code BaseResponse} - Indicating successful deletion.
   */
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping(value = "/{email:.+}",
                 produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse deleteUser(
    @PathVariable
      String email
  ) {
    
    userService.deleteUser(email);
    return ResponseHelper.toBaseResponse(HttpStatus.OK);
  }
  
  /**
   * Retrieves a user based on given parameter.
   *
   * @param email Email of user to be retrieved.
   *
   * @return {@code DataResponse<UserWebResponse>} - The retrieved user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{email:.+}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> getUser(
    @PathVariable
      String email
  ) {
    
    return UserResponseMapper.toUserDataResponse(userService.getUser(email));
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
    @RequestParam(required = false)
      String role,
    @RequestParam(required = false,
                  defaultValue = "1")
      int page
  ) {
    
    return UserResponseMapper.toUsersPagingResponse(
      userService.getUsers(Role.toRole(role), PageHelper.toPage(page, 10)));
  }
  
  /**
   * Updates existing user in database.
   *
   * @param email Email of to-be-updated user.
   * @param data  Data of existing user in JSON format.
   * @param image New profile image of the existing user.
   *
   * @return {@code DataResponse<UserWebResponse>} - The updated user data,
   * wrapped in
   * {@link com.future.function.web.model.response.base.DataResponse} and
   * {@link com.future.function.web.model.response.feature.core.UserWebResponse}
   */
  @ResponseStatus(HttpStatus.OK)
  @PutMapping(value = "/{email:.+}",
              consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> updateUser(
    @PathVariable
      String email,
    @RequestParam
      String data,
    @RequestParam(required = false)
      MultipartFile image
  ) {
    
    return UserResponseMapper.toUserDataResponse(
      userService.updateUser(userRequestMapper.toUser(email, data), image));
  }
  
}
