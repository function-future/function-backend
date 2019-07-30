package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "010")
public class DataMigration_010 {

  @ChangeSet(author = "oliver", id = "questionBankAccessList", order = "0001")
  public void insertQuestionBankAccessList(MongoTemplate mongoTemplate) {

    //TODO need to ask
    String urlRegex = "";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", true);
    adminComponents.put("delete", true);
    adminComponents.put("edit", true);
    adminComponents.put("read", true);

    Map<String, Object> otherComponents = new HashMap<>();
    otherComponents.put("add", false);
    otherComponents.put("delete", false);
    otherComponents.put("edit", false);
    otherComponents.put("read", false);

    Access adminAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(adminComponents)
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
        .components(otherComponents)
        .role(Role.STUDENT)
        .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

  }

  @ChangeSet(author = "oliver", id = "allScoringListAndDetailPageAccessList", order = "0002")
  public void insertAllScoringListAndDetailPageAccessList(MongoTemplate mongoTemplate) {

    //TODO need to ask (quiz, assignment, and final judging pages)
    String urlRegex = "";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", true);
    adminComponents.put("delete", true);
    adminComponents.put("edit", true);
    adminComponents.put("read", true);

    Map<String, Object> otherComponents = new HashMap<>();
    otherComponents.put("add", false);
    otherComponents.put("delete", false);
    otherComponents.put("edit", false);
    otherComponents.put("read", true);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", false);
    studentComponents.put("delete", false);
    studentComponents.put("edit", false);
    studentComponents.put("read", false);

    Access adminAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(adminComponents)
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



  @ChangeSet(author = "oliver", id = "scoringBatchesPageAccessList", order = "0003")
  public void insertScoringBatchesAccessList(MongoTemplate mongoTemplate) {

    //TODO need to ask (all quiz, assignment, final judging batches page)
    String urlRegex = "";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", true);
    adminComponents.put("delete", true);
    adminComponents.put("edit", true);
    adminComponents.put("read", true);

    Map<String, Object> otherComponents = new HashMap<>();
    otherComponents.put("add", false);
    otherComponents.put("delete", false);
    otherComponents.put("edit", false);
    otherComponents.put("read", true);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", false);
    studentComponents.put("delete", false);
    studentComponents.put("edit", false);
    studentComponents.put("read", false);

    Access adminAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(adminComponents)
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

  @ChangeSet(author = "oliver", id = "studentQuizAccessList", order = "0004")
  public void insertStudentQuizAccessList(MongoTemplate mongoTemplate) {

    //TODO need to ask (student quiz pages)
    String urlRegex = "";

    Map<String, Object> otherComponents = new HashMap<>();
    otherComponents.put("add", false);
    otherComponents.put("delete", false);
    otherComponents.put("edit", false);
    otherComponents.put("read", false);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", true);
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

  @ChangeSet(author = "oliver", id = "studentRoomAccessList", order = "0005")
  public void insertStudentRoomAccessList(MongoTemplate mongoTemplate) {

    //TODO need to ask (student quiz pages)
    String urlRegex = "";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("add", false);
    adminComponents.put("delete", false);
    adminComponents.put("edit", false);
    adminComponents.put("read", true);

    Map<String, Object> discussionWithoutScoreComponents = new HashMap<>();
    discussionWithoutScoreComponents.put("score", false);
    discussionWithoutScoreComponents.put("discussion", true);

    Map<String, Object> discussionWithScoreComponents = new HashMap<>();
    discussionWithScoreComponents.put("score", true);
    discussionWithScoreComponents.put("discussion", true);

    Map<String, Object> withScoreComponents = new HashMap<>();
    withScoreComponents.put("add", discussionWithScoreComponents);
    withScoreComponents.put("delete", false);
    withScoreComponents.put("edit", false);
    withScoreComponents.put("read", true);

    Map<String, Object> withoutScoreComponents = new HashMap<>();
    withoutScoreComponents.put("add", discussionWithoutScoreComponents);
    withoutScoreComponents.put("delete", false);
    withoutScoreComponents.put("edit", false);
    withoutScoreComponents.put("read", true);

    Access adminAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(withScoreComponents)
        .role(Role.ADMIN)
        .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(withScoreComponents)
        .role(Role.MENTOR)
        .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(withoutScoreComponents)
        .role(Role.JUDGE)
        .build();

    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

    Access studentAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(withoutScoreComponents)
        .role(Role.STUDENT)
        .build();

    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);

  }

  @ChangeSet(author = "oliver", id = "finalJudgingComparisonAccessList", order = "0006")
  public void insertFinalJudgingComparisonAccessList(MongoTemplate mongoTemplate) {

    //TODO need to ask (student quiz pages)
    String urlRegex = "";

    Map<String, Object> onlyReadComponents = new HashMap<>();
    onlyReadComponents.put("add", false);
    onlyReadComponents.put("delete", false);
    onlyReadComponents.put("edit", false);
    onlyReadComponents.put("read", true);

    Map<String, Object> addAndReadComponents = new HashMap<>();
    addAndReadComponents.put("add", true);
    addAndReadComponents.put("delete", false);
    addAndReadComponents.put("edit", false);
    addAndReadComponents.put("read", true);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", false);
    studentComponents.put("delete", false);
    studentComponents.put("edit", false);
    studentComponents.put("read", false);

    Access adminAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(onlyReadComponents)
        .role(Role.ADMIN)
        .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(onlyReadComponents)
        .role(Role.MENTOR)
        .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(addAndReadComponents)
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

  @ChangeSet(author = "oliver", id = "pointsAccessList", order = "0007")
  public void insertPointsAccessList(MongoTemplate mongoTemplate) {

    //TODO need to ask (student quiz pages)
    String urlRegex = "";

    Map<String, Object> allFalseComponents = new HashMap<>();
    allFalseComponents.put("add", false);
    allFalseComponents.put("delete", false);
    allFalseComponents.put("edit", false);
    allFalseComponents.put("read", false);

    Map<String, Object> studentComponents = new HashMap<>();
    studentComponents.put("add", false);
    studentComponents.put("delete", false);
    studentComponents.put("edit", false);
    studentComponents.put("read", true);

    Access adminAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(allFalseComponents)
        .role(Role.ADMIN)
        .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

    Access mentorAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(allFalseComponents)
        .role(Role.MENTOR)
        .build();

    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

    Access judgeAccess = Access.builder()
        .urlRegex(urlRegex)
        .components(allFalseComponents)
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
