package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import static com.future.function.data.migration.constant.IndexName.USER_ROLE_DELETED;
import static com.future.function.data.migration.constant.IndexName.USER_ROLE_NAME_DELETED;

@ChangeLog(order = "017")
public class DataMigration_017 {

  @ChangeSet(author = "jonathan",
             id = "changeIndexForGettingUsers",
             order = "0001")
  public void changeIndexForGettingUsers(MongoDatabase mongoDatabase) {

    mongoDatabase.getCollection(DocumentName.USER)
      .dropIndex(USER_ROLE_DELETED.name());

    mongoDatabase.getCollection(DocumentName.USER)
      .createIndex(
        Indexes.ascending(USER_ROLE_NAME_DELETED.getFields()),
        new IndexOptions().name(USER_ROLE_NAME_DELETED.name())
      );
  }

}
