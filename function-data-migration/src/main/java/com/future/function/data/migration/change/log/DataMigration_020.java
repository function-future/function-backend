package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ChangeLog(order = "020")
public class DataMigration_020 {

    @ChangeSet(author = "david",
            id = "addUrlRegexForJudgingList",
            order = "0001")
    public void addUrlRegexForJudgingList(
            MongoTemplate mongoTemplate
    ) {

        String urlRegex = "^\\/final-judging(\\/)?$";

        Map<String, Object> adminComponents = new HashMap<>();
        adminComponents.put("add", true);
        adminComponents.put("delete", true);
        adminComponents.put("edit", true);
        adminComponents.put("read", true);

        Map<String, Object> studentComponents = new HashMap<>();
        studentComponents.put("add", false);
        studentComponents.put("delete", false);
        studentComponents.put("edit", false);
        studentComponents.put("read", false);

        Map<String, Object> otherComponents = new HashMap<>();
        otherComponents.put("add", false);
        otherComponents.put("delete", false);
        otherComponents.put("edit", false);
        otherComponents.put("read", true);

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


    @ChangeSet(author = "david",
            id = "removeDeprecatedJudgingRegex",
            order = "0002")
    public void removeDeprecatedJudgingRegex(
            MongoTemplate mongoTemplate
    ) {
        Criteria criteria = Criteria.where(FieldName.Access.URL_REGEX)
                .is("^\\/batches\\/[A-Za-z0-9\\-]+\\/(assignments|quizzes|final-judging)(\\/|\\/[A-Za-z0-9\\-]+)?(\\/|\\/(detail|addDetail|add)(\\/)?)?$");
        List<Access> accesses = mongoTemplate.find(
                Query.query(criteria), Access.class);

        accesses.forEach(access -> {
            access.setUrlRegex("^\\/batches\\/[A-Za-z0-9\\-]+\\/(assignments|quizzes)(\\/|\\/[A-Za-z0-9\\-]+)?(\\/|\\/(detail|addDetail|add)(\\/)?)?$");
            mongoTemplate.save(access);
        });
    }

    @ChangeSet(author = "david",
            id = "addNewUrlRegexForJudgingOperations",
            order = "0003")
    public void addUrlRegexForJudgingOperations(
            MongoTemplate mongoTemplate
    ) {

        String urlRegex = "^\\/batches\\/[A-Za-z0-9\\-]+\\/final-judging\\/(add|report-page|(.*\\/(detail|edit)))(\\/)?$";

        Map<String, Object> adminComponents = new HashMap<>();
        adminComponents.put("add", true);
        adminComponents.put("delete", true);
        adminComponents.put("edit", true);
        adminComponents.put("read", true);

        Map<String, Object> studentComponents = new HashMap<>();
        studentComponents.put("add", false);
        studentComponents.put("delete", false);
        studentComponents.put("edit", false);
        studentComponents.put("read", false);

        Map<String, Object> otherComponents = new HashMap<>();
        otherComponents.put("add", false);
        otherComponents.put("delete", false);
        otherComponents.put("edit", false);
        otherComponents.put("read", true);

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

}