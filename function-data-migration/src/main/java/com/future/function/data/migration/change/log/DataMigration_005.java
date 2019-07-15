package com.future.function.data.migration.change.log;

import com.future.function.model.entity.feature.scoring.Question;
import com.future.function.model.entity.feature.scoring.QuestionBank;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "005")
public class DataMigration_005 {

  private static final String ADMIN_EMAIL = "admin@admin.com";
  private static final String QUESTION_BANK_TITLE = "Question Bank #1";
  private static final String QUESTION_BANK_DESCRIPTION = "Question Bank Description #1";
  private static final String QUESTION_LABEL = "Question #1";
  private static final String OPTION_lABEL_PREFIX = "Option #";

  @ChangeSet(author = "oliver", order = "001", id = "questionBankMigration")
  public void questionBankMigration(MongoTemplate mongoTemplate) {
    BasicDBObject questionBank = new BasicDBObject();
    questionBank.append(FieldName.QuestionBank.TITLE, QUESTION_BANK_TITLE);
    questionBank.append(FieldName.QuestionBank.DESCRIPTION, QUESTION_BANK_DESCRIPTION);
    questionBank.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    questionBank.append(FieldName.BaseEntity.CREATED_BY, ADMIN_EMAIL);
    questionBank.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    questionBank.append(FieldName.BaseEntity.UPDATED_BY, ADMIN_EMAIL);
    questionBank.append(FieldName.BaseEntity.VERSION, 0);
    questionBank.append(FieldName.BaseEntity.DELETED, false);
    mongoTemplate.insert(questionBank, DocumentName.QUESTION_BANK);
  }

  @ChangeSet(author = "oliver", order = "002", id = "questionMigration")
  public void questionMigration(MongoTemplate mongoTemplate) {
    BasicDBObject question = new BasicDBObject();
    String questionBankId = mongoTemplate.findAll(QuestionBank.class).get(0).getId();
    question.append(FieldName.Question.QUESTION_BANK, new DBRef(DocumentName.QUESTION_BANK, new ObjectId(questionBankId)));
    question.append(FieldName.Question.LABEL, QUESTION_LABEL);
    question.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    question.append(FieldName.BaseEntity.CREATED_BY, ADMIN_EMAIL);
    question.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    question.append(FieldName.BaseEntity.UPDATED_BY, ADMIN_EMAIL);
    question.append(FieldName.BaseEntity.VERSION, 0);
    question.append(FieldName.BaseEntity.DELETED, false);
    mongoTemplate.insert(question, DocumentName.QUESTION);
  }

  @ChangeSet(author = "oliver", order = "003", id = "optionMigration")
  public void optionMigration(MongoTemplate mongoTemplate) {
    String questionId = mongoTemplate.findAll(Question.class).get(0).getId();
    for (int i = 0; i < 4; i++) {
      BasicDBObject option = new BasicDBObject();
      option.append(FieldName.Option.QUESTION, new DBRef(DocumentName.QUESTION, new ObjectId(questionId)));
      option.append(FieldName.Option.LABEL, OPTION_lABEL_PREFIX.concat(String.valueOf(i + 1)));
      option.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
      option.append(FieldName.BaseEntity.CREATED_BY, ADMIN_EMAIL);
      option.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
      option.append(FieldName.BaseEntity.UPDATED_BY, ADMIN_EMAIL);
      option.append(FieldName.BaseEntity.VERSION, 0);
      option.append(FieldName.BaseEntity.DELETED, false);
      if (i == 0) {
        option.append(FieldName.Option.CORRECT, true);
      } else {
        option.append(FieldName.Option.CORRECT, false);
      }
      mongoTemplate.save(option, DocumentName.OPTION);
    }
  }
}
