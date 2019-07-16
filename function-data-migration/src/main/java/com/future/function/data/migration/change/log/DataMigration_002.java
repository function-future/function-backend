package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;

@SuppressWarnings("squid:S00101")
@ChangeLog(order = "002")
public class DataMigration_002 {
  
  // Insert first folder on app database, which will never be deleted.
  
  @ChangeSet(author = "jonathan",
             id = "rootFolderMigration",
             order = "0001")
  public void insertRootFolder(MongoTemplate mongoTemplate) {
    
    BasicDBObject rootFolder = new BasicDBObject();
    rootFolder.append("_" + FieldName.BaseEntity.ID, "root");
    rootFolder.append(FieldName.File.AS_RESOURCE, false);
    rootFolder.append(FieldName.File.MARK_FOLDER, true);
    rootFolder.append(FieldName.File.USED, true);
    rootFolder.append(
      FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    rootFolder.append(FieldName.BaseEntity.CREATED_BY, "admin@admin.com");
    rootFolder.append(
      FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    rootFolder.append(FieldName.BaseEntity.UPDATED_BY, "admin@admin.com");
    rootFolder.append(FieldName.BaseEntity.VERSION, 0);
    
    mongoTemplate.insert(rootFolder, DocumentName.FILE);
  }
  
}
