package com.future.function.service.api.feature.core;

public interface MailService {

  void sendEmail(String recipient, String subject, String messageContent);
}
