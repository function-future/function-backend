package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Access;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

@ChangeLog(order = "029")
public class DataMigration_029 {

    private static final String URL_REGEX = "^\\/batches\\/[A-Za-z0-9\\-]+\\/(assignments|quizzes|final-judging)" +
            "(\\/|\\/[A-Za-z0-9\\-]+)?(\\/|\\/(detail|addDetail|add)(\\/)?)?$";

    private static final String EMPTY = "";

    @ChangeSet(author = "oliver",
            id = "updateScoringAccessList",
            order = "0001")
    public void updateScoringAccessList(MongoDatabase mongoDatabase) {
        Bson adminFilters = Filters.and(Filters.eq(FieldName.Access.URL_REGEX, URL_REGEX),
                Filters.eq(FieldName.Access.ROLE, Role.ADMIN.name()));
        Document accessList = mongoDatabase.getCollection(DocumentName.ACCESS).find(adminFilters).first();
        String newUrlRegex = accessList.getString(FieldName.Access.URL_REGEX);
        newUrlRegex = newUrlRegex.replace("|final-judging", EMPTY);
        newUrlRegex = newUrlRegex.replace("|addDetail|add", EMPTY);
        accessList.remove(FieldName.Access.URL_REGEX);
        accessList.put(FieldName.Access.URL_REGEX, newUrlRegex);
        mongoDatabase.getCollection(DocumentName.ACCESS).findOneAndReplace(adminFilters, accessList);

        Bson judgeFilters = Filters.and(Filters.eq(FieldName.Access.URL_REGEX, URL_REGEX),
                Filters.eq(FieldName.Access.ROLE, Role.JUDGE.name()));
        Document judgeAccessList = mongoDatabase.getCollection(DocumentName.ACCESS).find(judgeFilters).first();
        judgeAccessList.remove(FieldName.Access.URL_REGEX);
        judgeAccessList.put(FieldName.Access.URL_REGEX, newUrlRegex);
        mongoDatabase.getCollection(DocumentName.ACCESS).findOneAndReplace(judgeFilters, judgeAccessList);

        Bson mentorFilters = Filters.and(Filters.eq(FieldName.Access.URL_REGEX, URL_REGEX),
                Filters.eq(FieldName.Access.ROLE, Role.MENTOR.name()));
        Document mentorAccessList = mongoDatabase.getCollection(DocumentName.ACCESS).find(mentorFilters).first();
        mentorAccessList.remove(FieldName.Access.URL_REGEX);
        mentorAccessList.put(FieldName.Access.URL_REGEX, newUrlRegex);
        mongoDatabase.getCollection(DocumentName.ACCESS).findOneAndReplace(mentorFilters, mentorAccessList);

        Bson studentFilters = Filters.and(Filters.eq(FieldName.Access.URL_REGEX, URL_REGEX),
                Filters.eq(FieldName.Access.ROLE, Role.STUDENT.name()));
        Document studentAccessList = mongoDatabase.getCollection(DocumentName.ACCESS).find(studentFilters).first();
        studentAccessList.remove(FieldName.Access.URL_REGEX);
        studentAccessList.put(FieldName.Access.URL_REGEX, newUrlRegex);
        mongoDatabase.getCollection(DocumentName.ACCESS).findOneAndReplace(studentFilters, studentAccessList);
    }

