package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.UnauthorizedException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.DiscussionRepository;
import com.future.function.service.api.feature.core.DiscussionService;
import com.future.function.service.api.feature.core.SharedCourseService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation class for discussion logic operations implementation.
 */
@Service
public class DiscussionServiceImpl implements DiscussionService {
  
  private final DiscussionRepository discussionRepository;
  
  private final UserService userService;
  
  private final SharedCourseService sharedCourseService;
  
  @Autowired
  public DiscussionServiceImpl(
    DiscussionRepository discussionRepository, UserService userService,
    SharedCourseService sharedCourseService
  ) {
    
    this.discussionRepository = discussionRepository;
    this.userService = userService;
    this.sharedCourseService = sharedCourseService;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param courseId  Id of course's discussion to be retrieved.
   * @param batchCode Batch code of target discussion.
   * @param pageable  Pageable object for paging data.
   *
   * @return {@code Page<Discussion>} - Page of discussions found in database.
   */
  @Override
  public Page<Discussion> getDiscussions(
    String email, String courseId, String batchCode, Pageable pageable
  ) {
    
    return Optional.of(email)
      .filter(e -> this.isUserValidForAddingDiscussion(e, batchCode))
      .filter(ignored -> this.isSharedCourseExist(courseId, batchCode))
      .map(
        ignored -> discussionRepository.findAllByCourseIdAndBatchCodeOrderByCreatedAtDesc(
          courseId, batchCode, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param discussion Discussion object to be saved.
   *
   * @return {@code Discussion} - The discussion object of the saved data.
   */
  @Override
  public Discussion createDiscussion(Discussion discussion) {
    
    String email = discussion.getUser()
      .getEmail();
    String courseId = discussion.getCourseId();
    String batchCode = discussion.getBatchCode();
    
    return Optional.of(discussion)
      .filter(ignored -> this.isUserValidForAddingDiscussion(email, batchCode))
      .filter(ignored -> this.isSharedCourseExist(courseId, batchCode))
      .map(this::setDiscussionUser)
      .map(discussionRepository::save)
      .orElseThrow(
        () -> new UnsupportedOperationException("Create Discussion Failed"));
  }
  
  private boolean isUserValidForAddingDiscussion(
    String email, String batchCode
  ) {
    
    User userByEmail = userService.getUserByEmail(email);
    
    if (userByEmail.getRole() != Role.STUDENT) {
      return true;
    }
    
    return Optional.of(userByEmail)
      .map(User::getBatch)
      .map(Batch::getCode)
      .filter(code -> code.equals(batchCode))
      .map(code -> true)
      .orElseThrow(() -> new UnauthorizedException("Invalid Batch"));
  }
  
  private boolean isSharedCourseExist(String courseId, String batchCode) {
    
    return sharedCourseService.getCourseByIdAndBatchCode(courseId, batchCode) !=
           null;
  }
  
  private Discussion setDiscussionUser(Discussion discussion) {
    
    User user = userService.getUserByEmail(discussion.getUser()
                                             .getEmail());
    
    discussion.setUser(user);
    
    return discussion;
  }
  
}
