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
  
  public static final String ANNOUNCEMENT = "announcements";

  public static final String STICKY_NOTE = "sticky-notes";


  public static final String QUESTIONNAIRE = "questionnaires";

  public static final String QUESTION = "questions";

  public static final String QUESTION_RESPONSE = "question-responses";

  public static final String ANSWER = "answers";

  public static final String QUESTIONS_RESPONSE_SUMMARY = "questions-response-summary";

  public static final String QUESTIONNAIRE_RESPONSE = "questionnaire-responses";

  public static final String QUESTIONNAIRES_RESPONSE_SUMMARY = "questionnaires-response-summary";

  public static final String QUESTIONNAIRE_PARTICIPANT = "questionnaire-participants";

  public static final String USER_QUESTIONNAIRE_SUMMARY = "user-questionnaire-summary";

  public static final String CHATROOM = "chatrooms";

  public static final String MESSAGE = "messages";

  public static final String MESSAGE_STATUS = "message-status";


}
