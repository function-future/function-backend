package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.Collections;

@ChangeLog(order = "025")
public class DataMigration_025 {

  // Changed root folder path to conform to latest file/folder
  // paths implementation, which uses `FilePath` embedded object.

  @ChangeSet(author = "jonathan",
             id = "changeRootFolderPath",
             order = "0001")
  public void changeRootFolderPath(MongoDatabase mongoDatabase) {

    Document rootFolderPath = new Document();
    rootFolderPath.put("_id", "root");
    rootFolderPath.put("name", "Root");

    mongoDatabase.getCollection(DocumentName.FILE)
      .findOneAndUpdate(
        Filters.eq("_id", "root"),
        Updates.set("paths", Collections.singletonList(rootFolderPath))
      );
  }

}