    @ChangeSet(author = "oliver",
            id = "addFinalJudgingScoringAccessList",
            order = "0002")
    public void addFinalJudgingScoringAccessList(MongoTemplate mongoTemplate) {

        String finalJudgingDetailRegex = URL_REGEX.replace("assignments|quizzes|", EMPTY)
                .replace("|addDetail|add", EMPTY);

        Map<String, Object> judgeComponents = new HashMap<>();
        judgeComponents.put("add", true);
        judgeComponents.put("read", true);
        judgeComponents.put("edit", false);
        judgeComponents.put("delete", false);

        Map<String, Object> mentorComponents = new HashMap<>();
        mentorComponents.put("add", false);
        mentorComponents.put("read", true);
        mentorComponents.put("edit", false);
        mentorComponents.put("delete", false);

        Map<String, Object> adminComponents = new HashMap<>();
        adminComponents.put("add", false);
        adminComponents.put("read", true);
        adminComponents.put("edit", true);
        adminComponents.put("delete", true);

        Map<String, Object> studentComponents = new HashMap<>();
        studentComponents.put("add", false);
        studentComponents.put("read", false);
        studentComponents.put("edit", false);
        studentComponents.put("delete", false);

        Access judgeDetailAccess = Access.builder()
                .components(judgeComponents)
                .urlRegex(finalJudgingDetailRegex)
                .role(Role.JUDGE)
                .build();
        mongoTemplate.insert(judgeDetailAccess, DocumentName.ACCESS);

        Access mentorDetailAccess = Access.builder()
                .components(mentorComponents)
                .urlRegex(finalJudgingDetailRegex)
                .role(Role.MENTOR)
                .build();
        mongoTemplate.insert(mentorDetailAccess, DocumentName.ACCESS);

        Access adminDetailAccess = Access.builder()
                .components(adminComponents)
                .urlRegex(finalJudgingDetailRegex)
                .role(Role.ADMIN)
                .build();
        mongoTemplate.insert(adminDetailAccess, DocumentName.ACCESS);

        Access studentDetailAccess = Access.builder()
                .components(studentComponents)
                .urlRegex(finalJudgingDetailRegex)
                .role(Role.STUDENT)
                .build();
        mongoTemplate.insert(studentDetailAccess, DocumentName.ACCESS);

        String finalJudgingEditRegex = URL_REGEX.replace("assignments|quizzes|", EMPTY)
                .replace("detail|addDetail", "edit");

        adminComponents = new HashMap<>();
        adminComponents.put("add", true);
        adminComponents.put("read", true);
        adminComponents.put("edit", true);
        adminComponents.put("delete", true);

        Map<String, Object> otherComponents = new HashMap<>();
        otherComponents.put("add", false);
        otherComponents.put("read", false);
        otherComponents.put("edit", false);
        otherComponents.put("delete", false);

        Access judgeEditAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(finalJudgingEditRegex)
                .role(Role.JUDGE)
                .build();
        mongoTemplate.insert(judgeEditAccess, DocumentName.ACCESS);

        Access mentorEditAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(finalJudgingEditRegex)
                .role(Role.MENTOR)
                .build();
        mongoTemplate.insert(mentorEditAccess, DocumentName.ACCESS);

        Access adminEditAccess = Access.builder()
                .components(adminComponents)
                .urlRegex(finalJudgingEditRegex)
                .role(Role.ADMIN)
                .build();
        mongoTemplate.insert(adminEditAccess, DocumentName.ACCESS);

        Access studentEditAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(finalJudgingEditRegex)
                .role(Role.STUDENT)
                .build();
        mongoTemplate.insert(studentEditAccess, DocumentName.ACCESS);
    }

    @ChangeSet(author = "oliver",
            id = "addAnotherScoringAccessList",
            order = "0003")
    public void addAnotherScoringAccessList(MongoTemplate mongoTemplate) {

        String scoringAddUrlRegex = URL_REGEX
                .replace("(\\/|\\/[A-Za-z0-9\\-]+)?(\\/|\\/(detail|addDetail|add)(\\/)?)?$", "\\/(add)$");

        Map<String, Object> adminComponents = new HashMap<>();
        adminComponents.put("add", true);
        adminComponents.put("read", true);
        adminComponents.put("edit", true);
        adminComponents.put("delete", true);

        Map<String, Object> otherComponents = new HashMap<>();
        otherComponents.put("add", false);
        otherComponents.put("read", false);
        otherComponents.put("edit", false);
        otherComponents.put("delete", false);

        Access judgeEditAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(scoringAddUrlRegex)
                .role(Role.JUDGE)
                .build();
        mongoTemplate.insert(judgeEditAccess, DocumentName.ACCESS);

        Access mentorEditAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(scoringAddUrlRegex)
                .role(Role.MENTOR)
                .build();
        mongoTemplate.insert(mentorEditAccess, DocumentName.ACCESS);

        Access adminEditAccess = Access.builder()
                .components(adminComponents)
                .urlRegex(scoringAddUrlRegex)
                .role(Role.ADMIN)
                .build();
        mongoTemplate.insert(adminEditAccess, DocumentName.ACCESS);

        Access studentEditAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(scoringAddUrlRegex)
                .role(Role.STUDENT)
                .build();
        mongoTemplate.insert(studentEditAccess, DocumentName.ACCESS);
    }

    @ChangeSet(author = "oliver",
            id = "addEditScoringAccessList",
            order = "0004")
    public void addEditScoringAccessList(MongoTemplate mongoTemplate) {
        String newUrlRegex = URL_REGEX.replace("|final-judging", EMPTY);
        newUrlRegex = newUrlRegex.replace("detail|addDetail|add", "edit");
        Map<String, Object> components = new HashMap<>();
        components.put("read", true);
        components.put("add", true);
        components.put("edit", true);
        components.put("delete", true);
        Access adminAccess = Access.builder()
                .components(components)
                .urlRegex(newUrlRegex)
                .role(Role.ADMIN)
                .build();
        mongoTemplate.insert(adminAccess, DocumentName.ACCESS);

        Map<String, Object> otherComponents = new HashMap<>();
        otherComponents.put("read", false);
        otherComponents.put("add", false);
        otherComponents.put("edit", false);
        otherComponents.put("delete", false);

        Access judgeAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(newUrlRegex)
                .role(Role.JUDGE)
                .build();
        mongoTemplate.insert(judgeAccess, DocumentName.ACCESS);

        Access mentorAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(newUrlRegex)
                .role(Role.MENTOR)
                .build();
        mongoTemplate.insert(mentorAccess, DocumentName.ACCESS);

        Access studentAccess = Access.builder()
                .components(otherComponents)
                .urlRegex(newUrlRegex)
                .role(Role.STUDENT)
                .build();
        mongoTemplate.insert(studentAccess, DocumentName.ACCESS);
    }
}
