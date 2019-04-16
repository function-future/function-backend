package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.Sequence;
import com.future.function.model.entity.feature.core.Batch;
import com.future.function.model.entity.feature.core.File;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.DBRef;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@ChangeLog
public class FunctionChangeLog {
  
  private static final long ONE = 1;
  
  private Batch savedFirstBatch;
  
  @ChangeSet(author = "jonathan",
             id = "sequenceMigration",
             order = "001")
  public void insertFirstSequence(MongoTemplate mongoTemplate) {
    
    Sequence sequence = new Sequence(Batch.SEQUENCE_NAME, ONE);
    
    mongoTemplate.save(sequence, DocumentName.SEQUENCE);
  }
  
  @ChangeSet(author = "jonathan",
             id = "batchMigration",
             order = "002")
  public void insertFirstBatch(MongoTemplate mongoTemplate) {
    
    Batch batch = new Batch(ONE);
    
    mongoTemplate.save(batch, DocumentName.BATCH);
    
    savedFirstBatch = mongoTemplate.find(
      query(where("number").is(ONE)), Batch.class)
      .get(0);
  }
  
  @ChangeSet(author = "jonathan",
             id = "studentMigration",
             order = "003")
  public void insertStudent(MongoTemplate mongoTemplate) {
    
    DBRef batchRef = new DBRef(DocumentName.BATCH, savedFirstBatch.getId());
    
    BasicDBObject student = new BasicDBObject();
    student.append("email", "student@student.com");
    student.append("name", "Student");
    student.append("role", Role.STUDENT);
    student.append("password", "studentfunctionapp");
    student.append("phone", "081212341234");
    student.append("address", "Student Address");
    student.append("picture", new File());
    student.append("batch", batchRef);
    student.append("university", "University");
    student.append("createdAt", System.currentTimeMillis());
    student.append("updatedAt", System.currentTimeMillis());
    
    mongoTemplate.insert(student, DocumentName.USER);
  }
  
  @ChangeSet(author = "jonathan",
             id = "adminMigration",
             order = "004")
  public void insertAdmin(MongoTemplate mongoTemplate) {
    
    BasicDBObject admin = new BasicDBObject();
    admin.append("email", "admin@admin.com");
    admin.append("name", "Admin Istrator");
    admin.append("role", Role.ADMIN);
    admin.append("password", "administratorfunctionapp");
    admin.append("phone", "+6281212341234");
    admin.append("address", "Admin Address");
    admin.append("picture", new File());
    admin.append("createdAt", System.currentTimeMillis());
    admin.append("updatedAt", System.currentTimeMillis());
    
    mongoTemplate.insert(admin, DocumentName.USER);
  }
  
  @ChangeSet(author = "jonathan",
             id = "mentorMigration",
             order = "005")
  public void insertMentor(MongoTemplate mongoTemplate) {
    
    BasicDBObject mentor = new BasicDBObject();
    mentor.append("email", "mentor@mentor.com");
    mentor.append("name", "Mentor");
    mentor.append("role", Role.MENTOR);
    mentor.append("password", "mentorfunctionapp");
    mentor.append("phone", "+628121234123");
    mentor.append("address", "Mentor Address");
    mentor.append("picture", new File());
    mentor.append("createdAt", System.currentTimeMillis());
    mentor.append("updatedAt", System.currentTimeMillis());
    
    mongoTemplate.insert(mentor, DocumentName.USER);
  }
  
  @ChangeSet(author = "jonathan",
             id = "judgeMigration",
             order = "006")
  public void insertJudge(MongoTemplate mongoTemplate) {
    
    BasicDBObject judge = new BasicDBObject();
    judge.append("email", "judge@judge.com");
    judge.append("name", "Judge");
    judge.append("role", Role.JUDGE);
    judge.append("password", "judgefunctionapp");
    judge.append("phone", "+62812123412345");
    judge.append("address", "Judge Address");
    judge.append("picture", new File());
    judge.append("createdAt", System.currentTimeMillis());
    judge.append("updatedAt", System.currentTimeMillis());
    
    mongoTemplate.insert(judge, DocumentName.USER);
  }
  
}
