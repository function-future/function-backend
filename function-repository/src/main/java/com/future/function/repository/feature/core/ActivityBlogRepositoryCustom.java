package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityBlogRepositoryCustom {

  Page<ActivityBlog> findAll(String userId, String search, Pageable pageable);

}
