package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Service implementation class for user logic operations implementation.
 */
@Service
public class UserServiceImpl implements UserService {
  
  private final BatchService batchService;
  
  private final UserRepository userRepository;
  
  private final ResourceService resourceService;
  
  @Autowired
  public UserServiceImpl(
    BatchService batchService, UserRepository userRepository,
    ResourceService resourceService
  ) {
    
    this.batchService = batchService;
    this.userRepository = userRepository;
    this.resourceService = resourceService;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param userId Id of user to be retrieved.
   *
   * @return {@code User} - The user object found in database.
   */
  @Override
  public User getUser(String userId) {
    
    return Optional.ofNullable(userId)
      .map(userRepository::findOne)
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
   * @param user User data of new user.
   *
   * @return {@code User} - The user object of the saved data.
   */
  @Override
  public User createUser(User user) {
    
    if (user.getBatch() != null) {
      user.setBatch(batchService.getBatchByCode(user.getBatch()
                                                  .getCode()));
    }
    
    return Optional.of(user)
      .map(User::getId)
      .map(userRepository::findOne)
      .filter(User::isDeleted)
      .map(foundUser -> this.markDeleted(foundUser, false))
      .map(foundUser -> this.copyPropertiesAndSaveUser(user, foundUser))
      .orElseGet(() -> this.createNewUser(user));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param user User data of existing user.
   *
   * @return {@code User} - The user object of the saved data.
   */
  @Override
  public User updateUser(User user) {
    
    if (user.getBatch() != null) {
      user.setBatch(batchService.getBatchByCode(user.getBatch()
                                                  .getCode()));
    }
    
    return Optional.of(user)
      .map(User::getId)
      .map(userRepository::findOne)
      .map(this::deleteUserPicture)
      .map(foundUser -> this.copyPropertiesAndSaveUser(user, foundUser))
      .map(foundUser -> this.setUserPicture(user, foundUser))
      .orElse(user);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param userId Id of user to be deleted.
   */
  @Override
  public void deleteUser(String userId) {
    
    Optional.ofNullable(userId)
      .map(userRepository::findOne)
      .ifPresent(user -> this.markDeleted(user, true));
  }
  
  private User deleteUserPicture(User user) {
    
    return Optional.of(user)
      .map(User::getPictureV2)
      .map(FileV2::getId)
      .map(id -> this.markAndSetUserPicture(user, id, false))
      .orElse(user);
  }
  
  private User setUserPicture(User user, User foundUser) {
    
    return Optional.of(foundUser)
      .map(User::getPictureV2)
      .map(FileV2::getId)
      .map(fileId -> this.markAndSetUserPicture(user, fileId, true))
      .orElse(user);
  }
  
  private User markDeleted(User user, boolean deleted) {
    
    user.setDeleted(deleted);
    
    return userRepository.save(user);
  }
  
  private User copyPropertiesAndSaveUser(User user, User foundUser) {
    
    BeanUtils.copyProperties(user, foundUser);
    return userRepository.save(foundUser);
  }
  
  private User createNewUser(User user) {
    
    return Optional.of(user)
      .map(this::setDefaultEncryptedPassword)
      .map(this::setUserPicture)
      .map(userRepository::save)
      .orElseGet(() -> userRepository.save(user));
  }
  
  private User setUserPicture(User user) {
    
    return Optional.of(user)
      .map(User::getPictureV2)
      .map(FileV2::getId)
      .map(fileId -> this.markAndSetUserPicture(user, fileId, true))
      .orElse(user);
  }
  
  private User markAndSetUserPicture(User user, String fileId, boolean used) {
    
    resourceService.markFilesUsed(Collections.singletonList(fileId), used);
    user.setPictureV2(resourceService.getFile(fileId));
    return user;
  }
  
  private User setDefaultEncryptedPassword(User user) {
    
    // TODO encrypt password when auth is developed
    return user;
  }
  
}
