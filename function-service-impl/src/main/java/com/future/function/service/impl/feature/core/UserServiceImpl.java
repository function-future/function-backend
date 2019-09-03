package com.future.function.service.impl.feature.core;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.NotFoundException;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.UserRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.MailService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.api.feature.scoring.ScoringMediatorService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import java.util.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends Observable implements UserService {

  private final BatchService batchService;

  private final UserRepository userRepository;

  private final ResourceService resourceService;

  private final BCryptPasswordEncoder encoder;

  private final MailService mailService;

  @Autowired
  public UserServiceImpl(BatchService batchService, UserRepository userRepository, ResourceService resourceService,
      BCryptPasswordEncoder encoder, MailService mailService) {

    this.batchService = batchService;
    this.userRepository = userRepository;
    this.resourceService = resourceService;
    this.encoder = encoder;
    this.mailService = mailService;
  }

  @Override
  public User getUser(String userId) {

    return Optional.ofNullable(userId)
      .map(userRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Get User Not Found"));
  }

  @Override
  public User getUserByEmailAndPassword(String email, String password) {

    return Optional.ofNullable(email)
      .flatMap(userRepository::findByEmailAndDeletedFalse)
      .filter(user -> encoder.matches(password, user.getPassword()))
      .orElseThrow(() -> new UnauthorizedException("Invalid Email/Password"));
  }

  @Override
  public Page<User> getUsers(Role role, String name, Pageable pageable) {

    return userRepository.findAllByRoleAndNameContainsIgnoreCaseAndDeletedFalse(
      role, name, pageable);
  }

  @Override
  public Page<User> getStudentsWithinBatch(
    String batchCode, Pageable pageable
  ) {

    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode)
      .map(batch -> userRepository.findAllByBatchAndRoleAndDeletedFalse(batch,
                                                                        Role.STUDENT,
                                                                        pageable
      ))
      .orElseGet(() -> PageHelper.empty(pageable));
  }

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
      .map(this::sendEmail)
      .orElseThrow(
        () -> new UnsupportedOperationException("Failed Create User"));
  }

  private User sendEmail(User user) {

    mailService.sendEmail(user.getEmail(), "Registrasi Sukses", String.format(
        "Hi %s,\n\nMelalui email ini kami ingin mengabarkan bahwa Anda sudah terdaftar sebagai %s di aplikasi " +
            "manajemen FUTURE Program.\n\nBerikut informasi kredensial Anda untuk login:\n\tEmail %s\n\tPassword " +
            "%s\n\nUntuk link login dapat diakses di http://localhost:10001.\n\nTerima kasih!\n\n\n\nFunction App",
        user.getName(), user.getRole(), user.getEmail(), this.getDefaultPassword(user.getName())));

    return user;
  }

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
      .map(foundUser -> this.setUserPassword(user, foundUser))
      .map(foundUser -> this.copyPropertiesAndSaveUser(user, foundUser))
      .orElse(user);
  }

  private User setUserPassword(User user, User foundUser) {

    String password = Optional.of(foundUser)
      .filter(u -> !encoder.matches(this.getDefaultPassword(u.getName()),
                                    u.getPassword()
      ))
      .map(User::getPassword)
      .orElseGet(() -> encoder.encode(this.getDefaultPassword(user.getName())));

    user.setPassword(password);

    return foundUser;
  }

  @Override
  public void deleteUser(String userId) {

    Optional.ofNullable(userId)
      .map(userRepository::findOne)
      .ifPresent(this::markDeletedAndNotifyObserver);
  }

  @Override
  public List<User> getStudentsByBatchCode(
    String batchCode
  ) {

    return Optional.ofNullable(batchCode)
      .map(batchService::getBatchByCode)
      .map(batch -> userRepository.findAllByRoleAndBatchAndDeletedFalse(
        Role.STUDENT, batch))
      .orElseGet(Collections::emptyList);
  }

  @Override
  public User getUserByEmail(String email) {

    return userRepository.findByEmailAndDeletedFalse(email)
      .orElseThrow(() -> new NotFoundException("Get User Not Found"));
  }

  @Override
  @SuppressWarnings("squid:S2201")
  public void changeUserPassword(
    String email, String oldPassword, String newPassword
  ) {

    userRepository.findByEmailAndDeletedFalse(email)
      .filter(user -> encoder.matches(oldPassword, user.getPassword()))
      .map(user -> this.setEncryptedPassword(user, newPassword))
      .map(userRepository::save)
      .orElseThrow(() -> new UnauthorizedException("Invalid Old Password"));
  }

  @Override
  public User changeProfilePicture(User user) {

    return userRepository.findByEmailAndDeletedFalse(user.getEmail())
      .map(foundUser -> this.setUserPicture(user, foundUser))
      .map(userRepository::save)
      .orElse(user);
  }

  @Override
  public Page<User> getUsersByNameContainsIgnoreCase(
    String name, Pageable pageable
  ) {

    return userRepository.findAllByNameContainsIgnoreCaseAndDeletedFalse(name,
                                                                         pageable
    );
  }

  private void markDeletedAndNotifyObserver(User user) {

    user.setDeleted(true);
    deleteUserPicture(user);
    this.setChanged();
    this.notifyObservers(userRepository.save(user));
  }

  private User setUserPicture(User user, User foundUser) {

    return Optional.of(user)
      .map(User::getPictureV2)
      .map(FileV2::getId)
      .map(fileId -> this.markAndSetUserPicture(foundUser, fileId, true))
      .map(ignored -> foundUser)
      .orElse(foundUser);
  }

  private User copyPropertiesAndSaveUser(User user, User foundUser) {

    CopyHelper.copyProperties(user, foundUser);
    return userRepository.save(foundUser);
  }

  private User deleteUserPicture(User user) {

    return Optional.of(user)
      .map(User::getPictureV2)
      .map(FileV2::getId)
      .map(id -> this.markAndSetUserPicture(user, id, false))
      .orElse(user);
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

    return this.setEncryptedPassword(
      user, this.getDefaultPassword(user.getName()));
  }

  private String getDefaultPassword(String name) {

    return Optional.ofNullable(name)
      .map(String::toLowerCase)
      .map(n -> n.replace(" ", ""))
      .map(n -> n.concat("functionapp"))
      .orElse(null);
  }

  private User setEncryptedPassword(User user, String password) {

    user.setPassword(encoder.encode(password));
    return user;
  }

}
