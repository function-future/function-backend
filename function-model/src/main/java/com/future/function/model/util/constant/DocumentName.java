package com.future.function.model.util.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Abstract class containing name of documents in database.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DocumentName {
  
  public static final String BATCH = "batches";
  
  public static final String FILE = "files";
  
  public static final String USER = "users";

  public static final String COURSE = "courses";

  public static final String SHARED_COURSE = "shared-courses";

  public static final String ANNOUNCEMENT = "announcements";

  public static final String STICKY_NOTE = "sticky-notes";
  
  public static final String DISCUSSION = "discussions";
  
  public static final String ACTIVITY_BLOG = "activity-blogs";

  public static final String ASSIGNMENT = "assignments";

  public static final String QUESTION_BANK = "question-banks";

  public static final String QUIZ = "quizzes";

  public static final String STUDENT_QUIZ = "student-quizzes";

  public static final String STUDENT_QUIZ_DETAIL = "student-quizzes-details";

  public static final String STUDENT_QUESTION = "student-questions";

  public static final String QUESTION = "questions";

  public static final String OPTION = "options";

  public static final String CHATROOM = "chatrooms";

  public static final String MESSAGE = "messages";

  public static final String MESSAGE_STATUS = "message-status";
  
}
