package com.future.function.service.impl.feature.user;

import com.future.function.common.enumeration.FileOrigin;
import com.future.function.common.enumeration.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.file.File;
import com.future.function.model.entity.feature.user.User;
import com.future.function.repository.feature.user.UserRepository;
import com.future.function.service.api.feature.batch.BatchService;
import com.future.function.service.api.feature.file.FileService;
import com.future.function.service.api.feature.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service implementation class for user logic operations implementation.
 */
@Service
public class UserServiceImpl implements UserService {
  
  private final BatchService batchService;
  
  private final FileService fileService;
  
  private final UserRepository userRepository;
  
  @Autowired
  public UserServiceImpl(
    BatchService batchService, FileService fileService,
    UserRepository userRepository
  ) {
    
    this.batchService = batchService;
    this.fileService = fileService;
    this.userRepository = userRepository;
  }
  
  @Override
  public User getUser(String email) {
    
    return userRepository.findByEmail(email)
      .orElseThrow(() -> new NotFoundException("Get User Not Found"));
  }
  
  @Override
  public Page<User> getUsers(Role role, Pageable pageable) {
    
    return userRepository.findAllByRole(role, pageable);
  }
  
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
      .map(newUser -> setUserPicture(newUser, image))
      .map(userRepository::save)
      .orElseGet(() -> userRepository.save(user));
  }
  
  private User setUserPicture(User user, MultipartFile image) {
    
    return Optional.ofNullable(image)
      .map(img -> fileService.storeFile(img, FileOrigin.USER))
      .map(file -> fileService.getFile(file.getId()))
      .map(file -> {
        user.setPicture(file);
        return user;
      })
      .orElse(user);
  }
  
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
  
  @Override
  public void deleteUser(String email) {
    
    Optional<User> targetUser = userRepository.findByEmail(email);
    
    if (!targetUser.isPresent()) {
      throw new NotFoundException("Delete User Not Found");
    } else {
      markDeleted(targetUser.get(), true);
    }
  }
  
  private User markDeleted(User user, boolean deleted) {
    
    user.setDeleted(deleted);
    
    return userRepository.save(user);
  }
  
}
