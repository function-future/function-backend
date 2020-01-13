package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Menu;
import com.future.function.model.util.constant.FieldName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@ChangeLog(order = "020")
public class DataMigration_020 {

    // Update menu list for scoring.

    private Menu removeDeprecatedMenuList (Menu menu) {
        menu.getSections().remove("questionBanks");
        menu.getSections().remove("assignments");
        menu.getSections().remove("quizzes");
        return menu;
    }

    @ChangeSet(author = "david",
            id = "scoringUpdateStudentMenuList",
            order = "0001")
    public void changeStudentMenuList(MongoTemplate mongoTemplate) {
        Criteria criteria = Criteria.where(FieldName.Menu.ROLE)
                .is(Role.STUDENT);

        List<Menu> menus = mongoTemplate.find(Query.query(criteria), Menu.class);
        menus.forEach(menu -> {
            removeDeprecatedMenuList(menu);
            menu.getSections().put("grades", true);
            menu.getSections().put("points", true);
            menu.getSections().put("comparisons", false);
            mongoTemplate.save(menu);
        });
    }

    @ChangeSet(author = "david",
            id = "scoringUpdateGuestMenuList",
            order = "0002")
    public void changeGuestMenuList(MongoTemplate mongoTemplate) {
        Criteria criteria = Criteria.where(FieldName.Menu.ROLE)
                .is(Role.UNKNOWN);

        List<Menu> menus = mongoTemplate.find(Query.query(criteria), Menu.class);
        menus.forEach(menu -> {
            removeDeprecatedMenuList(menu);
            menu.getSections().put("grades", false);
            menu.getSections().put("points", false);
            menu.getSections().put("comparisons", false);
            mongoTemplate.save(menu);
        });
    }

    @ChangeSet(author = "david",
            id = "scoringUpdateOthersMenuList",
            order = "0003")
    public void changeOthersMenuList(MongoTemplate mongoTemplate) {
        Criteria criteria = Criteria.where(FieldName.Menu.ROLE)
                .regex(".*(ADMIN|JUDGE|MENTOR).*");

        List<Menu> menus = mongoTemplate.find(Query.query(criteria), Menu.class);
        menus.forEach(menu -> {
            removeDeprecatedMenuList(menu);
            menu.getSections().put("grades", true);
            menu.getSections().put("points", false);
            menu.getSections().put("comparisons", true);
            mongoTemplate.save(menu);
        });
    }

}
