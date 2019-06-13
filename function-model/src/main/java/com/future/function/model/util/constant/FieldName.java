package com.future.function.model.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interface class containing name of fields in database.
 */
public interface FieldName {
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class BaseEntity {

    public static final String ID = "id";

    public static final String CREATED_AT = "createdAt";
    
    public static final String CREATED_BY = "createdBy";
    
    public static final String UPDATED_AT = "updatedAt";
    
    public static final String UPDATED_BY = "updatedBy";
    
    public static final String DELETED = "deleted";
  
    public static final String VERSION = "version";
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class User {
    
    public static final String EMAIL = "email";
    
    public static final String NAME = "name";
    
    public static final String ROLE = "role";
    
    public static final String PASS = "password";
    
    public static final String PHONE = "phone";
    
    public static final String ADDRESS = "address";
    
    public static final String PICTURE = "picture";
    
    public static final String BATCH = "batch";
    
    public static final String UNIVERSITY = "university";
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Batch {
    
    public static final String NAME = "name";
    
    public static final String CODE = "code";
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class File {
    
    public static final String FILE_PATH = "filePath";
    
    public static final String FILE_URL = "fileUrl";
    
    public static final String THUMBNAIL_PATH = "thumbnailPath";
    
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    
    public static final String NAME = "name";
    
    public static final String PARENT_ID = "parentId";
    
    public static final String USED = "used";
    
    public static final String MARK_FOLDER = "markFolder";
    
    public static final String AS_RESOURCE = "asResource";
    
    public static final String VERSIONS = "versions";
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Announcement {
    
    public static final String TITLE = "title";
    
    public static final String SUMMARY = "summary";
    
    public static final String DESCRIPTION = "description";
    
    public static final String FILE = "file";
    
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Chatroom {

    public static final String MEMBERS = "members";

    public static final String CHATROOM_TITLE = "chatroomTitle";

    public static final String TYPE = "type";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Message {

    public static final String TEXT = "text";

    public static final String SENDER = "sender";

    public static final String CHATROOM = "chatroom";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class MessageStatus {

    public static final String MEMBER = "member";

    public static final String MESSAGE = "message";

    public static final String IS_SEEN = "isSeen";

    public static final String CHATROOM = "chatroom";

  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Course {

    public static final String TITLE = "title";

    public static final String DESCRIPTION = "description";

    public static final String FILE = "file";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class SharedCourse {

    public static final String BATCH = "batch";

    public static final String COURSE = "course";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Discussion {

    public static final String DESCRIPTION = "description";

    public static final String USER = "user";

    public static final String COURSE_ID = "course_id";

    public static final String BATCH_CODE = "batch_code";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Quiz {

    public static final String TITLE = "title";
    
    public static final String DESCRIPTION = "description";
    
    public static final String START_DATE = "startDate";
    
    public static final String END_DATE = "endDate";
    
    public static final String TIME_LIMIT = "timeLimit";
    
    public static final String TRIALS = "trials";
    
    public static final String QUESTION_BANK = "questionBanks";
    
    public static final String QUESTION_COUNT = "questionCount";
    
    public static final String BATCH = "batch";
    
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class QuestionBank {

    public static final String DESCRIPTION = "description";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Question {

    public static final String TEXT = "text";

    public static final String QUESTION_BANK = "questionBank";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Option {

    public static final String LABEL = "text";

    public static final String CORRECT = "correct";

    public static final String QUESTION = "question";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class StudentQuiz {

    public static final String TRIALS = "TRIALS";

    public static final String DONE = "done";

    public static final String STUDENT = "student";

    public static final String QUIZ = "quiz";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class StudentQuizDetail {

    public static final String POINT = "point";

    public static final String STUDENT_QUIZ = "studentQuiz";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class StudentQuestion {

    public static final String STUDENT_QUIZ_DETAIL = "studentQuizDetail";

    public static final String QUESTION = "question";

    public static final String OPTION = "option";

    public static final String CORRECT = "correct";

    public static final String NUMBER = "number";

  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class ActivityBlog {
    
    public static final String TITLE = "title";
    
    public static final String DESCRIPTION = "description";
    
    public static final String USER = "user";
    
    public static final String FILES = "files";
    
  }
  
}
