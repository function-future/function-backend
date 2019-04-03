package com.future.function.service.impl.feature.user;

import com.future.function.common.enumeration.FileOrigin;
import com.future.function.common.enumeration.Role;
import com.future.function.common.exception.NotFoundException;
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
    
    user.setPicture(fileService.storeFile(image, FileOrigin.USER));
    
    userRepository.save(user);
    
    return user;
  }
  
  @Override
  public User updateUser(User user, MultipartFile image) {
    
    userRepository.findByEmail(user.getEmail())
      .map(foundUser -> {
        resetUserPicture(user, image, foundUser);
        BeanUtils.copyProperties(user, foundUser);
        return userRepository.save(foundUser);
      })
      .orElseThrow(() -> new NotFoundException("Update User Not Found"));
    
    
    return userRepository.findByEmail(user.getEmail())
      .orElse(null);
  }
  
  private void resetUserPicture(User user, MultipartFile image, User foundUser) {
    
    fileService.deleteFile(foundUser.getPicture()
                             .getId());
    user.setPicture(fileService.storeFile(image, FileOrigin.USER));
  }
  
  @Override
  public void deleteUser(String email) {
    
    Optional<User> targetUser = userRepository.findByEmail(email);
    
    if (!targetUser.isPresent()) {
      throw new NotFoundException("Delete User Not Found");
    } else {
      targetUser.get()
        .setDeleted(true);
    }
  }
  
}
