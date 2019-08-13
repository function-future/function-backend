package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import static com.future.function.data.migration.constant.IndexName.ACTIVITY_BLOG_TITLE;
import static com.future.function.data.migration.constant.IndexName.ACTIVITY_BLOG_TITLE_DESCRIPTION;
import static com.future.function.data.migration.constant.IndexName.ACTIVITY_BLOG_USER_ID_TITLE;
import static com.future.function.data.migration.constant.IndexName.ACTIVITY_BLOG_USER_ID_TITLE_DESCRIPTION;

@ChangeLog(order = "014")
public class DataMigration_014 {

  @ChangeSet(author = "jonathan",
             id = "updateActivityBlogIndexesContainingDescription",
             order = "0001")
  public void updateActivityBlogCollectionIndexesContainingDescription(
    MongoDatabase mongoDatabase
  ) {

    mongoDatabase.getCollection(DocumentName.ACTIVITY_BLOG)
      .dropIndex(ACTIVITY_BLOG_TITLE_DESCRIPTION.name());
    mongoDatabase.getCollection(DocumentName.ACTIVITY_BLOG)
      .dropIndex(ACTIVITY_BLOG_USER_ID_TITLE_DESCRIPTION.name());

    mongoDatabase.getCollection(DocumentName.ACTIVITY_BLOG)
      .createIndex(Indexes.ascending(ACTIVITY_BLOG_TITLE.getFields()),
                   new IndexOptions().name(ACTIVITY_BLOG_TITLE.name())
      );
    mongoDatabase.getCollection(DocumentName.ACTIVITY_BLOG)
      .createIndex(Indexes.ascending(ACTIVITY_BLOG_USER_ID_TITLE.getFields()),
                   new IndexOptions().name(ACTIVITY_BLOG_USER_ID_TITLE.name())
      );
  }

}
