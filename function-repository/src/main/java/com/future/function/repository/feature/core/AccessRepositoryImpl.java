package com.future.function.repository.feature.core;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class AccessRepositoryImpl implements AccessRepositoryCustom {
  
  private final MongoTemplate mongoTemplate;
  
  public AccessRepositoryImpl(MongoTemplate mongoTemplate) {
    
    this.mongoTemplate = mongoTemplate;
  }
  
  @Override
  public Optional<Access> findByUrlAndRole(String url, Role role) {
    
    List<Access> accesses = mongoTemplate.find(query(
      Criteria.where(FieldName.Access.ROLE)
        .is(role)), Access.class, DocumentName.ACCESS);
    
    return accesses.stream()
      .filter(a -> url.matches(a.getUrlRegex()))
      .findFirst();
  }
  
}
