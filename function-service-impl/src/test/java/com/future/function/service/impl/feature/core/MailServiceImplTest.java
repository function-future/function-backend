package com.future.function.service.impl.feature.core;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceImplTest {

  private static final String MESSAGE_CONTENT = "message-content";

  private static final String RECIPIENT = "recipient@recipient.com";

  private static final String SUBJECT = "subject";

  @Mock
  private JavaMailSender mailSender;

  @InjectMocks
  private MailServiceImpl mailService;

  @Mock
  private MimeMessage message;

  @Before
  public void setUp() {

  }

  @After
  public void tearDown() {

    verifyNoMoreInteractions(message, mailSender);
  }

  @Test
  public void testGivenNullRecipientAndNullSubjectAndNullMessageContentBySendingEmailReturnFailedSendEmail() {

    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendEmail(null, null, null);

    verify(mailSender).createMimeMessage();
    verifyZeroInteractions(message);
  }

  @Test
  public void testGivenRecipientAndSubjectAndMessageContentBySendingEmailReturnSuccessfulSendEmail() throws Exception {

    when(mailSender.createMimeMessage()).thenReturn(message);

    mailService.sendEmail(RECIPIENT, SUBJECT, MESSAGE_CONTENT);

    verify(mailSender).createMimeMessage();
    verify(mailSender).send(message);
    verify(message).setRecipient(Message.RecipientType.TO, new InternetAddress(RECIPIENT));
    verify(message).setSubject(SUBJECT);
    verify(message).setText(MESSAGE_CONTENT);
  }
}
