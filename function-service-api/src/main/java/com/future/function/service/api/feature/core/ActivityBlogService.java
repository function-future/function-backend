package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.ActivityBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface class for activity blog logic operations declaration.
 */
public interface ActivityBlogService {

  /**
   * Retrieves an activity blog from database given the activity blog's id. If
   * not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param activityBlogId Id of activity blog to be retrieved.
   *
   * @return {@code ActivityBlog} - The activity blog object found in database.
   */
  ActivityBlog getActivityBlog(String activityBlogId);

  /**
   * Retrieves activity blogs from database.
   *
   * @param userId   Id of selected user, so that all blogs from this user can
   *                 be retrieved.
   * @param search   Search query to be searched from database.
   * @param pageable Pageable object for paging data.
   *
   * @return {@code Page<ActivityBlog>} - Page of activity blogs found in
   * database.
   */
  Page<ActivityBlog> getActivityBlogs(
    String userId, String search, Pageable pageable
  );

  /**
   * Creates activity blog object and saves any other data related to the
   * activity blog.
   *
   * @param activityBlog Activity blog data of new activity blog.
   *
   * @return {@code ActivityBlog} - The activity blog object of the saved data.
   */
  ActivityBlog createActivityBlog(ActivityBlog activityBlog);

  /**
   * Updates activity blog object and saves any other data related to the
   * activity blog. If not found, then throw
   * {@link com.future.function.common.exception.NotFoundException} exception.
   *
   * @param activityBlog Activity blog data of new activity blog.
   *
   * @return {@code ActivityBlog} - The activity blog object of the saved data.
   */
  ActivityBlog updateActivityBlog(String userId, Role role,
                                  ActivityBlog activityBlog);

  /**
   * Deletes activity blog object from database.
   *
   * @param userId          Id of current user.
   * @param activityBlogId Id of activity blog to be deleted.
   */
  void deleteActivityBlog(String userId, Role role, String activityBlogId);

}
