package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@ChangeLog(order = "001")
public class FunctionChangeLog {
  
  private static final long ONE = 1;
  
  private Batch savedFirstBatch;
  
  @ChangeSet(author = "jonathan",
             id = "batchMigration",
             order = "002")
  public void insertFirstBatch(MongoTemplate mongoTemplate) {
    
    Batch batch = new Batch(ONE);
    
    mongoTemplate.save(batch, DocumentName.BATCH);
    
    savedFirstBatch = mongoTemplate.find(
      query(where("code").is(ONE)), Batch.class)
      .get(0);
  }
  
  @ChangeSet(author = "jonathan",
             id = "studentMigration",
             order = "003")
  public void insertStudent(MongoTemplate mongoTemplate) {
    
    DBRef batchRef = new DBRef(DocumentName.BATCH, savedFirstBatch.getId());
    
    BasicDBObject student = new BasicDBObject();
    student.append("email", "student@student.com");
    student.append(FieldName.User.NAME, "Student");
    student.append(FieldName.User.ROLE, Role.STUDENT);
    student.append(FieldName.User.PASSWORD, "studentfunctionapp");
    student.append(FieldName.User.PHONE, "081212341234");
    student.append(FieldName.User.ADDRESS, "Student Address");
    student.append(FieldName.User.PICTURE, new File());
    student.append(FieldName.User.BATCH, batchRef);
    student.append(FieldName.User.UNIVERSITY, "University");
    student.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    student.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    
    mongoTemplate.insert(student, DocumentName.USER);
  }
  
  @ChangeSet(author = "jonathan",
             id = "adminMigration",
             order = "004")
  public void insertAdmin(MongoTemplate mongoTemplate) {
    
    BasicDBObject admin = new BasicDBObject();
    admin.append(FieldName.User.EMAIL, "admin@admin.com");
    admin.append(FieldName.User.NAME, "Admin Istrator");
    admin.append(FieldName.User.ROLE, Role.ADMIN);
    admin.append(FieldName.User.PASSWORD, "administratorfunctionapp");
    admin.append(FieldName.User.PHONE, "+6281212341234");
    admin.append(FieldName.User.ADDRESS, "Admin Address");
    admin.append(FieldName.User.PICTURE, new File());
    admin.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    admin.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    
    mongoTemplate.insert(admin, DocumentName.USER);
  }
  
  @ChangeSet(author = "jonathan",
             id = "mentorMigration",
             order = "005")
  public void insertMentor(MongoTemplate mongoTemplate) {
    
    BasicDBObject mentor = new BasicDBObject();
    mentor.append(FieldName.User.EMAIL, "mentor@mentor.com");
    mentor.append(FieldName.User.NAME, "Mentor");
    mentor.append(FieldName.User.ROLE, Role.MENTOR);
    mentor.append(FieldName.User.PASSWORD, "mentorfunctionapp");
    mentor.append(FieldName.User.PHONE, "+628121234123");
    mentor.append(FieldName.User.ADDRESS, "Mentor Address");
    mentor.append(FieldName.User.PICTURE, new File());
    mentor.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    mentor.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    
    mongoTemplate.insert(mentor, DocumentName.USER);
  }
  
  @ChangeSet(author = "jonathan",
             id = "judgeMigration",
             order = "006")
  public void insertJudge(MongoTemplate mongoTemplate) {
    
    BasicDBObject judge = new BasicDBObject();
    judge.append(FieldName.User.EMAIL, "judge@judge.com");
    judge.append(FieldName.User.NAME, "Judge");
    judge.append(FieldName.User.ROLE, Role.JUDGE);
    judge.append(FieldName.User.PASSWORD, "judgefunctionapp");
    judge.append(FieldName.User.PHONE, "+62812123412345");
    judge.append(FieldName.User.ADDRESS, "Judge Address");
    judge.append(FieldName.User.PICTURE, new File());
    judge.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    judge.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    
    mongoTemplate.insert(judge, DocumentName.USER);
  }
  
}
