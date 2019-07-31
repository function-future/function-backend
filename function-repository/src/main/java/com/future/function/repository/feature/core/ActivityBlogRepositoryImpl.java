package com.future.function.repository.feature.core;

import com.future.function.model.entity.feature.core.ActivityBlog;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

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
    
    Criteria criteria = this.buildCriteria(userId, search);
    MatchOperation matchOperation = Aggregation.match(criteria);
    
    SortOperation sortOperation = Aggregation.sort(
      new Sort(Sort.Direction.DESC, FieldName.BaseEntity.UPDATED_AT));
    
    SkipOperation skipOperation = new SkipOperation(pageable.getOffset());
    
    LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());
    
    Aggregation dataAggregation = Aggregation.newAggregation(
      sortOperation, matchOperation, skipOperation, limitOperation);
    
    List<ActivityBlog> foundActivityBlogs = mongoTemplate.aggregate(
      dataAggregation, DocumentName.ACTIVITY_BLOG, ActivityBlog.class)
      .getMappedResults();
    
    long size = mongoTemplate.count(
      Query.query(criteria), DocumentName.ACTIVITY_BLOG);
    
    return new PageImpl<>(foundActivityBlogs, pageable, size);
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
