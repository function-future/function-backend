package com.future.function.service.impl.feature.core;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.future.function.service.api.feature.core.MailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

  private final JavaMailSender mailSender;

  public MailServiceImpl(JavaMailSender mailSender) {

    this.mailSender = mailSender;
  }

  @Override
  public void sendEmail(String recipient, String subject, String messageContent) {

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

    try {
      messageHelper.setTo(recipient);
      messageHelper.setSubject(subject);
      messageHelper.setText(messageContent);

      mailSender.send(message);
    } catch (Exception e) {
      log.error("Failed send message: ", e);
    }
  }
}
