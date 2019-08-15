package com.future.function.data.migration.change.log;

import com.future.function.model.entity.feature.core.FileV2;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

@SuppressWarnings("squid:S00101")
@ChangeLog(order = "002")
public class DataMigration_002 {

  // Insert first folder on app database, which will never be deleted.

  @ChangeSet(author = "jonathan",
             id = "rootFolderMigration",
             order = "0001")
  public void insertRootFolder(MongoTemplate mongoTemplate) {

    FileV2 rootFolder = new FileV2();
    rootFolder.setId("root");
    rootFolder.setName("Root");
    rootFolder.setAsResource(false);
    rootFolder.setMarkFolder(true);
    rootFolder.setUsed(true);
    rootFolder.setCreatedAt(System.currentTimeMillis());
    rootFolder.setCreatedBy("admin@admin.com");
    rootFolder.setUpdatedAt(System.currentTimeMillis());
    rootFolder.setUpdatedBy("admin@admin.com");
    rootFolder.setDeleted(false);
    rootFolder.setVersion(0L);

    mongoTemplate.insert(rootFolder, DocumentName.FILE);

    FileV2 savedRootFolder = mongoTemplate.findById("root", FileV2.class);

    savedRootFolder.setPaths(Collections.singletonList(rootFolder));

    mongoTemplate.save(savedRootFolder);
  }

}
