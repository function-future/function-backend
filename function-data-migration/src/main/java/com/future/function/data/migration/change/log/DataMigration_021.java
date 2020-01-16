package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog(order = "020")
public class DataMigration_021 {

  @ChangeSet(author = "jonathan",
             id = "changeCoursesAccessList",
             order = "0001")
  public void changeCoursesAccessList(MongoTemplate mongoTemplate) {

    String oldUrlRegex = "^\\/courses.*$";
    Criteria criteria = Criteria.where(FieldName.Access.URL_REGEX)
      .is(oldUrlRegex);

    List<Access> accesses = mongoTemplate.find(
      Query.query(criteria), Access.class);

    String newUrlRegex = "^\\/courses(\\/.*|)$";
    accesses.forEach(access -> {
      access.setUrlRegex(newUrlRegex);
      access.getComponents()
        .forEach((key, value) -> {
          boolean nonGuestNorStudentAccess = !(
            isOfRole(access, Role.UNKNOWN) || isOfRole(access, Role.STUDENT)
          );
          access.getComponents()
            .put(key, nonGuestNorStudentAccess);
        });

      if (isOfRole(access, Role.STUDENT)) {
        access.getComponents()
          .put("read", true);
      }

      mongoTemplate.save(access);
    });
  }

  private boolean isOfRole(Access access, Role role) {

    return access.getRole()
      .equals(role);
  }

  @ChangeSet(author = "jonathan",
             id = "registerMasterCoursesTabAccessList",
             order = "0002")
  public void registerMasterCoursesTabAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/courses\\?tab=master.*$";

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
             id = "registerSharedCoursesTabAccessList",
             order = "0003")
  public void registerSharedCoursesTabAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/courses\\?tab=batch.*$";

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

}
