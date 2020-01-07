package com.future.function.data.migration.change.log;

import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.FieldName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@ChangeLog(order = "018")
public class DataMigration_018 {


  @ChangeSet(author = "jonathan",
             id = "changeUrlRegexForLoginModal",
             order = "0001")
  public void changeUrlRegexForLoginModal(MongoTemplate mongoTemplate) {

    Criteria criteria = Criteria.where(FieldName.Access.URL_REGEX)
      .is("^\\/login(\\/|\\?.*)?$");
    List<Access> accesses = mongoTemplate.find(
      Query.query(criteria), Access.class);

    accesses.forEach(access -> {
      access.setUrlRegex("^(\\/|)\\?auth=login$");
      mongoTemplate.save(access);
    });
  }

}
