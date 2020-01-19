package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings(value = {
  "squid:S00101", "squid:S1192", "common-java:DuplicatedBlocks"
})
@ChangeLog(order = "006")
public class DataMigration_006 {

  // Insert access list for core.

  @ChangeSet(author = "jonathan",
             id = "userAccessListMigration",
             order = "0001")
  public void insertUserAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/users.*$";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", true);
    adminComponents.put("delete", true);
    adminComponents.put("edit", true);
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

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "announcementAccessListMigration",
             order = "0002")
  public void insertAnnouncementAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/announcements.*$";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", true);
    adminComponents.put("delete", true);
    adminComponents.put("edit", true);
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
    nonAdminComponents.put("read", true);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "stickyNoteAccessListMigration",
             order = "0003")
  public void insertStickyNoteAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/sticky-notes.*$";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", true);
    adminComponents.put("delete", true);
    adminComponents.put("edit", true);
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
    nonAdminComponents.put("read", true);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(nonAdminComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "activityBlogAccessListMigration",
             order = "0004")
  public void insertActivityBlogAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/activity-blogs.*$";

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
    guestComponents.put("read", true);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(guestComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "courseAccessListMigration",
             order = "0005")
  public void insertCourseAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/courses.*$";

    Map<String, Object> nonStudentNorGuestComponents = new HashMap<>();
    nonStudentNorGuestComponents.put("add", true);
    nonStudentNorGuestComponents.put("delete", true);
    nonStudentNorGuestComponents.put("edit", true);
    nonStudentNorGuestComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(nonStudentNorGuestComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(nonStudentNorGuestComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(nonStudentNorGuestComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Map<String, Object> studentOrGuestComponents = new HashMap<>();
    studentOrGuestComponents.put("add", false);
    studentOrGuestComponents.put("delete", false);
    studentOrGuestComponents.put("edit", false);
    studentOrGuestComponents.put("read", false);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(studentOrGuestComponents)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

    Access guestAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.UNKNOWN)
      .components(studentOrGuestComponents)
      .build();

    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "jonathan",
             id = "sharedCourseAccessListMigration",
             order = "0006")
  public void insertSharedCourseAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/batches\\/.+\\/courses.*$";

    Map<String, Object> nonStudentNorGuestComponents = new HashMap<>();
    nonStudentNorGuestComponents.put("add", true);
    nonStudentNorGuestComponents.put("delete", true);
    nonStudentNorGuestComponents.put("edit", true);
    nonStudentNorGuestComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.ADMIN)
      .components(nonStudentNorGuestComponents)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.JUDGE)
      .components(nonStudentNorGuestComponents)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.MENTOR)
      .components(nonStudentNorGuestComponents)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", false);
    studentComponents.put("delete", false);
    studentComponents.put("edit", false);
    studentComponents.put("read", true);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .role(Role.STUDENT)
      .components(studentComponents)
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

  @ChangeSet(author = "jonathan",
             id = "batchAccessListMigration",
             order = "0007")
  public void insertBatchAccessList(MongoTemplate mongoTemplate) {

    String urlRegexGet = "^\\/batches(\\/)?$";
    String urlRegexAdd = "^\\/batches\\/add(\\/)?$";
    String urlRegexEdit = "^\\/batches\\/.+\\/edit(\\/)?$";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", true);
    adminComponents.put("delete", true);
    adminComponents.put("edit", true);
    adminComponents.put("read", true);

    Access adminAccessGet = Access.builder()
      .urlRegex(urlRegexGet)
      .role(Role.ADMIN)
      .components(adminComponents)
      .build();
    Access adminAccessAdd = Access.builder()
      .urlRegex(urlRegexAdd)
      .role(Role.ADMIN)
      .components(adminComponents)
      .build();
    Access adminAccessEdit = Access.builder()
      .urlRegex(urlRegexEdit)
      .role(Role.ADMIN)
      .components(adminComponents)
      .build();

    mongoTemplate.insert(adminAccessGet, DocumentName.ACCESS);
    mongoTemplate.insert(adminAccessAdd, DocumentName.ACCESS);
    mongoTemplate.insert(adminAccessEdit, DocumentName.ACCESS);

    Map<String, Object> nonGuestComponents = new HashMap<>();
    nonGuestComponents.put("add", false);
    nonGuestComponents.put("delete", false);
    nonGuestComponents.put("edit", false);
    nonGuestComponents.put("read", true);

    Access judgeAccessGet = Access.builder()
      .urlRegex(urlRegexGet)
      .role(Role.JUDGE)
      .components(nonGuestComponents)
      .build();
    Access judgeAccessAdd = Access.builder()
      .urlRegex(urlRegexAdd)
      .role(Role.JUDGE)
      .components(nonGuestComponents)
      .build();
    Access judgeAccessEdit = Access.builder()
      .urlRegex(urlRegexEdit)
      .role(Role.JUDGE)
      .components(nonGuestComponents)
      .build();

    mongoTemplate.insert(judgeAccessGet, DocumentName.ACCESS);
    mongoTemplate.insert(judgeAccessAdd, DocumentName.ACCESS);
    mongoTemplate.insert(judgeAccessEdit, DocumentName.ACCESS);

    Access mentorAccessGet = Access.builder()
      .urlRegex(urlRegexGet)
      .role(Role.MENTOR)
      .components(nonGuestComponents)
      .build();
    Access mentorAccessAdd = Access.builder()
      .urlRegex(urlRegexAdd)
      .role(Role.MENTOR)
      .components(nonGuestComponents)
      .build();
    Access mentorAccessEdit = Access.builder()
      .urlRegex(urlRegexEdit)
      .role(Role.MENTOR)
      .components(nonGuestComponents)
      .build();

    mongoTemplate.insert(mentorAccessGet, DocumentName.ACCESS);
    mongoTemplate.insert(mentorAccessAdd, DocumentName.ACCESS);
    mongoTemplate.insert(mentorAccessEdit, DocumentName.ACCESS);

    Access studentAccessGet = Access.builder()
      .urlRegex(urlRegexGet)
      .role(Role.STUDENT)
      .components(nonGuestComponents)
      .build();
    Access studentAccessAdd = Access.builder()
      .urlRegex(urlRegexAdd)
      .role(Role.STUDENT)
      .components(nonGuestComponents)
      .build();
    Access studentAccessEdit = Access.builder()
      .urlRegex(urlRegexEdit)
      .role(Role.STUDENT)
      .components(nonGuestComponents)
      .build();

    mongoTemplate.insert(studentAccessGet, DocumentName.ACCESS);
    mongoTemplate.insert(studentAccessAdd, DocumentName.ACCESS);
    mongoTemplate.insert(studentAccessEdit, DocumentName.ACCESS);

    Map<String, Object> guestComponents = new HashMap<>();
    guestComponents.put("add", false);
    guestComponents.put("delete", false);
    guestComponents.put("edit", false);
    guestComponents.put("read", false);

    Access guestAccessGet = Access.builder()
      .urlRegex(urlRegexGet)
      .role(Role.UNKNOWN)
      .components(guestComponents)
      .build();
    Access guestAccessAdd = Access.builder()
      .urlRegex(urlRegexAdd)
      .role(Role.UNKNOWN)
      .components(guestComponents)
      .build();
    Access guestAccessEdit = Access.builder()
      .urlRegex(urlRegexEdit)
      .role(Role.UNKNOWN)
      .components(guestComponents)
      .build();

    mongoTemplate.insert(guestAccessGet, DocumentName.ACCESS);
    mongoTemplate.insert(guestAccessAdd, DocumentName.ACCESS);
    mongoTemplate.insert(guestAccessEdit, DocumentName.ACCESS);
  }

  @ChangeSet(author = "priagung",
             id = "chatroomAccessListMigration",
             order = "0008")
  public void insertChatroomAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^(\\/m)?\\/chatrooms(\\/(\\w+)?)?$";

    Map<String, Object> nonGuestComponent = new HashMap<>();
    nonGuestComponent.put("add", true);
    nonGuestComponent.put("delete", true);
    nonGuestComponent.put("edit", true);
    nonGuestComponent.put("read", true);

    Map<String, Object> guestComponent = new HashMap<>();
    guestComponent.put("add", false);
    guestComponent.put("delete", false);
    guestComponent.put("edit", false);
    guestComponent.put("read", false);

    Access adminAccess = Access.builder()
      .role(Role.ADMIN)
      .urlRegex(urlRegex)
      .components(nonGuestComponent)
      .build();

    Access judgeAccess = Access.builder()
      .role(Role.JUDGE)
      .components(nonGuestComponent)
      .urlRegex(urlRegex)
      .build();

    Access mentorAccess = Access.builder()
      .role(Role.MENTOR)
      .components(nonGuestComponent)
      .urlRegex(urlRegex)
      .build();

    Access studentAccess = Access.builder()
      .role(Role.STUDENT)
      .components(nonGuestComponent)
      .urlRegex(urlRegex)
      .build();

    Access guestAccess = Access.builder()
      .role(Role.UNKNOWN)
      .components(guestComponent)
      .urlRegex(urlRegex)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);
    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);
    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);
    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "priagung",
             id = "reminderAccessListMigration",
             order = "0009")
  public void insertReminderAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/reminders(\\/.*)?$";

    Map<String, Object> nonAdminComponent = new HashMap<>();
    nonAdminComponent.put("add", false);
    nonAdminComponent.put("delete", false);
    nonAdminComponent.put("edit", false);
    nonAdminComponent.put("read", false);

    Map<String, Object> adminComponent = new HashMap<>();
    adminComponent.put("add", true);
    adminComponent.put("delete", true);
    adminComponent.put("edit", true);
    adminComponent.put("read", true);

    Access adminAccess = Access.builder()
      .role(Role.ADMIN)
      .urlRegex(urlRegex)
      .components(adminComponent)
      .build();

    Access judgeAccess = Access.builder()
      .role(Role.JUDGE)
      .components(nonAdminComponent)
      .urlRegex(urlRegex)
      .build();

    Access mentorAccess = Access.builder()
      .role(Role.MENTOR)
      .components(nonAdminComponent)
      .urlRegex(urlRegex)
      .build();

    Access studentAccess = Access.builder()
      .role(Role.STUDENT)
      .components(nonAdminComponent)
      .urlRegex(urlRegex)
      .build();

    Access guestAccess = Access.builder()
      .role(Role.UNKNOWN)
      .components(nonAdminComponent)
      .urlRegex(urlRegex)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);
    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);
    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);
    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "priagung",
             id = "notificationAccessListMigration",
             order = "0010")
  public void insertNotificationAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/notifications(\\/)?$";

    Map<String, Object> nonGuestComponent = new HashMap<>();
    nonGuestComponent.put("add", true);
    nonGuestComponent.put("delete", true);
    nonGuestComponent.put("edit", true);
    nonGuestComponent.put("read", true);

    Map<String, Object> guestComponent = new HashMap<>();
    guestComponent.put("add", false);
    guestComponent.put("delete", false);
    guestComponent.put("edit", false);
    guestComponent.put("read", false);

    Access adminAccess = Access.builder()
      .role(Role.ADMIN)
      .urlRegex(urlRegex)
      .components(nonGuestComponent)
      .build();

    Access judgeAccess = Access.builder()
      .role(Role.JUDGE)
      .components(nonGuestComponent)
      .urlRegex(urlRegex)
      .build();

    Access mentorAccess = Access.builder()
      .role(Role.MENTOR)
      .components(nonGuestComponent)
      .urlRegex(urlRegex)
      .build();

    Access studentAccess = Access.builder()
      .role(Role.STUDENT)
      .components(nonGuestComponent)
      .urlRegex(urlRegex)
      .build();

    Access guestAccess = Access.builder()
      .role(Role.UNKNOWN)
      .components(guestComponent)
      .urlRegex(urlRegex)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);
    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);
    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);
    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

}
