package com.future.function.service.impl.feature.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.future.function.service.api.feature.core.MailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

  private final JavaMailSender mailSender;

  private final ExecutorService executorService;

  @Autowired
  public MailServiceImpl(JavaMailSender mailSender, ExecutorService executorService) {

    this.mailSender = mailSender;
    this.executorService = executorService;
  }

  @Override
  public void sendEmail(String recipient, String subject, String messageContent) {

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

    try {
      messageHelper.setTo(recipient);
      messageHelper.setSubject(subject);
      messageHelper.setText(messageContent);

      CompletableFuture.runAsync(() -> mailSender.send(message), executorService);
    } catch (Exception e) {
      log.error("Failed send message: ", e);
    }
  }
}
