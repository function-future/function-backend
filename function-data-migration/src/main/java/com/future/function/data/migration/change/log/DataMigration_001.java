package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ChangeLog(order = "001")
public class DataMigration_001 {
  
  // Insert first admin on app database.
  
  private static final BCryptPasswordEncoder ENCODER =
    new BCryptPasswordEncoder();
  
  private static final String ADMIN_EMAIL = "admin@admin.com";
  
  @ChangeSet(author = "jonathan",
             id = "adminMigration",
             order = "0001")
  public void insertAdmin(MongoTemplate mongoTemplate) {
    
    BasicDBObject admin = new BasicDBObject();
    admin.append(FieldName.User.EMAIL, ADMIN_EMAIL);
    admin.append(FieldName.User.NAME, "Admin Istrator");
    admin.append(FieldName.User.ROLE, Role.ADMIN);
    admin.append(
      FieldName.User.PASS, ENCODER.encode("administratorfunctionapp"));
    admin.append(FieldName.User.PHONE, "+6281212341234");
    admin.append(FieldName.User.ADDRESS, "Admin Address");
    admin.append(FieldName.BaseEntity.CREATED_AT, System.currentTimeMillis());
    admin.append(FieldName.BaseEntity.CREATED_BY, ADMIN_EMAIL);
    admin.append(FieldName.BaseEntity.UPDATED_AT, System.currentTimeMillis());
    admin.append(FieldName.BaseEntity.UPDATED_BY, ADMIN_EMAIL);
    admin.append(FieldName.BaseEntity.VERSION, 0);
    
    mongoTemplate.insert(admin, DocumentName.USER);
  }
  
}
