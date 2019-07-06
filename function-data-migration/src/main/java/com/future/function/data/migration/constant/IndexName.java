package com.future.function.data.migration.constant;

import com.future.function.model.util.constant.FieldName;
import lombok.Getter;

@Getter
public enum IndexName {
  USER_EMAIL_DELETED(FieldName.User.EMAIL, FieldName.BaseEntity.DELETED),
  USER_ROLE_DELETED(FieldName.User.ROLE, FieldName.BaseEntity.DELETED),
  USER_ROLE_BATCH_DELETED(FieldName.User.ROLE, FieldName.User.BATCH,
                          FieldName.BaseEntity.DELETED
  ),
  USER_NAME_DELETED(FieldName.User.NAME, FieldName.BaseEntity.DELETED),
  STICKY_NOTE_ID_UPDATED_AT_DESC("_" + FieldName.BaseEntity.ID,
                                 FieldName.BaseEntity.UPDATED_AT
  ),
  BATCH_ID_UPDATED_AT_DESC("_" + FieldName.BaseEntity.ID,
                           FieldName.BaseEntity.UPDATED_AT
  ),
  BATCH_CODE(FieldName.Batch.CODE),
  SHARED_COURSE_COURSE_ID_BATCH(
    FieldName.SharedCourse.COURSE + "." + "_" + FieldName.BaseEntity.ID,
    FieldName.SharedCourse.BATCH
  ),
  SHARED_COURSE_BATCH(FieldName.SharedCourse.BATCH),
  SHARED_COURSE_COURSE_ID(
    FieldName.SharedCourse.COURSE + "." + "_" + FieldName.BaseEntity.ID),
  DISCUSSION_COURSE_ID_BATCH_ID_CREATED_AT(FieldName.Discussion.COURSE_ID,
                                           FieldName.Discussion.BATCH_ID,
                                           FieldName.BaseEntity.CREATED_AT
  ),
  FILE_ID_AS_RESOURCE(
    "_" + FieldName.BaseEntity.ID, FieldName.File.AS_RESOURCE),
  FILE_PARENT_ID_AS_RESOURCE_DELETED_MARK_FOLDER(FieldName.File.PARENT_ID,
                                                 FieldName.File.AS_RESOURCE,
                                                 FieldName.BaseEntity.DELETED,
                                                 FieldName.File.MARK_FOLDER
  ),
  FILE_PARENT_ID_DELETED(
    FieldName.File.PARENT_ID, FieldName.BaseEntity.DELETED),
  FILE_ID_PARENT_ID_DELETED("_" + FieldName.BaseEntity.ID,
                            FieldName.File.PARENT_ID,
                            FieldName.BaseEntity.DELETED
  ),
  FILE_USED(FieldName.File.USED),
  ACTIVITY_BLOG_TITLE_DESCRIPTION(FieldName.ActivityBlog.TITLE,
                                  FieldName.ActivityBlog.DESCRIPTION
  ),
  ACTIVITY_BLOG_USER_ID_TITLE_DESCRIPTION(
    FieldName.ActivityBlog.USER + ".$" + FieldName.BaseEntity.ID,
    FieldName.ActivityBlog.TITLE, FieldName.ActivityBlog.DESCRIPTION
  ),
  MENU_ROLE(FieldName.Menu.ROLE),
  ACCESS_ROLE(FieldName.Access.ROLE);
  
  private String[] fields;
  
  IndexName(String... fields) {
    
    this.fields = fields;
  }
  
}