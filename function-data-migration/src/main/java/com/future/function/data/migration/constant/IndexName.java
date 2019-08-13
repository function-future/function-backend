package com.future.function.data.migration.constant;

import com.future.function.model.util.constant.FieldName;
import lombok.Getter;

@Getter
public enum IndexName {
  USER_EMAIL_DELETED(FieldName.User.EMAIL, FieldName.BaseEntity.DELETED),
  USER_ROLE_DELETED(FieldName.User.ROLE, FieldName.BaseEntity.DELETED),
  USER_ROLE_NAME_DELETED(FieldName.User.ROLE, FieldName.User.NAME,
                         FieldName.BaseEntity.DELETED
  ),
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
  ACTIVITY_BLOG_TITLE(FieldName.ActivityBlog.TITLE),
  ACTIVITY_BLOG_USER_ID_TITLE(
    FieldName.ActivityBlog.USER + ".$" + FieldName.BaseEntity.ID,
    FieldName.ActivityBlog.TITLE
  ),
  MENU_ROLE(FieldName.Menu.ROLE),
  ACCESS_ROLE(FieldName.Access.ROLE),

  ENTITY_DELETED(FieldName.BaseEntity.DELETED),

  STUDENT_QUIZ_USER_ID_AND_QUIZ_ID_AND_DELETED(
    FieldName.StudentQuiz.STUDENT.concat(".")
      .concat("$")
      .concat(FieldName.BaseEntity.ID), FieldName.StudentQuiz.QUIZ.concat(".")
      .concat("_")
      .concat(FieldName.BaseEntity.ID), FieldName.BaseEntity.DELETED),

  STUDENT_QUIZ_USER_ID_AND_DELETED(FieldName.StudentQuiz.STUDENT.concat(".")
                                     .concat("$")
                                     .concat(FieldName.BaseEntity.ID),
                                   FieldName.BaseEntity.DELETED),

  STUDENT_QUIZ_DETAIL_STUDENT_QUIZ_ID_AND_CREATED_AT_AND_DELETED(
    FieldName.StudentQuizDetail.STUDENT_QUIZ.concat(".")
      .concat("$")
      .concat(FieldName.BaseEntity.ID), FieldName.BaseEntity.CREATED_AT,
    FieldName.BaseEntity.DELETED
  ),

  STUDENT_QUESTION_STUDENT_QUIZ_DETAIL_ID_AND_NUMBER(
    FieldName.StudentQuestion.STUDENT_QUIZ_DETAIL.concat(".")
      .concat("$")
      .concat(FieldName.BaseEntity.ID), FieldName.StudentQuestion.NUMBER),

  QUESTION_QUESTION_BANK_ID_AND_DELETED(FieldName.Question.QUESTION_BANK.concat(
    ".")
                                          .concat("$")
                                          .concat(FieldName.BaseEntity.ID),
                                        FieldName.BaseEntity.DELETED),

  OPTION_QUESTION_ID(FieldName.Option.QUESTION.concat(".")
                       .concat("$")
                       .concat(FieldName.BaseEntity.ID)),

  ROOM_ASSIGNMENT_ID_AND_DELETED(FieldName.Room.ASSIGNMENT.concat(".")
                                   .concat("$")
                                   .concat(FieldName.BaseEntity.ID),
                                 FieldName.BaseEntity.DELETED),

  ROOM_ASSIGNMENT_USER_ID_AND_DELETED(FieldName.Room.STUDENT.concat(".")
                                        .concat("$")
                                        .concat(FieldName.BaseEntity.ID),
                                      FieldName.BaseEntity.DELETED),

  DISCUSSION_ROOM_ID_AND_CREATED_AT(FieldName.Comment.ROOM.concat(".")
                                      .concat("$")
                                      .concat(FieldName.BaseEntity.ID),
                                    FieldName.BaseEntity.CREATED_AT),

  REPORT_DETAIL_REPORT_ID_AND_DELETED(FieldName.ReportDetail.REPORT.concat(".")
                                        .concat("$")
                                        .concat(FieldName.BaseEntity.ID),
                                      FieldName.BaseEntity.DELETED),
  REPORT_DETAIL_USER_ID_AND_DELETED(FieldName.ReportDetail.USER.concat(".")
                                      .concat("$")
                                      .concat(FieldName.BaseEntity.ID),
                                    FieldName.BaseEntity.DELETED);


  private String[] fields;

  IndexName(String... fields) {

    this.fields = fields;
  }

}
