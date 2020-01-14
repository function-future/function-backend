package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@ChangeLog(order = "008")
public class DataMigration_008 {

  @ChangeSet(author = "Ricky Kennedy",
             id = "questionnaireAccessListMigration",
             order = "0001")
  public void insertQuestionnaireAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^(\\/)questionnaires(\\/)?(.?)*$";

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
      .components(adminComponent)
      .urlRegex(urlRegex)
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

  @ChangeSet(author = "Ricky Kennedy",
             id = "myAccessListMigration",
             order = "0002")
  public void insertMyQuestionnaireAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^(\\/)my-questionnaire(\\/)?(.?)*$$";

    Map<String, Object> nonParticipantComponent = new HashMap<>();
    nonParticipantComponent.put("add", false);
    nonParticipantComponent.put("delete", false);
    nonParticipantComponent.put("edit", false);
    nonParticipantComponent.put("read", false);

    Map<String, Object> participantComponent = new HashMap<>();
    participantComponent.put("add", true);
    participantComponent.put("delete", true);
    participantComponent.put("edit", true);
    participantComponent.put("read", true);

    Access adminAccess = Access.builder()
      .role(Role.ADMIN)
      .components(nonParticipantComponent)
      .urlRegex(urlRegex)
      .build();

    Access judgeAccess = Access.builder()
      .role(Role.JUDGE)
      .components(nonParticipantComponent)
      .urlRegex(urlRegex)
      .build();

    Access mentorAccess = Access.builder()
      .role(Role.MENTOR)
      .components(participantComponent)
      .urlRegex(urlRegex)
      .build();

    Access studentAccess = Access.builder()
      .role(Role.STUDENT)
      .components(participantComponent)
      .urlRegex(urlRegex)
      .build();

    Access guestAccess = Access.builder()
      .role(Role.UNKNOWN)
      .components(nonParticipantComponent)
      .urlRegex(urlRegex)
      .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);
    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);
    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);
    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
    mongoTemplate.insert(guestAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "Ricky Kennedy",
             id = "questionnaireResultsAccessListMigration",
             order = "0003")
  public void insertQuestionnaireResultsAccessList(
    MongoTemplate mongoTemplate
  ) {

    String urlRegex = "^(\\/)questionnaire-results(\\/)?(.?)*$";

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
      .components(adminComponent)
      .urlRegex(urlRegex)
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

}
