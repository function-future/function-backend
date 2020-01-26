package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("squid:S00101")
@ChangeLog(order = "009")
public class DataMigration_009 {

  // Add more access list for core features.

  @ChangeSet(author = "jonathan",
             id = "homeAccessListMigration",
             order = "0001")
  public void insertHomeAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^(|\\/)$";

    Map<String, Object> homeComponents = new HashMap<>();
    homeComponents.put("add", true);
    homeComponents.put("delete", true);
    homeComponents.put("edit", true);
    homeComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(homeComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(homeComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(homeComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(homeComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(homeComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "loginAccessListMigration",
             order = "0002")
  public void insertLoginAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/login(\\/|\\?.*)?$";

    Map<String, Object> loginComponents = new HashMap<>();
    loginComponents.put("add", true);
    loginComponents.put("delete", false);
    loginComponents.put("edit", true);
    loginComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(loginComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(loginComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(loginComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(loginComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(loginComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "profileAndChangePasswordAccessListMigration",
             order = "0003")
  public void insertProfileAndChangePasswordAccessList(
    MongoTemplate mongoTemplate
  ) {

    String urlRegex = "^\\/profile(\\/|\\/change-password(\\/)?)?$";

    Map<String, Object> profileOrChangePasswordComponents = new HashMap<>();
    profileOrChangePasswordComponents.put("add", true);
    profileOrChangePasswordComponents.put("delete", false);
    profileOrChangePasswordComponents.put("edit", true);
    profileOrChangePasswordComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(profileOrChangePasswordComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(profileOrChangePasswordComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(profileOrChangePasswordComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(profileOrChangePasswordComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(profileOrChangePasswordComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "filesFoldersAccessListMigration",
             order = "0004")
  public void insertFilesFoldersAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/files(\\/|\\/[A-Za-z0-9\\-]+(\\/)?)?$";

    Map<String, Object> authorizedUserComponents = new HashMap<>();
    authorizedUserComponents.put("add", true);
    authorizedUserComponents.put("delete", true);
    authorizedUserComponents.put("edit", true);
    authorizedUserComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(authorizedUserComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(authorizedUserComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(authorizedUserComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(authorizedUserComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Map<String, Object> guestComponents = new HashMap<>();
    guestComponents.put("add", false);
    guestComponents.put("delete", false);
    guestComponents.put("edit", false);
    guestComponents.put("read", false);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(guestComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

}
