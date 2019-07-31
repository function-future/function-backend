package com.future.function.data.migration.change.log;

import com.future.function.model.entity.feature.core.Access;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@SuppressWarnings("squid:S00101")
@ChangeLog(order = "011")
public class DataMigration_011 {
  
  @ChangeSet(author = "jonathan",
             id = "changeFilesFoldersUrlRegexAccessListMigration",
             order = "0001")
  public void changeFilesFoldersUrlRegex(MongoTemplate mongoTemplate) {
    
    String oldUrlRegex = "^\\/files(\\/|\\/[A-Za-z0-9\\-]+(\\/)?)?$";
    Criteria criteria = Criteria.where("urlRegex")
      .is(oldUrlRegex);
    List<Access> foundAccesses = mongoTemplate.find(
      Query.query(criteria), Access.class);
    
    String newUrlRegex =
      "^\\/files(\\/|(\\/[A-Za-z0-9\\-]+|\\/[A-Za-z0-9\\-]+\\/[A-Za-z0-9\\-]+)(\\/)?)?$";
    
    foundAccesses.forEach(access -> {
      access.setUrlRegex(newUrlRegex);
      mongoTemplate.save(access);
    });
  }
  
}
