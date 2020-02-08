package com.future.function.data.migration.change.log;

import com.future.function.model.util.constant.DocumentName;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.conversions.Bson;

import static com.future.function.data.migration.constant.IndexName.*;

@ChangeLog(order = "028")
public class DataMigration_028 {

    public static final String QUIZ_STUDENT_VIEW_INDEX_NAME = "student_quiz_batch_1_deleted_1_startDate_1_endDate_-1";
    public static final String QUIZ_ADMIN_VIEW_INDEX_NAME = "quiz_batch_1_deleted_1_endDate_-1";
    public static final String QUIZ_PAST_VIEW_INDEX_NAME = "past_quiz_batch_1_deleted_1_startDate_1_endDate_-1";

    public static final String ASSIGNMENT_VIEW_INDEX_NAME = "assignment_batch_1_deleted_1_deadline_-1";
    public static final String ASSIGNMENT_PAST_VIEW_INDEX_NAME = "past_assignment_batch_1_deleted_1_deadline_1";

    public static final String ROOM_VIEW_INDEX_NAME = "past_room_studentId_1_assignmentId_1_deleted_1";
    public static final String ROOM_STUDENT_VIEW_INDEX_NAME = "past_room_studentId_1_deleted_1";
    public static final String ROOM_ASSIGNMENT_VIEW_INDEX_NAME = "past_room_assignmentId_1_deleted_1";

    public static final String STUDENT_QUIZ_STUDENT_INDEX_NAME = "student_quiz_student_1_deleted_1";
    public static final String REPORT_NEW_INDEX_NAME = "report_batch_1_deleted_1";

    @ChangeSet(author = "oliver",
            id = "addQuizIndexes",
            order = "0001")
    public void addQuizIndexes(MongoDatabase mongoDatabase) {

        Bson studentViewIndex = Indexes.compoundIndex(Indexes.ascending("batch", "deleted", "startDate"),
                Indexes.descending("endDate"));
        IndexOptions studentViewOptions = new IndexOptions();
        studentViewOptions.background(true);
        studentViewOptions.sparse(true);
        studentViewOptions.name(QUIZ_STUDENT_VIEW_INDEX_NAME);
        Bson adminViewIndex = Indexes.compoundIndex(Indexes.ascending("batch", "deleted"),
                Indexes.descending("endDate"));
        IndexOptions adminViewOptions = new IndexOptions();
        adminViewOptions.background(true);
        adminViewOptions.sparse(true);
        adminViewOptions.name(QUIZ_ADMIN_VIEW_INDEX_NAME);
        Bson pastViewIndex = Indexes.ascending("batch", "deleted", "endDate");
        IndexOptions pastViewOptions = new IndexOptions();
        pastViewOptions.background(true);
        pastViewOptions.sparse(true);
        pastViewOptions.name(QUIZ_PAST_VIEW_INDEX_NAME);
        mongoDatabase.getCollection(DocumentName.QUIZ).createIndex(studentViewIndex, studentViewOptions);
        mongoDatabase.getCollection(DocumentName.QUIZ).createIndex(adminViewIndex, adminViewOptions);
        mongoDatabase.getCollection(DocumentName.QUIZ).createIndex(pastViewIndex, pastViewOptions);
    }

    @ChangeSet(author = "oliver",
            id = "addAssignmentIndexes",
            order = "0002")
    public void addAssignmentIndexes(MongoDatabase mongoDatabase) {

        Bson viewIndex = Indexes.compoundIndex(Indexes.ascending("batch", "deleted"),
                Indexes.descending("deadline"));
        IndexOptions viewOptions = new IndexOptions();
        viewOptions.background(true);
        viewOptions.sparse(true);
        viewOptions.name(ASSIGNMENT_VIEW_INDEX_NAME);
        Bson pastViewIndex = Indexes.ascending("batch", "deleted", "deadline");
        IndexOptions pastViewOptions = new IndexOptions();
        pastViewOptions.background(true);
        pastViewOptions.sparse(true);
        pastViewOptions.name(ASSIGNMENT_PAST_VIEW_INDEX_NAME);
        mongoDatabase.getCollection(DocumentName.ASSIGNMENT).createIndex(viewIndex, viewOptions);
        mongoDatabase.getCollection(DocumentName.ASSIGNMENT).createIndex(pastViewIndex, pastViewOptions);
    }

