package com.future.function.service.impl.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.common.exception.ForbiddenException;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.Discussion;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.DiscussionRepository;
import com.future.function.service.api.feature.core.BatchService;
import com.future.function.service.api.feature.core.DiscussionService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiscussionServiceImpl implements DiscussionService {
  
  private final DiscussionRepository discussionRepository;
  
  private final UserService userService;
  
  private final BatchService batchService;
  
  @Autowired
  public DiscussionServiceImpl(
    DiscussionRepository discussionRepository, UserService userService,
    BatchService batchService
  ) {
    
    this.discussionRepository = discussionRepository;
    this.userService = userService;
    this.batchService = batchService;
  }
  
  @Override
  public Page<Discussion> getDiscussions(
    String email, String courseId, String batchCode, Pageable pageable
  ) {
    
    return Optional.of(email)
      .filter(e -> this.isUserValidForAddingDiscussion(e, batchCode))
      .map(ignored -> this.getBatchId(batchCode))
      .map(batchId -> discussionRepository.findAllByCourseIdAndBatchIdOrderByCreatedAtDesc(
        courseId, batchId, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }
  
  @Override
  public Discussion createDiscussion(Discussion discussion) {
    
    String email = discussion.getUser()
      .getEmail();
    String batchCode = discussion.getBatchCode();
    
    return Optional.of(discussion)
      .filter(ignored -> this.isUserValidForAddingDiscussion(email, batchCode))
      .map(this::setDiscussionUser)
      .map(this::setDiscussionBatch)
      .map(discussionRepository::save)
      .orElseThrow(
        () -> new UnsupportedOperationException("Create Discussion Failed"));
  }
  
  private Discussion setDiscussionBatch(Discussion discussion) {
    
    discussion.setBatchId(this.getBatchId(discussion.getBatchCode()));
    
    return discussion;
  }
  
  private String getBatchId(String batchCode) {
    
    return batchService.getBatchByCode(batchCode)
      .getId();
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
      .orElseThrow(() -> new ForbiddenException("Invalid Batch"));
  }
  
  private Discussion setDiscussionUser(Discussion discussion) {
    
    User user = userService.getUserByEmail(discussion.getUser()
                                             .getEmail());
    
    discussion.setUser(user);
    
    return discussion;
  }
  
  @Override
  public void deleteDiscussions(String courseId, String batchCode) {
    
    discussionRepository.deleteAllByCourseIdAndBatchId(courseId,
                                                       this.getBatchId(
                                                         batchCode)
    );
  }
  
}
