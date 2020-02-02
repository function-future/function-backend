package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

@ChangeLog(order = "027")
public class DataMigration_027 {

  @ChangeSet(author = "jonathan",
             id = "allowRedirectSuffixAfterAuthLogin",
             order = "0001")
  public void allowRedirectSuffixAfterAuthLogin(MongoDatabase mongoDatabase) {

    mongoDatabase.getCollection(DocumentName.ACCESS)
      .updateMany(
        Filters.eq("urlRegex", "^(\\/|)\\?auth=login$"),
        Updates.set("urlRegex", "^(\\/|)\\?auth=login.*$")
      );
  }

}
