package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@ChangeLog(order = "024")
public class DataMigration_024 {

  @ChangeSet(author = "priagung",
          id = "peerAppraisalsAccessList",
          order = "0001")
  public void insertPeerAppraisalsAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/peer-appraisal(\\/)?$";

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
