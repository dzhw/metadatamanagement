package eu.dzhw.fdz.metadatamanagement.mailmanagement.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

  private final Logger log = LoggerFactory.getLogger(MailService.class);

  @Autowired
  private JHipsterProperties jhipsterProperties;

  @Autowired
  private JavaMailSenderImpl javaMailSender;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Async
  private Future<Void> sendEmail(String[] to, String subject, String content, boolean isMultipart,
      boolean isHtml) {
    log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
        isMultipart, isHtml, to, subject, content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message =
          new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
      message.setTo(to);
      message.setFrom(jhipsterProperties.getMail()
          .getFrom());
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);

      log.debug("Sent e-mail to users '{}'", Arrays.toString(to));
    } catch (MessagingException e) {
      log.warn("E-mail could not be sent to users '{}', exception is: {}", 
          Arrays.toString(to), e.getMessage());
    }

    return new AsyncResult<Void>(null);
  }

  /**
   * Send user activation email.
   */
  @Async
  public Future<Void> sendActivationEmail(User user, String baseUrl) {
    log.debug("Sending activation e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("activationEmail", context);
    String subject = messageSource.getMessage("email.activation.title", null, locale);
    return sendEmail(new String[] {user.getEmail()}, subject, content, false, true);
  }

  /**
   * Send password reset mail.
   */
  @Async
  public Future<Void> sendPasswordResetMail(User user, String baseUrl) {
    log.debug("Sending password reset e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("passwordResetEmail", context);
    String subject = messageSource.getMessage("email.reset.title", null, locale);
    return sendEmail(new String[] {user.getEmail()}, subject, content, false, true);
  }
  
  /**
   * Send new account activated mail.
   */
  @Async
  public Future<Void> sendNewAccountActivatedMail(List<User> admins, User newUser) {
    log.debug("Sending new account e-mail to all admins");
    Context context = new Context();
    context.setVariable("user", newUser);
    String content = templateEngine.process("newAccountActivatedEmail", context);
    String subject = "New account " + newUser.getLogin() + " activated";
    List<String> emailAddresses = admins.stream().map(User::getEmail).collect(Collectors.toList());
    return sendEmail(emailAddresses.toArray(new String[emailAddresses.size()]), 
        subject, content, false, true);
  }

}
