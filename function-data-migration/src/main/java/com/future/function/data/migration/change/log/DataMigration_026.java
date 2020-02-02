package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import static com.future.function.data.migration.constant.IndexName.*;

@ChangeLog(order = "026")
public class DataMigration_026 {

  @ChangeSet(author = "jonathan",
             id = "changeIndexCardinalityOrder",
             order = "0001")
  public void changeIndexCardinalityOrder(MongoDatabase mongoDatabase) {

    mongoDatabase.getCollection(DocumentName.USER)
      .dropIndex(USER_ROLE_NAME_DELETED.name());
    mongoDatabase.getCollection(DocumentName.USER)
      .dropIndex(USER_ROLE_BATCH_DELETED.name());
    mongoDatabase.getCollection(DocumentName.USER)
      .dropIndex(USER_NAME_DELETED.name());

    mongoDatabase.getCollection(DocumentName.USER)
      .createIndex(
        Indexes.ascending(USER_NAME_ROLE_DELETED.getFields()),
        new IndexOptions().name(USER_NAME_ROLE_DELETED.name())
      );
    mongoDatabase.getCollection(DocumentName.USER)
      .createIndex(
        Indexes.ascending(USER_BATCH_ROLE_DELETED.getFields()),
        new IndexOptions().name(USER_BATCH_ROLE_DELETED.name())
      );
  }

}
