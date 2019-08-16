package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

@ChangeLog(order = "015")
public class DataMigration_015 {

  @ChangeSet(author = "jonathan",
             id = "changeFieldFileToFilesInAnnouncementCollection",
             order = "0001")
  public void changeFieldFileToFilesInAnnouncementCollection(
    MongoDatabase mongoDatabase
  ) {

    Bson filter = Filters.or(
      Filters.exists("file"), Filters.not(Filters.eq("file", null)));

    mongoDatabase.getCollection(DocumentName.ANNOUNCEMENT)
      .find(filter)
      .iterator()
      .forEachRemaining(document -> setFiles(mongoDatabase, document));
  }

  private void setFiles(
    MongoDatabase mongoDatabase, Document document
  ) {

    document.append("files", document.get("file"));
    document.remove("file");

    mongoDatabase.getCollection(DocumentName.ANNOUNCEMENT)
      .replaceOne(Filters.eq("_id", document.get("_id")), document);
  }

}
