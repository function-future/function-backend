package com.future.function.model.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Abstract class containing name of fields in database.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class FieldName {
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static abstract class BaseEntity {
    
    public static final String CREATED_AT = "createdAt";
    
    public static final String CREATED_BY = "createdBy";
    
    public static final String UPDATED_AT = "updatedAt";
    
    public static final String UPDATED_BY = "updatedBy";
    
    public static final String DELETED = "deleted";
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static abstract class User {
    
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
  public static abstract class Batch {
    
    public static final String NUMBER = "number";
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static abstract class File {
    
    public static final String FILE_PATH = "filePath";
    
    public static final String FILE_URL = "fileUrl";
    
    public static final String THUMBNAIL_PATH = "thumbnailPath";
    
    public static final String THUMBNAIL_URL = "thumbnailUrl";
    
    public static final String MARK_FOLDER = "markFolder";
    
    public static final String AS_RESOURCE = "asResource";
    
  }
  
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static abstract class Sequence {
    
    public static final String SEQUENCE_NUMBER = "sequence_number";
    
  }
  
}
