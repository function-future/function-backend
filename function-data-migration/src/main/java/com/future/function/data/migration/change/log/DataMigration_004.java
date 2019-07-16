package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import static com.future.function.data.migration.constant.IndexName.*;

@SuppressWarnings("squid:S00101")
@ChangeLog(order = "004")
public class DataMigration_004 {
  
  // Add indexes on database for core features.
  
  @ChangeSet(author = "jonathan",
             id = "userIndexes",
             order = "0001")
  public void addUserCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.USER)
      .createIndex(
        Indexes.ascending(USER_EMAIL_DELETED.getFields()),
        new IndexOptions().name(USER_EMAIL_DELETED.name())
      );
    mongoDatabase.getCollection(DocumentName.USER)
      .createIndex(
        Indexes.ascending(USER_ROLE_DELETED.getFields()),
        new IndexOptions().name(USER_ROLE_DELETED.name())
      );
    mongoDatabase.getCollection(DocumentName.USER)
      .createIndex(
        Indexes.ascending(USER_ROLE_BATCH_DELETED.getFields()),
        new IndexOptions().name(USER_ROLE_BATCH_DELETED.name())
      );
    mongoDatabase.getCollection(DocumentName.USER)
      .createIndex(
        Indexes.ascending(USER_NAME_DELETED.getFields()),
        new IndexOptions().name(USER_NAME_DELETED.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "stickyNoteIndexes",
             order = "0002")
  public void addStickyNoteCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.STICKY_NOTE)
      .createIndex(
        Indexes.descending(STICKY_NOTE_ID_UPDATED_AT_DESC.getFields()),
        new IndexOptions().name(STICKY_NOTE_ID_UPDATED_AT_DESC.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "batchIndexes",
             order = "0003")
  public void addBatchCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.BATCH)
      .createIndex(
        Indexes.descending(BATCH_ID_UPDATED_AT_DESC.getFields()),
        new IndexOptions().name(BATCH_ID_UPDATED_AT_DESC.name())
      );
    mongoDatabase.getCollection(DocumentName.BATCH)
      .createIndex(
        Indexes.ascending(BATCH_CODE.getFields()),
        new IndexOptions().name(BATCH_CODE.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "sharedCourseIndexes",
             order = "0004")
  public void addSharedCourseCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.SHARED_COURSE)
      .createIndex(
        Indexes.ascending(SHARED_COURSE_COURSE_ID_BATCH.getFields()),
        new IndexOptions().name(SHARED_COURSE_COURSE_ID_BATCH.name())
      );
    mongoDatabase.getCollection(DocumentName.SHARED_COURSE)
      .createIndex(
        Indexes.ascending(SHARED_COURSE_BATCH.getFields()),
        new IndexOptions().name(SHARED_COURSE_BATCH.name())
      );
    mongoDatabase.getCollection(DocumentName.SHARED_COURSE)
      .createIndex(
        Indexes.ascending(SHARED_COURSE_COURSE_ID.getFields()),
        new IndexOptions().name(SHARED_COURSE_COURSE_ID.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "discussionIndexes",
             order = "0004")
  public void addDiscussionCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.DISCUSSION)
      .createIndex(
        Indexes.descending(
          DISCUSSION_COURSE_ID_BATCH_ID_CREATED_AT.getFields()),
        new IndexOptions().name(DISCUSSION_COURSE_ID_BATCH_ID_CREATED_AT.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "fileIndexes",
             order = "0005")
  public void addFileCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.FILE)
      .createIndex(
        Indexes.ascending(FILE_ID_AS_RESOURCE.getFields()),
        new IndexOptions().name(FILE_ID_AS_RESOURCE.name())
      );
    mongoDatabase.getCollection(DocumentName.FILE)
      .createIndex(
        Indexes.ascending(
          FILE_PARENT_ID_AS_RESOURCE_DELETED_MARK_FOLDER.getFields()),
        new IndexOptions().name(
          FILE_PARENT_ID_AS_RESOURCE_DELETED_MARK_FOLDER.name())
      );
    mongoDatabase.getCollection(DocumentName.FILE)
      .createIndex(
        Indexes.ascending(FILE_PARENT_ID_DELETED.getFields()),
        new IndexOptions().name(FILE_PARENT_ID_DELETED.name())
      );
    mongoDatabase.getCollection(DocumentName.FILE)
      .createIndex(
        Indexes.ascending(FILE_ID_PARENT_ID_DELETED.getFields()),
        new IndexOptions().name(FILE_ID_PARENT_ID_DELETED.name())
      );
    mongoDatabase.getCollection(DocumentName.FILE)
      .createIndex(
        Indexes.ascending(FILE_USED.getFields()),
        new IndexOptions().name(FILE_USED.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "activityBlogIndexes",
             order = "0006")
  public void addActivityBlogCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.ACTIVITY_BLOG)
      .createIndex(
        Indexes.ascending(ACTIVITY_BLOG_TITLE_DESCRIPTION.getFields()),
        new IndexOptions().name(ACTIVITY_BLOG_TITLE_DESCRIPTION.name())
      );
    mongoDatabase.getCollection(DocumentName.ACTIVITY_BLOG)
      .createIndex(
        Indexes.ascending(ACTIVITY_BLOG_USER_ID_TITLE_DESCRIPTION.getFields()),
        new IndexOptions().name(ACTIVITY_BLOG_USER_ID_TITLE_DESCRIPTION.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "menuIndexes",
             order = "0007")
  public void addMenuCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.MENU)
      .createIndex(
        Indexes.ascending(MENU_ROLE.getFields()),
        new IndexOptions().name(MENU_ROLE.name())
      );
  }
  
  @ChangeSet(author = "jonathan",
             id = "accessIndexes",
             order = "0008")
  public void addAccessCollectionIndexes(MongoDatabase mongoDatabase) {
    
    mongoDatabase.getCollection(DocumentName.ACCESS)
      .createIndex(
        Indexes.ascending(ACCESS_ROLE.getFields()),
        new IndexOptions().name(ACCESS_ROLE.name())
      );
  }
  
}
