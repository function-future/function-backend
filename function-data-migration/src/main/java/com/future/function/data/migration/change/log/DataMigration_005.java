package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Menu;
import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings(value = { "squid:S00101", "squid:S1192" })
@ChangeLog(order = "005")
public class DataMigration_005 {

  // Insert menu list for core and scoring.

  @ChangeSet(author = "jonathan",
             id = "menuListMigration",
             order = "0001")
  public void insertMenuList(MongoTemplate mongoTemplate) {

    Map<String, Object> adminSections = new HashMap<>();
    adminSections.put("courses", true);
    adminSections.put("files", true);
    adminSections.put("users", true);
    adminSections.put("questionBanks", true);
    adminSections.put("quizzes", true);
    adminSections.put("assignments", true);
    adminSections.put("comparisons", true);
    adminSections.put("points", false);
    adminSections.put("chatrooms", true);
    adminSections.put("reminders", true);
    adminSections.put("myQuestionnaire", false);
    adminSections.put("questionnaireAdmin", true);


    Menu adminMenu = Menu.builder()
      .role(Role.ADMIN)
      .sections(adminSections)
      .build();

    mongoTemplate.insert(adminMenu, DocumentName.MENU);

    Map<String, Object> judgeSections = new HashMap<>();
    judgeSections.put("courses", true);
    judgeSections.put("files", true);
    judgeSections.put("users", false);
    judgeSections.put("questionBanks", false);
    judgeSections.put("quizzes", false);
    judgeSections.put("assignments", true);
    judgeSections.put("comparisons", true);
    judgeSections.put("points", false);
    judgeSections.put("chatrooms", true);
    judgeSections.put("reminders", false);
    judgeSections.put("myQuestionnaire", false);
    judgeSections.put("questionnaireAdmin", false);

    Menu judgeMenu = Menu.builder()
      .role(Role.JUDGE)
      .sections(judgeSections)
      .build();

    mongoTemplate.insert(judgeMenu, DocumentName.MENU);

    Map<String, Object> mentorSections = new HashMap<>();
    mentorSections.put("courses", true);
    mentorSections.put("files", true);
    mentorSections.put("users", false);
    mentorSections.put("questionBanks", false);
    mentorSections.put("quizzes", false);
    mentorSections.put("assignments", true);
    mentorSections.put("comparisons", true);
    mentorSections.put("points", false);
    mentorSections.put("chatrooms", true);
    mentorSections.put("reminders", false);
    mentorSections.put("myQuestionnaire", true);
    mentorSections.put("questionnaireAdmin", false);

    Menu mentorMenu = Menu.builder()
      .role(Role.MENTOR)
      .sections(mentorSections)
      .build();

    mongoTemplate.insert(mentorMenu, DocumentName.MENU);

    Map<String, Object> studentSections = new HashMap<>();
    studentSections.put("courses", true);
    studentSections.put("files", true);
    studentSections.put("users", false);
    studentSections.put("questionBanks", false);
    studentSections.put("quizzes", true);
    studentSections.put("assignments", true);
    studentSections.put("comparisons", false);
    studentSections.put("points", true);
    studentSections.put("chatrooms", true);
    studentSections.put("reminders", false);
    studentSections.put("myQuestionnaire", true);
    studentSections.put("questionnaireAdmin", false);

    Menu studentMenu = Menu.builder()
      .role(Role.STUDENT)
      .sections(studentSections)
      .build();

    mongoTemplate.insert(studentMenu, DocumentName.MENU);

    Map<String, Object> guestSections = new HashMap<>();
    guestSections.put("courses", false);
    guestSections.put("files", false);
    guestSections.put("users", false);
    guestSections.put("questionBanks", false);
    guestSections.put("quizzes", false);
    guestSections.put("assignments", false);
    guestSections.put("comparisons", false);
    guestSections.put("points", false);
    guestSections.put("reminders", false);
    guestSections.put("myQuestionnaire", false);
    guestSections.put("questionnaireAdmin", false);
    Menu guestMenu = Menu.builder()
      .role(Role.UNKNOWN)
      .sections(guestSections)
      .build();

    mongoTemplate.insert(guestMenu, DocumentName.MENU);
  }

}
