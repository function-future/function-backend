package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "022")
public class DataMigration_022 {

  @ChangeSet(author = "oliver",
      id = "addScoringAccessList",
      order = "0001")
  public void addScoringAccess(MongoTemplate mongoTemplate) {

    String scoringRegex = "\\/scoring(\\/|.*)?";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("read", true);
    adminComponents.put("create", true);
    adminComponents.put("edit", true);
    adminComponents.put("delete", true);

    Map<String, Object> otherComponents = new HashMap<>();
    otherComponents.put("read", true);
    otherComponents.put("create", false);
    otherComponents.put("edit", false);
    otherComponents.put("delete", false);

    Access adminScoringAccess = Access.builder()
        .role(Role.ADMIN)
        .urlRegex(scoringRegex)
        .components(adminComponents)
        .build();

    Access mentorScoringAccess = Access.builder()
        .role(Role.MENTOR)
        .urlRegex(scoringRegex)
        .components(otherComponents)
        .build();

    Access judgeScoringAccess = Access.builder()
        .role(Role.JUDGE)
        .urlRegex(scoringRegex)
        .components(otherComponents)
        .build();

    Access studentScoringAccess = Access.builder()
        .role(Role.STUDENT)
        .urlRegex(scoringRegex)
        .components(otherComponents)
        .build();

    mongoTemplate.insert(adminScoringAccess, DocumentName.ACCESS);
    mongoTemplate.insert(mentorScoringAccess, DocumentName.ACCESS);
    mongoTemplate.insert(judgeScoringAccess, DocumentName.ACCESS);
    mongoTemplate.insert(studentScoringAccess, DocumentName.ACCESS);
  }

  @ChangeSet(author = "oliver",
      id = "addFinalJudgingAccessList",
      order = "0002")
  public void addFinalJudgingAccess(MongoTemplate mongoTemplate) {
    String finalJudgingRegex = "\\/final-judging(\\/)?";

    Map<String, Object> adminComponents = new HashMap<>();
    adminComponents.put("read", true);
    adminComponents.put("create", true);
    adminComponents.put("edit", true);
    adminComponents.put("delete", true);

    Map<String, Object> studentComponents = new HashMap<>();
    adminComponents.put("read", false);
    adminComponents.put("create", true);
    adminComponents.put("edit", true);
    adminComponents.put("delete", true);

    Map<String, Object> otherComponents = new HashMap<>();
    adminComponents.put("read", true);
    adminComponents.put("create", false);
    adminComponents.put("edit", false);
    adminComponents.put("delete", false);

    Access adminAccess = Access
        .builder()
        .components(adminComponents)
        .role(Role.ADMIN)
        .urlRegex(finalJudgingRegex)
        .build();

    Access mentorAccess = Access.builder()
        .components(otherComponents)
        .role(Role.MENTOR)
        .urlRegex(finalJudgingRegex)
        .build();

    Access judgeAccess = Access.builder()
        .components(otherComponents)
        .role(Role.JUDGE)
        .urlRegex(finalJudgingRegex)
        .build();

    Access studentAccess = Access.builder()
        .components(studentComponents)
        .role(Role.STUDENT)
        .urlRegex(finalJudgingRegex)
        .build();

    mongoTemplate.insert(adminAccess, DocumentName.ACCESS);
    mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);
    mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);
    mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
  }

}
