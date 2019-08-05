package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;


import java.util.HashMap;
import java.util.Map;

/**
 * Author: ricky.kennedy
 * Created At: 9:50 AM 8/5/2019
 */
@ChangeLog(order = "012")
public class DataMigration_012{

  @ChangeSet(author = "ricky kennedy",
             id = "loggingRoomAccessListMigration",
             order = "0001")
  public void insertLoggingRoomAccessList (MongoTemplate mongoTemplate)  {

    String urlRegex = "^\\/logging-rooms.*$";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", false);
    adminComponents.put("delete", false);
    adminComponents.put("edit", false);
    adminComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(adminComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Map<String, Object> nonAdminComponents = new HashMap<>();
    nonAdminComponents.put("add", false);
    nonAdminComponents.put("delete", false);
    nonAdminComponents.put("edit", false);
    nonAdminComponents.put("read", false);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);

    Map<String, Object> mentorComponents = new HashMap<>();
    mentorComponents.put("add", false);
    mentorComponents.put("delete", false);
    mentorComponents.put("edit", true);
    mentorComponents.put("read", true);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(mentorComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", true);
    studentComponents.put("delete", false);
    studentComponents.put("edit", false);
    studentComponents.put("read", true);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(studentComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "ricky kennedy",
    id = "editLoggingRoomAccessListMigration",
    order = "0002")
  public void editLoggingRoomAccessList (MongoTemplate mongoTemplate)  {

    String urlRegex = "^\\/logging-rooms\\/.*\\/_edit$";

    Map<String, Object> mentorComponents = new HashMap<>();
    mentorComponents.put("add", true);
    mentorComponents.put("delete", true);
    mentorComponents.put("edit", true);
    mentorComponents.put("read", true);

    Map<String, Object> nonMentorComponents = new HashMap<>();
    nonMentorComponents.put("add", false);
    nonMentorComponents.put("delete", false);
    nonMentorComponents.put("edit", false);
    nonMentorComponents.put("read", false);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);


    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(mentorComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "ricky kennedy",
    id = "createLoggingRoomAccessListMigration",
    order = "0003")
  public void createLoggingRoomAccessList (MongoTemplate mongoTemplate)  {

    String urlRegex = "^\\/logging-rooms\\/_create$";

    Map<String, Object> mentorComponents = new HashMap<>();
    mentorComponents.put("add", true);
    mentorComponents.put("delete", true);
    mentorComponents.put("edit", true);
    mentorComponents.put("read", true);

    Map<String, Object> nonMentorComponents = new HashMap<>();
    nonMentorComponents.put("add", false);
    nonMentorComponents.put("delete", false);
    nonMentorComponents.put("edit", false);
    nonMentorComponents.put("read", false);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);


    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(mentorComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(nonMentorComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
  }
}
