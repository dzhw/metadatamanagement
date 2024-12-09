package eu.dzhw.fdz.metadatamanagement.mailmanagement.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.JobStatusException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.JobStatusResponseException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.DaraPidApiException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.RegistrationClientException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.RegistrationResponseException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidClientService.RegistrationResponseParsingException;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraPidRegistrationService.RegistrationFailedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
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
import eu.dzhw.fdz.metadatamanagement.common.domain.TaskErrorNotification;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import joptsimple.internal.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

  private static final String GERMAN_TEXT_MAIL_HINT =
      "\n\n--\n Die HTML-Version dieser E-Mail enthält Links, die in dieser Version nicht"
      + " angezeigt werden.";

  private static final String ENGLISH_TEXT_MAIL_HINT =
      "\n\n--\n The HTML-version of this mail contains links which are not displayed in"
      + " this version.";

  private final JHipsterProperties jhipsterProperties;

  private final JavaMailSenderImpl javaMailSender;

  private final MessageSource messageSource;

  private final SpringTemplateEngine templateEngine;

  private final Environment env;

  @Value("${metadatamanagement.server.context-root}")
  private String baseUrl;

  private Future<Void> sendEmail(String from, String[] to, String[] cc, String bcc, String subject,
      String htmlContent, Locale locale) {
    log.debug("Send e-mail to '{}' with subject '{}' and htmlContent={}", to, subject, htmlContent);
    if (to.length < 1) {
      log.warn("No recipients specified for email!");
    }
    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message =
          new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
      message.setTo(to);
      if (cc != null && cc.length > 0) {
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

      String textContent = Jsoup.parse(htmlContent).body().wholeText()
          .replaceAll("\t", " ")
          .replaceAll(" {2,}", " ");

      if (Locale.GERMAN.equals(locale)) {
        textContent += GERMAN_TEXT_MAIL_HINT;
      } else {
        textContent += ENGLISH_TEXT_MAIL_HINT;
      }
      textContent = textContent.replaceAll("(\\n){3,}", "\n\n");

      message.setText(textContent, htmlContent);

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
    return sendEmail(null, new String[] {user.getEmail()}, null, null, subject, content, locale);
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
    return sendEmail(null, new String[] {user.getEmail()}, null, null, subject, content, locale);
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
        subject, content, Locale.ENGLISH);
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
        subject, content, Locale.ENGLISH);
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
      sendEmail(sender, new String[] {user.getEmail()}, null, null, subject, content, locale);
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
          currentUser != null ? currentUser.getEmail() : null, subject, content, locale);
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
      log.debug("Sending 'data provider access revoked' mail");
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
          currentUser != null ? currentUser.getEmail() : null, subject, content, locale);
    });
  }

  /**
   * Send the result of the dataset report generation to the user who has started the report
   * generation.
   *
   * @param user The user who has started the report generation.
   * @param dataSetId The id of the {@link DataSet} for which the report has been generated.
   * @param language The language in which the report has been generated.
   * @param sender The sender of the email.
   */
  @Async
  public void sendDataSetReportGeneratedMail(User user, String dataSetId, String language,
      String sender) {
    log.debug("Sending 'dataset report generated' mail");
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("dataSetId", dataSetId);
    context.setVariable("locale", locale);
    context.setVariable("language", language);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("datasetReportGeneratedEmail", context);
    String subject = messageSource.getMessage("email.dataset-report-generated.title",
        new Object[] {dataSetId, language}, locale);
    sendEmail(sender, new String[] {user.getEmail()}, null, null, subject, content, locale);
  }

  /**
   * Send the error during dataset report generation to the user who started the task and to all
   * admins.
   *
   * @param onBehalfUser The user who has started the report generation.
   * @param admins A list of admins.
   * @param sender The sender of the email.
   */
  @Async
  public void sendDataSetReportErrorMail(User onBehalfUser, List<User> admins,
      TaskErrorNotification errorNotification, String sender) {
    log.debug("Sending 'dataset report error' mail");
    Locale locale = Locale.forLanguageTag(onBehalfUser.getLangKey());
    Context context = new Context(locale);
    context.setVariable("onBehalfUser", onBehalfUser);
    context.setVariable("taskErrorNotification", errorNotification);
    context.setVariable("locale", locale);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("datasetReportErrorEmail", context);
    String subject = messageSource.getMessage("email.dataset-report-error.title",
        new Object[] {errorNotification.getDomainObjectId()}, locale);
    List<String> adminAddresses = admins.stream().map(User::getEmail).collect(Collectors.toList());
    sendEmail(sender, new String[] {onBehalfUser.getEmail()},
        adminAddresses.toArray(new String[adminAddresses.size()]), null, subject, content, locale);

  }

  /**
   * Send a mail to all release managers when the project is released with a new major version.
   *
   * @param releaseManagers List of ROLE_RELEASE_MANAGER
   * @param dataAcquisitionProjectId the id of the project which has been released
   * @param release the release object containing the version
   */
  public void sendMailOnNewMajorProjectRelease(List<User> releaseManagers,
      String dataAcquisitionProjectId, Release release) {
    log.debug("Sending 'new major project release' mail");
    Context context = new Context();
    context.setVariable("projectId", dataAcquisitionProjectId);
    context.setVariable("release", release);
    context.setVariable("baseUrl", baseUrl);
    context.setVariable("profiles", env.getActiveProfiles());
    String content = templateEngine.process("newMajorProjectRelease", context);
    String subject = "New Major Release for Project \"" + dataAcquisitionProjectId + "\" ("
        + release.getVersion() + ") on " + Strings.join(env.getActiveProfiles(), ",");
    List<String> emailAddresses =
        releaseManagers.stream().map(User::getEmail).collect(Collectors.toList());
    sendEmail(null, emailAddresses.toArray(new String[emailAddresses.size()]), null, null, subject,
        content, Locale.ENGLISH);
  }

  /**
   * Send the result of the data package overview generation to the user who has started the
   * overview generation.
   *
   * @param user The user who has started the overview generation.
   * @param dataPackageId The id of the {@link DataPackage} for which the overview has been
   *        generated.
   * @param language The language in which the report has been generated.
   * @param sender The sender of the email.
   */
  @Async
  public void sendDataPackageOverviewGeneratedMail(User user, String dataPackageId, String language,
      String sender) {
    log.debug("Sending 'data package overview generated' mail");
    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);
    context.setVariable("user", user);
    context.setVariable("dataPackageId", dataPackageId);
    context.setVariable("locale", locale);
    context.setVariable("language", language);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("dataPackageOverviewGeneratedEmail", context);
    String subject = messageSource.getMessage("email.datapackage-overview-generated.title",
        new Object[] {dataPackageId, language}, locale);
    sendEmail(sender, new String[] {user.getEmail()}, null, null, subject, content, locale);
  }

  /**
   * Send the error during data package overview generation to the user who started the task and to
   * all admins.
   *
   * @param onBehalfUser The user who has started the report generation.
   * @param admins A list of admins.
   * @param sender The sender of the email.
   */
  @Async
  public void sendDataPackageOverviewErrorMail(User onBehalfUser, List<User> admins,
      TaskErrorNotification errorNotification, String sender) {
    log.debug("Sending 'datapackage overview error' mail");
    Locale locale = Locale.forLanguageTag(onBehalfUser.getLangKey());
    Context context = new Context(locale);
    context.setVariable("onBehalfUser", onBehalfUser);
    context.setVariable("taskErrorNotification", errorNotification);
    context.setVariable("locale", locale);
    context.setVariable("baseUrl", baseUrl);
    String content = templateEngine.process("dataPackageOverviewErrorEmail", context);
    String subject = messageSource.getMessage("email.datapackage-overview-error.title",
        new Object[] {errorNotification.getDomainObjectId()}, locale);
    List<String> adminAddresses = admins.stream().map(User::getEmail).collect(Collectors.toList());
    sendEmail(sender, new String[] {onBehalfUser.getEmail()},
        adminAddresses.toArray(new String[adminAddresses.size()]), null, subject, content, locale);

  }

  /**
   * Sends an email notifying the user that the registration process completed successfully.
   * @param user the user that triggered the process by releasing a new project version
   * @param project the data acquisition project which got a new release
   * @param dataPackage the data package that was included in the project release
   */
  public void sendDaraPidRegistrationCompleteEmail(User user, DataAcquisitionProject project,
                                                           DataPackage dataPackage
  ) {
    // CPD-OFF
    log.debug("Sending da|ra PID registration complete e-mail to '{}'", user.getEmail());

    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);

    if (user.getFirstName() != null && !user.getFirstName().isBlank() &&
        user.getLastName() != null && !user.getLastName().isBlank()
    ) {
      context.setVariable("name", user.getFirstName() + " " + user.getLastName());
    } else {
      context.setVariable("name", user.getLogin());
    }
    context.setVariable("projectReleaseVersion", project.getRelease().getVersion());
    context.setVariable("projectUrl", String.format("%s/%s/data-packages/%s?page=1&size=10&type=surveys",
      this.baseUrl, user.getLangKey(), dataPackage.getMasterId().replace("$", "")));
    context.setVariable("dataPackageTitle", Objects.equals(user.getLangKey(), "de")
      ? dataPackage.getTitle().getDe()
      : dataPackage.getTitle().getEn());
    // CPD-ON

    String content = this.templateEngine.process("daraPidRegistrationCompleteEmail", context);
    String subject = this.messageSource.getMessage("email.dara-pid-registration-complete.subject", null, locale);

    this.sendEmail(null, new String[] {user.getEmail()}, null, null, subject, content, locale);
  }

  /**
   * Sends an Email for when the PID registration of variables linked to a data acquisition project fails.
   * @param user the user that triggered the process by releasing a new project version
   * @param admins list of admins that should also be notified
   * @param project the data acquisition project which got a new release
   * @param dataPackage the data package that was included in the project release
   * @param exception the exception that was thrown during the registration process
   */
  public void sendDaraPidRegistrationErrorEmail(User user, List<User> admins, DataAcquisitionProject project,
                                                DataPackage dataPackage, DaraPidApiException exception
  ) {
    log.debug("Sending da|ra PID registration error e-mail to '{}'", user.getEmail());

    Locale locale = Locale.forLanguageTag(user.getLangKey());
    Context context = new Context(locale);

    if (user.getFirstName() != null && !user.getFirstName().isBlank() &&
      user.getLastName() != null && !user.getLastName().isBlank()
    ) {
      context.setVariable("name", user.getFirstName() + " " + user.getLastName());
    } else {
      context.setVariable("name", user.getLogin());
    }
    context.setVariable("projectReleaseVersion", project.getRelease().getVersion());
    context.setVariable("projectUrl", String.format("%s/%s/data-packages/%s?page=1&size=10&type=surveys",
      this.baseUrl, user.getLangKey(), dataPackage.getMasterId().replace("$", "")));
    context.setVariable("dataPackageTitle", Objects.equals(user.getLangKey(), "de")
      ? dataPackage.getTitle().getDe()
      : dataPackage.getTitle().getEn());

    if (exception instanceof RegistrationResponseException rre) {
      context.setVariable("errorMessage", rre.getMessage());
      context.setVariable("statusCode", rre.getStatusCode());
      context.setVariable("body", rre.getBody());
    } else if (exception instanceof RegistrationClientException rce) {
      context.setVariable("stackTrace", ExceptionUtils.getStackTrace(rce));
    } else if (exception instanceof RegistrationResponseParsingException rrpe) {
      context.setVariable("stackTrace", ExceptionUtils.getStackTrace(rrpe));
    } else if (exception instanceof JobStatusResponseException jsre) {
      context.setVariable("errorMessage", jsre.getMessage());
      context.setVariable("statusCode", jsre.getStatusCode());
      context.setVariable("body", jsre.getBody());
    } else if (exception instanceof JobStatusException jse) {
      context.setVariable("stackTrace", ExceptionUtils.getStackTrace(jse));
    } else if (exception instanceof RegistrationFailedException rfe) {
      context.setVariable("errorMessage", rfe.getMessage());
      context.setVariable("failedEntries", rfe.getEntries());
    } else {
      throw new RuntimeException("Unexpected exception type: " + exception.getClass());
    }

    String content = this.templateEngine.process("daraPidRegistrationErrorEmail", context);
    String subject = this.messageSource.getMessage("email.dara-pid-registration-error.subject", null, locale);

    this.sendEmail(null, new String[] {user.getEmail()},
      admins.stream().map(User::getEmail).toList().toArray(new String[0]), null, subject, content, locale);
  }
}
