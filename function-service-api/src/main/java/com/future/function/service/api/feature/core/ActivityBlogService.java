package com.future.function.service.api.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.ActivityBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityBlogService {

  ActivityBlog getActivityBlog(String activityBlogId);

  Page<ActivityBlog> getActivityBlogs(
    String userId, String search, Pageable pageable
  );

  ActivityBlog createActivityBlog(ActivityBlog activityBlog);

  ActivityBlog updateActivityBlog(String userId, Role role,
                                  ActivityBlog activityBlog);

  void deleteActivityBlog(String userId, Role role, String activityBlogId);

}
