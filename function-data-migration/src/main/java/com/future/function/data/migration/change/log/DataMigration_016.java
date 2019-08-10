package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import static com.future.function.data.migration.constant.IndexName.*;

@ChangeLog(order = "016")
public class DataMigration_016 {

    @ChangeSet(author = "oliver",
            id = "quizIndexes",
            order = "0001")
    public void addQuizIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.QUIZ)
                .createIndex(
                        Indexes.ascending(ENTITY_DELETED.getFields()),
                        new IndexOptions().name(ENTITY_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "assignmentIndexes",
            order = "0002")
    public void addAssignmentIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.ASSIGNMENT)
                .createIndex(
                        Indexes.ascending(ENTITY_DELETED.getFields()),
                        new IndexOptions().name(ENTITY_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "studentQuizIndexes",
            order = "0003")
    public void addStudentQuizIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.STUDENT_QUIZ)
                .createIndex(
                        Indexes.ascending(STUDENT_QUIZ_USER_ID_AND_QUIZ_ID_AND_DELETED.getFields()),
                        new IndexOptions().name(STUDENT_QUIZ_USER_ID_AND_QUIZ_ID_AND_DELETED.name())
                );

        mongoDatabase.getCollection(DocumentName.STUDENT_QUIZ)
                .createIndex(
                        Indexes.ascending(STUDENT_QUIZ_USER_ID_AND_DELETED.getFields()),
                        new IndexOptions().name(STUDENT_QUIZ_USER_ID_AND_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "assignmentRoomIndexes",
            order = "0004")
    public void addAssignmentRoomIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.ROOM)
                .createIndex(
                        Indexes.ascending(ROOM_ASSIGNMENT_ID_AND_DELETED.getFields()),
                        new IndexOptions().name(ROOM_ASSIGNMENT_ID_AND_DELETED.name())
                );

        mongoDatabase.getCollection(DocumentName.ROOM)
                .createIndex(
                        Indexes.ascending(ROOM_ASSIGNMENT_USER_ID_AND_DELETED.getFields()),
                        new IndexOptions().name(ROOM_ASSIGNMENT_USER_ID_AND_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "roomCommentIndexes",
            order = "0005")
    public void addRoomCommentIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.COMMENT)
                .createIndex(
                        Indexes.descending(DISCUSSION_ROOM_ID_AND_CREATED_AT.getFields()),
                        new IndexOptions().name(DISCUSSION_ROOM_ID_AND_CREATED_AT.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "reportDetailIndexes",
            order = "0006")
    public void addReportDetailIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.REPORT_DETAIL)
                .createIndex(
                        Indexes.ascending(REPORT_DETAIL_REPORT_ID_AND_DELETED.getFields()),
                        new IndexOptions().name(REPORT_DETAIL_REPORT_ID_AND_DELETED.name())
                );

        mongoDatabase.getCollection(DocumentName.REPORT_DETAIL)
                .createIndex(
                        Indexes.ascending(REPORT_DETAIL_USER_ID_AND_DELETED.getFields()),
                        new IndexOptions().name(REPORT_DETAIL_USER_ID_AND_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "questionBankIndexes",
            order = "0007")
    public void addQuestionBankIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.QUESTION_BANK)
                .createIndex(
                        Indexes.ascending(ENTITY_DELETED.getFields()),
                        new IndexOptions().name(ENTITY_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "questionIndexes",
            order = "0008")
    public void addQuestionIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.QUESTION)
                .createIndex(
                        Indexes.ascending(QUESTION_QUESTION_BANK_ID_AND_DELETED.getFields()),
                        new IndexOptions().name(QUESTION_QUESTION_BANK_ID_AND_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "optionIndexes",
            order = "0009")
    public void addOptionIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.OPTION)
                .createIndex(
                        Indexes.ascending(OPTION_QUESTION__ID.getFields()),
                        new IndexOptions().name(OPTION_QUESTION__ID.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "reportIndexes",
            order = "0010")
    public void addReportIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.REPORT)
                .createIndex(
                        Indexes.ascending(ENTITY_DELETED.getFields()),
                        new IndexOptions().name(ENTITY_DELETED.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "studentQuestionIndexes",
            order = "0011")
    public void addStudentQuestionIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.STUDENT_QUESTION)
                .createIndex(
                        Indexes.ascending(STUDENT_QUESTION_STUDENT_QUIZ_DETAIL_ID_AND_NUMBER.getFields()),
                        new IndexOptions().name(STUDENT_QUESTION_STUDENT_QUIZ_DETAIL_ID_AND_NUMBER.name())
                );
    }

    @ChangeSet(author = "oliver",
            id = "studentQuizDetailIndexes",
            order = "0012")
    public void addStudentQuizDetailIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.STUDENT_QUIZ_DETAIL)
                .createIndex(
                        Indexes.descending(STUDENT_QUIZ_DETAIL_STUDENT_QUIZ_ID_AND_CREATED_AT_AND_DELETED.getFields()),
                        new IndexOptions().name(STUDENT_QUIZ_DETAIL_STUDENT_QUIZ_ID_AND_CREATED_AT_AND_DELETED.name())
                );
    }

}
