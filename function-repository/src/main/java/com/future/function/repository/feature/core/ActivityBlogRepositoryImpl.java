package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class ActivityBlogRepositoryImpl
  implements ActivityBlogRepositoryCustom {
  
  private final MongoTemplate mongoTemplate;
  
  @Autowired
  public ActivityBlogRepositoryImpl(MongoTemplate mongoTemplate) {
    
    this.mongoTemplate = mongoTemplate;
  }
  
  @Override
  public Page<ActivityBlog> findAll(
    String userId, String search, Pageable pageable
  ) {
    
    List<ActivityBlog> foundActivityBlogs = mongoTemplate.find(
      query(this.buildCriteria(userId, search)), ActivityBlog.class,
      DocumentName.ACTIVITY_BLOG
    );
    
    return new PageImpl<>(
      foundActivityBlogs, pageable, foundActivityBlogs.size());
  }
  
  private Criteria buildCriteria(String userId, String search) {
    
    Criteria criteria;
    
    if (StringUtils.isEmpty(userId)) {
      criteria = this.buildTitleAndDescriptionCriteria(search);
    } else {
      criteria = Criteria.where(
        FieldName.ActivityBlog.USER + ".$" + FieldName.BaseEntity.ID)
        .is(new ObjectId(userId))
        .andOperator(this.buildTitleAndDescriptionCriteria(search));
    }
    
    return criteria;
  }
  
  private Criteria buildTitleAndDescriptionCriteria(String search) {
    
    String regex = ".*" + search + ".*";
    
    return this.buildRegexCriteria(FieldName.ActivityBlog.TITLE, regex)
      .orOperator(
        this.buildRegexCriteria(FieldName.ActivityBlog.DESCRIPTION, regex));
  }
  
  private Criteria buildRegexCriteria(String fieldName, String regex) {
    
    return Criteria.where(fieldName)
      .regex(regex, "i");
  }
  
}
