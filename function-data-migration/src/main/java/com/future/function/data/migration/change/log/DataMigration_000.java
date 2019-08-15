package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

@SuppressWarnings("squid:S00101")
@ChangeLog(order = "000")
public class DataMigration_000 {

  // Create collections.

  @ChangeSet(author = "jonathan",
             id = "createCollections",
             order = "0001")
  public void createCollections(MongoTemplate mongoTemplate) {

    mongoTemplate.createCollection(DocumentName.BATCH);
    mongoTemplate.createCollection(DocumentName.FILE);
    mongoTemplate.createCollection(DocumentName.USER);
    mongoTemplate.createCollection(DocumentName.COURSE);
    mongoTemplate.createCollection(DocumentName.SHARED_COURSE);
    mongoTemplate.createCollection(DocumentName.ANNOUNCEMENT);
    mongoTemplate.createCollection(DocumentName.STICKY_NOTE);
    mongoTemplate.createCollection(DocumentName.DISCUSSION);
    mongoTemplate.createCollection(DocumentName.ACTIVITY_BLOG);
    mongoTemplate.createCollection(DocumentName.QUESTIONNAIRE);
    mongoTemplate.createCollection(DocumentName.QUESTION_QUESTIONNAIRE);
    mongoTemplate.createCollection(DocumentName.QUESTION_RESPONSE);
    mongoTemplate.createCollection(DocumentName.QUESTION_RESPONSE_SUMMARY);
    mongoTemplate.createCollection(DocumentName.QUESTIONNAIRE_RESPONSE);
    mongoTemplate.createCollection(DocumentName.QUESTIONNAIRE_RESPONSE_SUMMARY);
    mongoTemplate.createCollection(DocumentName.QUESTIONNAIRE_PARTICIPANT);
    mongoTemplate.createCollection(DocumentName.USER_QUESTIONNAIRE_SUMMARY);
    mongoTemplate.createCollection(DocumentName.ASSIGNMENT);
    mongoTemplate.createCollection(DocumentName.QUESTION_BANK);
    mongoTemplate.createCollection(DocumentName.QUIZ);
    mongoTemplate.createCollection(DocumentName.STUDENT_QUIZ);
    mongoTemplate.createCollection(DocumentName.STUDENT_QUIZ_DETAIL);
    mongoTemplate.createCollection(DocumentName.STUDENT_QUESTION);
    mongoTemplate.createCollection(DocumentName.QUESTION);
    mongoTemplate.createCollection(DocumentName.OPTION);
    mongoTemplate.createCollection(DocumentName.CHATROOM);
    mongoTemplate.createCollection(DocumentName.MESSAGE);
    mongoTemplate.createCollection(DocumentName.MESSAGE_STATUS);
    mongoTemplate.createCollection(DocumentName.ACCESS);
    mongoTemplate.createCollection(DocumentName.MENU);
    mongoTemplate.createCollection(DocumentName.ROOM);
    mongoTemplate.createCollection(DocumentName.COMMENT);
    mongoTemplate.createCollection(DocumentName.NOTIFICATION);
    mongoTemplate.createCollection(DocumentName.REMINDER);
    mongoTemplate.createCollection(DocumentName.REPORT);
    mongoTemplate.createCollection(DocumentName.REPORT_DETAIL);
  }

}
