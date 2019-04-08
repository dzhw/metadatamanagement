package eu.dzhw.fdz.metadatamanagement.mailmanagement.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 */
@Service
@Slf4j
public class MailService {
  @Autowired
  private JHipsterProperties jhipsterProperties;

  @Autowired
  private JavaMailSenderImpl javaMailSender;

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Autowired
  private Environment env;

  @Value("${metadatamanagement.server.context-root}")
  private String baseUrl;

  private Future<Void> sendEmail(String from, String[] to, String cc, String bcc, String subject,
      String content, boolean isMultipart, boolean isHtml) {
    log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
        isMultipart, isHtml, to, subject, content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message =
          new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
      message.setTo(to);
      if (StringUtils.hasText(cc)) {
        message.setCc(cc);
      }
      if (StringUtils.hasText(bcc)) {
        message.setBcc(bcc);
      }
      if (StringUtils.hasText(from)) {
        message.setFrom(from);
      } else {
        message.setFrom(jhipsterProperties.getMail().getFrom());
      }
      message.setSubject(subject);
      message.setText(content, isHtml);
      javaMailSender.send(mimeMessage);

      log.debug("Sent e-mail to users '{}'", Arrays.toString(to));
    } catch (MessagingException e) {
      log.warn("E-mail could not be sent to users '{}', exception is: {}", Arrays.toString(to),
          e.getMessage());
    }

