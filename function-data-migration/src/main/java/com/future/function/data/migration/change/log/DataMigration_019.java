package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.entity.feature.core.Menu;
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

@ChangeLog(order = "019")
public class DataMigration_019 {

  @ChangeSet(author = "jonathan",
             id = "changeProfileAccessListToSupportMobile",
             order = "0001")
  public void changeProfileAccessListToSupportMobile(
    MongoTemplate mongoTemplate
  ) {

    Criteria criteria = Criteria.where(FieldName.Access.URL_REGEX)
      .is("^\\/profile(\\/|\\/change-password(\\/)?)?$");
    List<Access> accesses = mongoTemplate.find(
      Query.query(criteria), Access.class);

    accesses.forEach(access -> {
      access.setUrlRegex("^(\\/m)?\\/profile(\\/|\\/change-password(\\/)?)?$");
      mongoTemplate.save(access);
    });
  }

  @ChangeSet(author = "jonathan",
             id = "addAccountAccessList",
             order = "0002")
  public void addAccountAccessList(MongoTemplate mongoTemplate) {

    String urlRegex = "^\\/account(\\/)?$";

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
             id = "addBatchesMenuToMenuList",
             order = "0003")
  public void addBatchesMenuToMenuList(MongoTemplate mongoTemplate) {

    List<Menu> menus = mongoTemplate.findAll(Menu.class);

    menus.forEach(menu -> {
      menu.getSections()
        .put(
          "batches", menu.getRole()
            .equals(Role.ADMIN));
      mongoTemplate.save(menu);
    });
  }

  @ChangeSet(author = "jonathan",
             id = "changeAccessListForBatchesPage",
             order = "0004")
  public void changeAccessListForBatchesPage(MongoTemplate mongoTemplate) {

    Criteria criteria = Criteria.where(FieldName.Access.URL_REGEX)
      .is("^\\/batches(\\/)?$");
    List<Access> accesses = mongoTemplate.find(
      Query.query(criteria), Access.class);

    accesses.forEach(access -> {
      Map<String, Object> components = access.getComponents();
      components.keySet()
        .forEach(key -> components.put(key, access.getRole()
          .equals(Role.ADMIN)));
      mongoTemplate.save(access);
    });
  }

  @ChangeSet(author = "jonathan",
             id = "changeAccessListForAddBatchPage",
             order = "0005")
  public void changeAccessListForAddBatchPage(MongoTemplate mongoTemplate) {

    Criteria criteria = Criteria.where(FieldName.Access.URL_REGEX)
      .is("^\\/batches\\/add(\\/)?$");
    List<Access> accesses = mongoTemplate.find(
      Query.query(criteria), Access.class);

    accesses.forEach(access -> {
      Map<String, Object> components = access.getComponents();
      components.keySet()
        .forEach(key -> components.put(key, access.getRole()
          .equals(Role.ADMIN)));
      mongoTemplate.save(access);
    });
  }

}
