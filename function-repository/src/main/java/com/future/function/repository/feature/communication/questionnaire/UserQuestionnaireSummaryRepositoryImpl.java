package com.future.function.repository.feature.communication.questionnaire;

import com.future.function.model.entity.feature.communication.questionnaire.UserQuestionnaireSummary;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.FieldName;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserQuestionnaireSummaryRepositoryImpl
  implements UserQuestionnaireSummaryRepositoryCustom {
  
  private final MongoTemplate mongoTemplate;
  
  public UserQuestionnaireSummaryRepositoryImpl(MongoTemplate mongoTemplate) {
    
    this.mongoTemplate = mongoTemplate;
  }
  
  @Override
  public List<UserQuestionnaireSummary> findAllByUserName(String name) {
    
    List<String> userIds = this.getUserIds(name);
    
    return mongoTemplate.findAll(UserQuestionnaireSummary.class)
      .stream()
      .filter(summary -> this.isUserIdInUserIds(userIds, summary))
      .collect(Collectors.toList());
  }
  
  private boolean isUserIdInUserIds(
    List<String> userIds, UserQuestionnaireSummary summary
  ) {
    return userIds.contains(summary.getAppraisee().getId());
  }
  
  private List<String> getUserIds(String name) {
    
    return mongoTemplate.find(getQuery(name), User.class)
      .stream()
      .map(User::getId)
      .collect(Collectors.toList());
  }
  
  private Query getQuery(String name) {
    
    return Query.query(getRegex(name));
  }
  
  private Criteria getRegex(String name) {
    
    return Criteria.where(FieldName.User.NAME)
      .regex(String.format("*%s*", name));
  }
  
}
