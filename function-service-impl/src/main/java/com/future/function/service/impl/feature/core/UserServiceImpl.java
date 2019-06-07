package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.exception.UnauthorizedException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation class for user logic operations implementation.
 */
@Service
public class UserServiceImpl implements UserService {
  
  private final BatchService batchService;
  
  private final UserRepository userRepository;
  
  private final ResourceService resourceService;
  
  private final BCryptPasswordEncoder encoder;
  
  @Autowired
  public UserServiceImpl(
    BatchService batchService, UserRepository userRepository,
    ResourceService resourceService, BCryptPasswordEncoder encoder
  ) {
    
    this.batchService = batchService;
    this.userRepository = userRepository;
    this.resourceService = resourceService;
    this.encoder = encoder;
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
   * @param email    Email of user to be retrieved.
   * @param password Password of user.
   *
   * @return {@code User} - The user object found in database.
   */
  @Override
  public User getUserByEmailAndPassword(String email, String password) {
  
    return Optional.ofNullable(email)
      .flatMap(userRepository::findByEmail)
      .filter(user -> encoder.matches(password, user.getPassword()))
      .orElseThrow(() -> new UnauthorizedException("Invalid Email/Password"));
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
      .map(this::setDefaultEncryptedPassword)
      .map(this::setUserPicture)
      .map(userRepository::save)
      .orElse(user);
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
      .map(foundUser -> this.setUserPicture(user, foundUser))
      .map(foundUser -> this.copyPropertiesAndSaveUser(user, foundUser))
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
      .ifPresent(this::markDeleted);
  }
  
  /**
   * {@inheritDoc}
   *
   * @param batchCode Batch code for students.
   *
   * @return {@code List<User>} - List of users found in database.
   */
  @Override
  public List<User> getStudentsByBatchCode(
    String batchCode
  ) {
    
    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode)
      .map(batch -> userRepository.findAllByRoleAndBatch(Role.STUDENT, batch))
      .orElseGet(Collections::emptyList);
  }
  
  @Override
  public User getUserByEmail(String email) {
    
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new NotFoundException("Get User Not Found"));
  }
  
  private void markDeleted(User user) {
    
    user.setDeleted(true);
    deleteUserPicture(user);
    userRepository.save(user);
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
      .map(ignored -> foundUser)
      .orElse(foundUser);
  }
  
  private User copyPropertiesAndSaveUser(User user, User foundUser) {
    
    BeanUtils.copyProperties(user, foundUser);
    return userRepository.save(foundUser);
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
    
    user.setPassword(encoder.encode(user.getPassword()));
    return user;
  }
  
}
