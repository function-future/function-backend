package com.future.function.data.migration.change.log;

import com.future.function.common.enumeration.core.Role;
import com.future.function.model.entity.feature.core.Menu;
import com.future.function.model.util.constant.DocumentName;
import com.future.function.model.util.constant.FieldName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings(value = { "squid:S00101", "squid:S1192" })
@ChangeLog(order = "019")
public class DataMigration_019 {

    // Update menu list for scoring.

    private Document removeDeprecatedMenuList (Document document) {
        document.remove("questionBanks");
        document.remove("quizzes");
        document.remove("assignments");
        document.remove("comparisons");
        return document;
    }

    @ChangeSet(author = "david",
            id = "scoringUpdateStudentMenuList",
            order = "0001")
    public void changeStudentMenuList(MongoDatabase mongoDatabase) {
        MongoCollection<Document> menuCollection = mongoDatabase.getCollection(DocumentName.MENU);
        Bson filter = Filters.eq(FieldName.Menu.ROLE, Role.STUDENT);
        menuCollection
                .find(Filters.eq(FieldName.Menu.ROLE, Role.STUDENT))
                .forEach((Consumer<? super Document>) document -> {
                    document = removeDeprecatedMenuList(document);
                    document.put("grades", true);
                    document.put("points", true);
                    document.put("comparisons", false);
                    menuCollection.findOneAndReplace(filter, document);
                });
    }

    @ChangeSet(author = "david",
            id = "scoringUpdateGuestMenuList",
            order = "0002")
    public void changeGuestMenuList(MongoDatabase mongoDatabase) {
        MongoCollection<Document> menuCollection = mongoDatabase.getCollection(DocumentName.MENU);
        Bson filter = Filters.eq(FieldName.Menu.ROLE, Role.UNKNOWN);
        menuCollection
                .find(Filters.eq(FieldName.Menu.ROLE, Role.UNKNOWN))
                .forEach((Consumer<? super Document>) document -> {
                    document = removeDeprecatedMenuList(document);
                    document.put("grades", false);
                    document.put("points", false);
                    document.put("comparisons", false);
                    menuCollection.findOneAndReplace(filter, document);
                });
    }

    @ChangeSet(author = "david",
            id = "scoringUpdateOthersMenuList",
            order = "0003")
    public void changeOthersMenuList(MongoDatabase mongoDatabase) {
        MongoCollection<Document> menuCollection = mongoDatabase.getCollection(DocumentName.MENU);
        Bson filter = Filters.regex(FieldName.Menu.ROLE, ".*(ADMIN|JUDGE|MENTOR).*");
        menuCollection
                .find(Filters.eq(FieldName.Menu.ROLE, Role.ADMIN))
                .forEach((Consumer<? super Document>) document -> {
                    document = removeDeprecatedMenuList(document);
                    document.put("grades", true);
                    document.put("points", false);
                    document.put("comparisons", true);
                    menuCollection.findOneAndReplace(filter, document);
                });
    }
}
