package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.User;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SuppressWarnings("squid:S00101")
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

    User admin = new User();
    admin.setEmail(ADMIN_EMAIL);
    admin.setName("Admin Istrator");
    admin.setRole(Role.ADMIN);
    admin.setPassword(ENCODER.encode("administratorfunctionapp"));
    admin.setPhone("+6281212341234");
    admin.setAddress("Admin Address");
    admin.setCreatedAt(System.currentTimeMillis());
    admin.setCreatedBy(ADMIN_EMAIL);
    admin.setUpdatedAt(System.currentTimeMillis());
    admin.setUpdatedBy(ADMIN_EMAIL);
    admin.setVersion(0L);
    admin.setDeleted(false);

    mongoTemplate.insert(admin, DocumentName.USER);
  }

}
