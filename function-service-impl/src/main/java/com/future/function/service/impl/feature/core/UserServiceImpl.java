package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.FileOrigin;
import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.FileService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.ByteArrayHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Service implementation class for user logic operations implementation.
 */
@Service
public class UserServiceImpl implements UserService {
  
  private final BatchService batchService;
  
  private final FileService fileService;
  
  private final UserRepository userRepository;
  
  private final ResourceLoader resourceLoader;
  
  @Autowired
  public UserServiceImpl(
    BatchService batchService, FileService fileService,
    UserRepository userRepository, ResourceLoader webApplicationContext
  ) {
    
    this.batchService = batchService;
    this.fileService = fileService;
    this.userRepository = userRepository;
    this.resourceLoader = webApplicationContext;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param email Email of user to be retrieved.
   *
   * @return {@code User} - The user object found in database.
   */
  @Override
  public User getUser(String email) {
    
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new NotFoundException("Get User Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param role     Role enum of to-be-retrieved users
   * @param pageable Pageable object for paging data
   *
   * @return {@code Page<User>} - Page of users found in database.
   */
  @Override
  public Page<User> getUsers(Role role, Pageable pageable) {
    
    return userRepository.findAllByRole(role, pageable);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param user  User data of new user.
   * @param image Profile image of the new user. May be null, but will be
   *              replaced with default picture.
   *
   * @return {@code User} - The user object of the saved data.
   */
  @Override
  public User createUser(User user, MultipartFile image) {
    
    if (user.getBatch() != null) {
      user.setBatch(batchService.getBatch(user.getBatch()
                                            .getNumber()));
    }
    
    return userRepository.findByEmail(user.getEmail())
      .filter(User::isDeleted)
      .map(foundUser -> markDeleted(foundUser, false))
      .map(foundUser -> copyPropertiesAndSaveUser(user, foundUser))
      .orElseGet(() -> createNewUser(user, image));
  }
  
  private User copyPropertiesAndSaveUser(User user, User foundUser) {
    
    BeanUtils.copyProperties(user, foundUser);
    return userRepository.save(foundUser);
  }
  
  private User createNewUser(User user, MultipartFile image) {
    
    return Optional.of(user)
      .map(this::setDefaultEncryptedPassword)
      .map(newUser -> setUserPicture(newUser, image))
      .map(userRepository::save)
      .orElseGet(() -> userRepository.save(user));
  }
  
  private User setUserPicture(User user, MultipartFile image) {
    
    return Optional.ofNullable(image)
      .map(img -> fileService.storeFile(img, FileOrigin.USER))
      .map(file -> fileService.getFile(file.getId()))
      .map(file -> setUserPicture(user, file))
      .orElseGet(() -> setDefaultUserPicture(user));
  }
  
  private User setUserPicture(User user, File file) {
    
    user.setPicture(file);
    return user;
  }
  
  private User setDefaultUserPicture(User user) {
    
    java.io.File defaultPicture;
    try {
      defaultPicture = resourceLoader.getResource(
        "classpath:default-profile.png")
        .getFile();
    } catch (IOException e) {
      defaultPicture = null;
    }
    
    return Optional.ofNullable(defaultPicture)
      .map(ByteArrayHelper::getBytesFromJavaIoFile)
      .map(bytes -> new MockMultipartFile("default-profile.png",
                                          "default-profile.png",
                                          MediaType.IMAGE_PNG_VALUE, bytes
      ))
      .map(img -> fileService.storeFile(img, FileOrigin.USER))
      .map(file -> fileService.getFile(file.getId()))
      .map(file -> setUserPicture(user, file))
      .orElse(user);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param user  User data of existing user.
   * @param image Profile image of the new user. May be null, but will be
   *              replaced with default picture.
   *
   * @return {@code User} - The user object of the saved data.
   */
  @Override
  public User updateUser(User user, MultipartFile image) {
    
    if (user.getBatch() != null) {
      user.setBatch(batchService.getBatch(user.getBatch()
                                            .getNumber()));
    }
    
    return userRepository.findByEmail(user.getEmail())
      .map(this::deleteUserPicture)
      .map(foundUser -> setUserPicture(user, image))
      .map(foundUser -> copyPropertiesAndSaveUser(user, foundUser))
      .orElseThrow(() -> new NotFoundException("Update User Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param email Email of user to be deleted.
   */
  @Override
  public void deleteUser(String email) {
    
    userRepository.findByEmail(email)
      .map(user -> markDeleted(user, true))
      .orElseThrow(() -> new NotFoundException("Delete User Not Found"));
  }
  
  private User markDeleted(User user, boolean deleted) {
    
    user.setDeleted(deleted);
    
    return userRepository.save(user);
  }
  
  private User setDefaultEncryptedPassword(User user) {
    
    // TODO encrypt password when auth is developed
    return user;
  }
  
  private User deleteUserPicture(User user) {
    
    return Optional.of(user)
      .map(User::getPicture)
      .map(File::getId)
      .map(id -> {
        fileService.deleteFile(id);
        return user;
      })
      .orElse(user);
  }
  
}
