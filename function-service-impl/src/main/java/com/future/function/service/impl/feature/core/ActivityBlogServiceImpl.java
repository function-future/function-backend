package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.entity.feature.core.User;
import com.future.function.repository.feature.core.ActivityBlogRepository;
import com.future.function.service.api.feature.core.ActivityBlogService;
import com.future.function.service.api.feature.core.ResourceService;
import com.future.function.service.api.feature.core.UserService;
import com.future.function.service.impl.helper.CopyHelper;
import com.future.function.service.impl.helper.PageHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation class for activity blog logic operations
 * implementation.
 */
@Service
public class ActivityBlogServiceImpl implements ActivityBlogService {
  
  private final ActivityBlogRepository activityBlogRepository;
  
  private final ResourceService resourceService;
  
  private final UserService userService;
  
  public ActivityBlogServiceImpl(
    ActivityBlogRepository activityBlogRepository,
    ResourceService resourceService, UserService userService
  ) {
    
    this.activityBlogRepository = activityBlogRepository;
    this.resourceService = resourceService;
    this.userService = userService;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlogId Id of activity blog to be retrieved.
   *
   * @return {@code ActivityBlog} - The activity blog object found in database.
   */
  @Override
  public ActivityBlog getActivityBlog(String activityBlogId) {
    
    return Optional.ofNullable(activityBlogId)
      .map(activityBlogRepository::findOne)
      .orElseThrow(() -> new NotFoundException("Get Activity Blog Not Found"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param userId   Id of selected user, so that all blogs from this user can
   *                 be retrieved.
   * @param search   Search query to be searched from database.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<ActivityBlog>} - Page of activity blogs found in
   * database.
   */
  @Override
  public Page<ActivityBlog> getActivityBlogs(
    String userId, String search, Pageable pageable
  ) {
    
    return Optional.ofNullable(
      activityBlogRepository.findAll(userId, search, pageable))
      .orElseGet(() -> PageHelper.empty(pageable));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlog Activity blog data of new activity blog.
   *
   * @return {@code ActivityBlog} - The activity blog object of the saved data.
   */
  @Override
  public ActivityBlog createActivityBlog(ActivityBlog activityBlog) {
    
    return Optional.of(activityBlog)
      .map(this::setUser)
      .map(this::setFileV2s)
      .map(activityBlogRepository::save)
      .orElseThrow(
        () -> new UnsupportedOperationException("Create Activity Blog Failed"));
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlog Activity blog data of new activity blog.
   *
   * @return {@code ActivityBlog} - The activity blog object of the saved data.
   */
  @Override
  public ActivityBlog updateActivityBlog(ActivityBlog activityBlog) {
    
    return Optional.of(activityBlog)
      .map(ActivityBlog::getId)
      .map(activityBlogRepository::findOne)
      .filter(foundActivityBlog -> this.isUserValidForEditing(
        foundActivityBlog.getUser(), activityBlog.getUser()))
      .map(this::deleteActivityBlogFiles)
      .map(
        foundActivityBlog -> this.setFileV2s(foundActivityBlog, activityBlog))
      .map(foundActivityBlog -> this.copyPropertiesAndSaveActivityBlog(
        foundActivityBlog, activityBlog))
      .orElse(activityBlog);
  }
  
  private boolean isUserValidForEditing(
    User foundActivityBlogUser, User requestActivityBlogUser
  ) {
    
    return foundActivityBlogUser.getEmail()
      .equalsIgnoreCase(requestActivityBlogUser.getEmail());
  }
  
  private ActivityBlog copyPropertiesAndSaveActivityBlog(
    ActivityBlog foundActivityBlog, ActivityBlog activityBlog
  ) {
    
    CopyHelper.copyProperties(activityBlog, foundActivityBlog);
    
    return activityBlogRepository.save(foundActivityBlog);
    
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlogId Id of activity blog to be deleted.
   */
  @Override
  public void deleteActivityBlog(String email, String activityBlogId) {
    
    Optional.ofNullable(activityBlogId)
      .map(activityBlogRepository::findOne)
      // .filter(
      //   activityBlog -> email.equalsIgnoreCase(activityBlog.getCreatedBy()))
      .ifPresent(activityBlog -> {
        this.deleteActivityBlogFiles(activityBlog);
        activityBlogRepository.delete(activityBlog);
      });
  }
  
  private ActivityBlog deleteActivityBlogFiles(ActivityBlog activityBlog) {
    
    List<String> existingFileIds = this.getFileIds(activityBlog);
    resourceService.markFilesUsed(existingFileIds, false);
    
    return activityBlog;
  }
  
  private ActivityBlog setFileV2s(
    ActivityBlog foundActivityBlog, ActivityBlog activityBlog
  ) {
    
    List<String> fileIds = this.getFileIds(activityBlog);
    
    activityBlog.setFiles(this.getFileV2s(fileIds));
    
    resourceService.markFilesUsed(fileIds, true);
    
    return foundActivityBlog;
  }
  
  private List<String> getFileIds(ActivityBlog activityBlog) {
    
    return activityBlog.getFiles()
      .stream()
      .map(FileV2::getId)
      .collect(Collectors.toList());
  }
  
  private List<FileV2> getFileV2s(List<String> fileIds) {
    
    return fileIds.stream()
      .map(resourceService::getFile)
      .collect(Collectors.toList());
  }
  
  private ActivityBlog setUser(ActivityBlog activityBlog) {
    
    Optional.of(activityBlog)
      .map(ActivityBlog::getUser)
      .map(User::getEmail)
      .map(userService::getUserByEmail)
      .ifPresent(activityBlog::setUser);
    
    return activityBlog;
  }
  
  private ActivityBlog setFileV2s(ActivityBlog activityBlog) {
    
    List<String> fileIds = this.getFileIds(activityBlog);
    
    activityBlog.setFiles(this.getFileV2s(fileIds));
    
    resourceService.markFilesUsed(fileIds, true);
    
    return activityBlog;
  }
  
}
