package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@ChangeLog(order = "013")
public class DataMigration_013 {

  @ChangeSet(author = "oliver",
             id = "studentAssignmentListAccessList",
             order = "0001")
  public void insertStudentAssignmentListAccessList(
    MongoTemplate mongoTemplate
  ) {

    String urlRegex = "^\\/assignments(\\/)?$";

    Map<String, Object> otherComponents = new HashMap<>();
    otherComponents.put("add", false);
    otherComponents.put("delete", false);
    otherComponents.put("edit", false);
    otherComponents.put("read", false);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", false);
    studentComponents.put("delete", false);
    studentComponents.put("edit", false);
    studentComponents.put("read", true);

    Access adminAccess = Access.builder()
      .urlRegex(urlRegex)
      .components(otherComponents)
      .role(Role.ADMIN)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
      .urlRegex(urlRegex)
      .components(otherComponents)
      .role(Role.MENTOR)
      .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
      .urlRegex(urlRegex)
      .components(otherComponents)
      .role(Role.JUDGE)
      .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
      .urlRegex(urlRegex)
      .components(studentComponents)
      .role(Role.STUDENT)
      .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
  }

}
