package com.future.function.web.controller;

import com.future.function.common.enumeration.Role;
import com.future.function.service.api.feature.user.UserService;
import com.future.function.web.mapper.helper.PageHelper;
import com.future.function.web.mapper.helper.ResponseHelper;
import com.future.function.web.mapper.request.UserRequestMapper;
import com.future.function.web.mapper.response.UserResponseMapper;
import com.future.function.web.model.response.base.BaseResponse;
import com.future.function.web.model.response.base.DataResponse;
import com.future.function.web.model.response.base.PagingResponse;
import com.future.function.web.model.response.feature.user.UserWebResponse;
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
  
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{email:.+}",
              produces = MediaType.APPLICATION_JSON_VALUE)
  public DataResponse<UserWebResponse> getUser(
    @PathVariable
      String email
  ) {
    
    return UserResponseMapper.toUserDataResponse(userService.getUser(email));
  }
  
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
