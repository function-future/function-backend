package com.future.function.service.impl.feature.core;

import com.future.function.common.exception.NotFoundException;
import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.repository.feature.core.ActivityBlogRepository;
import com.future.function.service.api.feature.core.ActivityBlogService;
import com.future.function.service.api.feature.core.ResourceService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
  
  public ActivityBlogServiceImpl(
    ActivityBlogRepository activityBlogRepository,
    ResourceService resourceService
  ) {
    
    this.activityBlogRepository = activityBlogRepository;
    this.resourceService = resourceService;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlogId Id of activity blog to be retrieved.
   *
   * @return {@code ActivityBlog} - The activity blog object found in database.
   */
  @Override
  public ActivityBlog getActivityBlog(
    String activityBlogId
  ) {
    
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
      activityBlogRepository.findAll(this.toExample(userId, search), pageable))
      .orElseGet(() -> new PageImpl<>(Collections.emptyList(), pageable, 0));
  }
  
  private Example<ActivityBlog> toExample(String userId, String search) {
    
    return Example.of(
      this.buildActivityBlogForExample(userId, search),
      this.buildExampleMatcher()
    );
  }
  
  private ExampleMatcher buildExampleMatcher() {
    
    return ExampleMatcher.matchingAny()
      .withIgnoreCase()
      .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
      .withIgnoreNullValues();
  }
  
  private ActivityBlog buildActivityBlogForExample(
    String userId, String search
  ) {
    
    ActivityBlog activityBlog = new ActivityBlog();
    
    Optional.ofNullable(userId)
      .filter(id -> !StringUtils.isEmpty(id))
      .ifPresent(activityBlog::setId);
    
    Optional.ofNullable(search)
      .filter(text -> !StringUtils.isEmpty(text))
      .ifPresent(text -> {
        activityBlog.setTitle(text);
        activityBlog.setDescription(text);
      });
    
    return activityBlog;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlog Activity blog data of new activity blog.
   *
   * @return {@code ActivityBlog} - The activity blog object of the saved data.
   */
  @Override
  public ActivityBlog createActivityBlog(
    ActivityBlog activityBlog
  ) {
    
    return null;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlog Activity blog data of new activity blog.
   *
   * @return {@code ActivityBlog} - The activity blog object of the saved data.
   */
  @Override
  public ActivityBlog updateActivityBlog(
    ActivityBlog activityBlog
  ) {
    
    return null;
  }
  
  /**
   * {@inheritDoc}
   *
   * @param activityBlogId Id of activity blog to be deleted.
   */
  @Override
  public void deleteActivityBlog(String activityBlogId) {
    
    Optional.ofNullable(activityBlogId)
      .map(activityBlogRepository::findOne)
      .filter(Objects::nonNull)
      .ifPresent(activityBlog -> {
        resourceService.markFilesUsed(this.getFileIds(activityBlog), false);
        activityBlogRepository.delete(activityBlog);
      });
  }
  
  private List<String> getFileIds(ActivityBlog activityBlog) {
    
    return activityBlog.getFiles()
      .stream()
      .map(FileV2::getId)
      .collect(Collectors.toList());
  }
  
}
