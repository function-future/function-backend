package com.future.function.model.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Interface class containing name of fields in database.
 */
public interface FieldName {
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class BaseEntity {
    
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
    
    public static final String PASSWORD = "password";
    
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
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Announcement {
    
    public static final String TITLE = "title";
    
    public static final String SUMMARY = "summary";
    
    public static final String DESCRIPTION = "description";
    
    public static final String FILE = "file";
    
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Questionnaire {

    public static final String TITLE = "title";

    public static final String DESCRIPTION = "description";

    public static final String START_DATE = "startDate";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Question {

    public static final String QUESIONNAIRE = "questionnaire";

    public static final String DESCRIPTION = "description";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class QuestionResponse {

    public static final String QUESTION = "question";

    public static final String APRAISER = "apraiser";

    public static final String APRAISEE = "apraisee";

    public static final String SCORE = "score";

    public static final String COMMENT = "comment";

  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Answer {

    public static final String MIN = "minimum";

    public static final String MAX = "maximum";

    public static final String AVG = "average";
  }
}