    @ChangeSet(author = "oliver",
            id = "updateRoomIndexes",
            order = "0003")
    public void updateRoomIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.ROOM).dropIndex(ROOM_ASSIGNMENT_USER_ID_AND_DELETED.name());
        Bson newRoomIndex = Indexes.ascending("student.$id", "assignment.$id", "deleted");
        IndexOptions newRoomIndexOptions = new IndexOptions();
        newRoomIndexOptions.background(true);
        newRoomIndexOptions.sparse(true);
        newRoomIndexOptions.unique(true);
        newRoomIndexOptions.name(ROOM_VIEW_INDEX_NAME);
        mongoDatabase.getCollection(DocumentName.ROOM).createIndex(newRoomIndex, newRoomIndexOptions);

        Bson studentIndex = Indexes.ascending("student.$id", "deleted");
        IndexOptions studentIndexOptions = new IndexOptions();
        studentIndexOptions.background(true);
        studentIndexOptions.sparse(true);
        studentIndexOptions.name(ROOM_STUDENT_VIEW_INDEX_NAME);
        mongoDatabase.getCollection(DocumentName.ROOM).createIndex(studentIndex, studentIndexOptions);
    }

    @ChangeSet(author = "oliver",
            id = "updateReportDetailIndexes",
            order = "0004")
    public void updateReportDetailIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.REPORT_DETAIL).dropIndex(REPORT_DETAIL_REPORT_ID_AND_DELETED.name());
        mongoDatabase.getCollection(DocumentName.REPORT_DETAIL).dropIndex(REPORT_DETAIL_USER_ID_AND_DELETED.name());
        Bson newReportDetailIndex = Indexes.ascending("user.$id", "deleted");
        IndexOptions newReportDetailIndexOptions = new IndexOptions();
        newReportDetailIndexOptions.background(true);
        newReportDetailIndexOptions.sparse(true);
        newReportDetailIndexOptions.unique(true);
        newReportDetailIndexOptions.name(REPORT_DETAIL_USER_ID_AND_DELETED.name());
        mongoDatabase.getCollection(DocumentName.REPORT_DETAIL).createIndex(newReportDetailIndex, newReportDetailIndexOptions);
    }

    @ChangeSet(author = "oliver",
            id = "addReportIndexes",
            order = "0005")
    public void addReportIndexes(MongoDatabase mongoDatabase) {
        Bson reportIndex = Indexes.ascending("batch", "deleted");
        IndexOptions reportIndexOptions = new IndexOptions();
        reportIndexOptions.background(true);
        reportIndexOptions.sparse(true);
        reportIndexOptions.name(REPORT_NEW_INDEX_NAME);
        mongoDatabase.getCollection(DocumentName.REPORT)
                .createIndex(reportIndex, reportIndexOptions);
    }

    @ChangeSet(author = "oliver",
            id = "updateStudentQuizIndexes",
            order = "0006")
    public void updateStudentQuizIndexes(MongoDatabase mongoDatabase) {
        mongoDatabase.getCollection(DocumentName.STUDENT_QUIZ)
                .dropIndex(STUDENT_QUIZ_USER_ID_AND_QUIZ_ID_AND_DELETED.name());
        Bson newIndex = Indexes.ascending("student.$id", "quiz.$id", "deleted");
        IndexOptions newIndexOptions = new IndexOptions();
        newIndexOptions.background(true);
        newIndexOptions.sparse(true);
        newIndexOptions.unique(true);
        newIndexOptions.name(STUDENT_QUIZ_USER_ID_AND_QUIZ_ID_AND_DELETED.name());
        mongoDatabase.getCollection(DocumentName.STUDENT_QUIZ)
                .createIndex(newIndex, newIndexOptions);
    }
}