    return new AsyncResult<>(null);
  }

  /**
   * Send user activation email.
   */
  @Async
  public Future<Void> sendActivationEmail(User user) {
    log.debug("Sending activation e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("activationEmail", context);
    String subject = messageSource.getMessage("email.activation.title", null, locale);
    return sendEmail(null, new String[] {user.getEmail()}, null, null, subject, content, false,
        true);
  }

  /**
   * Send password reset mail.
   */
  @Async
  public Future<Void> sendPasswordResetMail(User user) {
    log.debug("Sending password reset e-mail to '{}'", user.getEmail());
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("passwordResetEmail", context);
    String subject = messageSource.getMessage("email.reset.title", null, locale);
    return sendEmail(null, new String[] {user.getEmail()}, null, null, subject, content, false,
        true);
  }

  /**
   * Send new account activated mail.
   */
  @Async
  public Future<Void> sendNewAccountActivatedMail(List<User> admins, User newUser) {
    log.debug("Sending new account e-mail to all admins");
    Context context = new Context();
    context.setVariable("user", newUser);
    context.setVariable("profiles", env.getActiveProfiles());
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("newAccountActivatedEmail", context);
    String subject = "New account " + newUser.getLogin() + " activated ("
        + StringUtils.arrayToCommaDelimitedString(env.getActiveProfiles()) + ")";
    List<String> emailAddresses = admins.stream().map(User::getEmail).collect(Collectors.toList());
    return sendEmail(null, emailAddresses.toArray(new String[emailAddresses.size()]), null, null,
        subject, content, false, true);
  }

  /**
   * Send an mail, if an automatic update to dara was not successful.
   */
  @Async
  public Future<Void> sendMailOnDaraAutomaticUpdateError(List<User> admins, String projectId) {
    log.debug("Sending 'automatic update to dara was not successful' mail");
    Context context = new Context();
    context.setVariable("projectId", projectId);
    String content = templateEngine.process("automaticDaraUpdateFailed", context);
    String subject = "Automatic Update to da|ra was not successful";
    List<String> emailAddresses = admins.stream().map(User::getEmail).collect(Collectors.toList());
    return sendEmail(null, emailAddresses.toArray(new String[emailAddresses.size()]), null, null,
        subject, content, false, true);
  }

  /**
   * Synchronously send an email to the customer and dataservice.
   */
  public void sendOrderCreatedMail(Order order, String cc) {
    log.debug("Sending notification email for order " + order.getId());
    Locale locale = Locale.forLanguageTag(order.getLanguageKey());
    Context context = new Context(locale);
    context.setVariable("order", order);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("orderCreated", context);
    String subject = messageSource.getMessage("email.order.created.title", null, locale);
    sendEmail(cc, new String[] {order.getCustomer().getEmail()}, cc, null, subject, content, false,
        true);
  }

  /**
   * Send a mail to users who were added as publishers to a project.
   */
  @Async
  public void sendPublishersAddedMail(List<User> publishers, String projectId, String sender) {
    if (!publishers.isEmpty()) {
      log.debug("Sending 'publishers added' mail");
      sendChangedProjectConfigurationMail("addedToProjectConfiguration",
          "email.project-configuration-added.title", "email.project-configuration.publisher-role",
          publishers, projectId, sender);
    }
  }

  /**
   * Send a mail to users who were removed as publishers from a project.
   */
  @Async
  public void sendPublisherRemovedMail(List<User> removedPublisherUsers, String projectId,
      String sender) {
    if (!removedPublisherUsers.isEmpty()) {
      log.debug("Sending 'publishers removed' mail");
      sendChangedProjectConfigurationMail("removedFromProjectConfiguration",
          "email.project-configuration-removed.title", "email.project-configuration.publisher-role",
          removedPublisherUsers, projectId, sender);
    }
  }

  /**
   * Send a mail to users who were added as data providers to a project.
   */
  @Async
  public void sendDataProviderAddedMail(List<User> addedDataProviders, String projectId,
      String sender) {
    if (!addedDataProviders.isEmpty()) {
      log.debug("Sending 'data providers added' mail");
      sendChangedProjectConfigurationMail("addedToProjectConfiguration",
          "email.project-configuration-added.title",
          "email.project-configuration.data-provider-role", addedDataProviders, projectId, sender);
    }
  }

  /**
   * Send a mail to users who were removed as data providers to a project.
   */
  @Async
  public void sendDataProviderRemovedMail(List<User> removedDataProviders, String projectId,
      String sender) {
    if (!removedDataProviders.isEmpty()) {
      log.debug("Sending 'data providers removed' mail");
      sendChangedProjectConfigurationMail("removedFromProjectConfiguration",
          "email.project-configuration-removed.title",
          "email.project-configuration.data-provider-role", removedDataProviders, projectId,
          sender);
    }
  }

  @Async
  private void sendChangedProjectConfigurationMail(String template, String subjectKey,
      String roleKey, List<User> users, String projectId, String sender) {
    users.parallelStream().forEach(user -> {
      Locale locale = Locale.forLanguageTag(user.getLangKey());
      Context context = new Context(locale);
      context.setVariable("user", user);
      context.setVariable("projectId", projectId);
      context.setVariable("baseUrl", baseUrl);
      context.setVariable("locale", locale);
      context.setVariable("role", messageSource.getMessage(roleKey, null, locale));
      String content = templateEngine.process(template, context);
      String subject = messageSource.getMessage(subjectKey, new Object[] {projectId}, locale);
      sendEmail(sender, new String[] {user.getEmail()}, null, null, subject, content, false, true);
    });
  }

  /**
   * Send a mail to users who are now able to edit the project.
   */
  @Async
  public void sendAssigneeGroupChangedMail(List<User> users, String projectId, String message,
      String sender, User currentUser) {

    if (!users.isEmpty()) {
      log.debug("Sending 'assignee group changed mail'");
    }

    users.parallelStream().forEach(user -> {
      Locale locale = Locale.forLanguageTag(user.getLangKey());
      Context context = new Context(locale);
      context.setVariable("user", user);
      context.setVariable("projectId", projectId);
      context.setVariable("locale", locale);
      context.setVariable("baseUrl", baseUrl);
      context.setVariable("messageToGroup", StringUtils.trimWhitespace(message));
      String content = templateEngine.process("assigneeGroupChanged", context);
      String subject = messageSource.getMessage("email.assignee-group-changed.title",
          new Object[] {projectId}, locale);
      sendEmail(sender, new String[] {user.getEmail()}, null,
          currentUser != null ? currentUser.getEmail() : null, subject, content, false, true);
    });
  }

  /**
   * Send a mail to data providers informing them that they've been removed as the assignee group by
   * a publisher.
   */
  @Async
  public void sendDataProviderAccessRevokedMail(List<User> users, String projectId, String message,
      String sender, User currentUser) {

    if (!users.isEmpty()) {
      log.debug("Sending 'data provider access revoked mail'");
    }

    users.parallelStream().forEach(user -> {
      Locale locale = Locale.forLanguageTag(user.getLangKey());
      Context context = new Context(locale);
      context.setVariable("user", user);
      context.setVariable("projectId", projectId);
      context.setVariable("locale", locale);
      context.setVariable("baseUrl", baseUrl);
      context.setVariable("messageToGroup", StringUtils.trimWhitespace(message));
      String content = templateEngine.process("dataProviderAccessRevoked", context);
      String subject = messageSource.getMessage("email.data-provider-access-revoked.title",
          new Object[] {projectId}, locale);
      sendEmail(sender, new String[] {user.getEmail()}, null,
          currentUser != null ? currentUser.getEmail() : null, subject, content, false, true);
    });
  }
}
