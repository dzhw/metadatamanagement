package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.loader.tools.RunProcess;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.model.ContainerOverride;
import com.amazonaws.services.ecs.model.DescribeServicesRequest;
import com.amazonaws.services.ecs.model.LaunchType;
import com.amazonaws.services.ecs.model.NetworkConfiguration;
import com.amazonaws.services.ecs.model.RunTaskRequest;
import com.amazonaws.services.ecs.model.TaskOverride;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties.ReportTask;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskState;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskType;
import eu.dzhw.fdz.metadatamanagement.common.domain.TaskErrorNotification;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing the domain object/aggregate {@link Task}.
 *
 * @author René Reitmann
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementService implements CrudService<Task> {
  private final TaskRepository taskRepo;

  private final CounterService counterService;

  private final UserApiService userApiService;

  private final MailService mailService;

  @Value("${metadatamanagement.server.instance-index}")
  private Integer instanceId;

  @Value("${metadatamanagement.projectmanagement.email}")
  private String projectManagementEmailSender;

  private final TaskCrudHelper crudHelper;

  private final Environment environment;

  private final MetadataManagementProperties metadataManagementProperties;

  @Autowired(required = false)
  private AmazonECS ecsClient;

  /**
   * Create a task.
   *
   * @param taskType The kind of task to create. Defined by {@link TaskType}
   * @return the created task.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public Task createTask(TaskType taskType) {
    String taskId = Long.toString(counterService.getNextSequence("tasks"));
    Task task = Task.builder().state(TaskState.RUNNING).id(taskId).type(taskType).build();
    return crudHelper.create(task);
  }

  /**
   * Mark the task as errored.
   *
   * @param task the task
   * @param exception the reason for failing.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public Task handleErrorTask(@NotNull Task task, Exception exception) {
    log.debug("Exception occurred in task {}", task.getId());
    task.setState(TaskState.FAILURE);
    task.setErrorList(createErrorListFromException(exception));
    return crudHelper.save(task);
  }

  /**
   * Handle all {@link TaskErrorNotification}s.
   *
   * @param errorNotification The details about the error.
   * @param onBehalfUser The User for whom the task has been executed.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public void handleErrorNotification(
      TaskErrorNotification errorNotification,
      UserDto onBehalfUser
  ) {
    switch (errorNotification.getTaskType()) {
      case DATA_SET_REPORT:
        handleDataSetReportError(errorNotification, onBehalfUser, projectManagementEmailSender);
        break;
      case DATA_PACKAGE_OVERVIEW:
        handleDataPackageOverviewError(errorNotification, onBehalfUser,
            projectManagementEmailSender);
        break;
      default:
        throw new NotImplementedException("Handling of errors for "
            + errorNotification.getTaskType() + " has not been implemented yet.");
    }
  }

  private void handleDataSetReportError(
      TaskErrorNotification errorNotification,
      UserDto onBehalfUser,
      String projectManagementEmailSender
  ) {
    var admins =
        userApiService.findAllByAuthoritiesContaining(AuthoritiesConstants.ADMIN);
    mailService.sendDataSetReportErrorMail(onBehalfUser, admins, errorNotification,
        projectManagementEmailSender);
  }

  private void handleDataPackageOverviewError(
      TaskErrorNotification errorNotification,
      UserDto onBehalfUser,
      String projectManagementEmailSender
  ) {
    var admins =
        userApiService.findAllByAuthoritiesContaining(AuthoritiesConstants.ADMIN);
    mailService.sendDataPackageOverviewErrorMail(onBehalfUser, admins, errorNotification,
        projectManagementEmailSender);
  }

  /**
   * Mark the task as done.
   *
   * @param task the task id
   * @param resultLocation the location to get the result processed by the task.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public Task handleTaskDone(@NotNull Task task, String resultLocation) {
    task.setState(TaskState.DONE);
    task.setLocation("/public/files" + resultLocation);
    return crudHelper.save(task);
  }

  private ErrorListDto createErrorListFromException(Exception exception) {
    ErrorListDto errorListDto = new ErrorListDto();
    log.debug("Handling exception", exception);
    if (exception instanceof TemplateException) {
      // The message of the exception is the error message of freemarker.
      // The manually added message for the dto can be translated into i18n strings
      String messageKey = "data-set-management.error.tex-template-error";
      errorListDto.add(new ErrorDto(null, messageKey, exception.getMessage(), null));
    } else if (exception instanceof TemplateIncompleteException) {
      TemplateIncompleteException te = (TemplateIncompleteException) exception;
      // All missing files
      for (String missingFile : te.getMissingFiles()) {
        errorListDto.add(new ErrorDto(null, te.getMessage(), missingFile, null));
      }
    } else {
      String messageKey = "data-set-management.error.io-error";
      errorListDto.add(new ErrorDto(null, messageKey, exception.getMessage(), null));
    }
    return errorListDto;
  }

  /**
   * Delete all completed tasks at 3 am.
   */
  @Scheduled(cron = "0 0 3 * * ?")
  public void deleteCompletedTasks() {
    if (instanceId != 0) {
      return;
    }
    log.debug("Starting deletion of completed tasks...");
    LocalDateTime yesterday = LocalDateTime.now().minusDays(14);
    taskRepo.deleteAllByStateAndCreatedDateBefore(TaskState.DONE, yesterday);
    taskRepo.deleteAllByStateAndCreatedDateBefore(TaskState.FAILURE, yesterday);
    log.debug("Finished deleting completed tasks.");
  }

  @Override
  public Optional<Task> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public void delete(Task domainObject) {
    crudHelper.delete(domainObject);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public Task save(Task domainObject) {
    return crudHelper.save(domainObject);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public Task create(Task domainObject) {
    String taskId = Long.toString(counterService.getNextSequence("tasks"));
    domainObject.setId(taskId);
    domainObject.setState(TaskState.RUNNING);
    return crudHelper.create(domainObject);
  }

  @Override
  public Optional<Task> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }

  /**
   * Start one container per language which builds the report/overview. Either via aws fargate or
   * locally via docker.
   *
   * @param id The id of the dataSet or data package for which the report/overview will be
   *        generated.
   * @param languages The languages in which we need to generate the report. Currently supported
   *        'de', 'en'.
   * @param version The version of the dataset report/data package overview.
   * @param onBehalfOf The name of the user which wants to generate the report.
   * @throws IOException in case the local task cannot be started
   */
  public void startReportTasks(String id, String version, List<String> languages,
      String onBehalfOf, TaskType taskType) throws IOException {
    for (String language : languages) {
      if (environment.acceptsProfiles(Profiles.of(Constants.SPRING_PROFILE_LOCAL))) {
        log.debug("Starting docker container from image report-task...");
        RunProcess dataSetReportTaskContainer =
            new RunProcess("src/main/resources/bin/run-report-task.sh", id, version,
                language, onBehalfOf, taskType.name());
        dataSetReportTaskContainer.run(false);
      } else {
        ReportTask taskProperties = metadataManagementProperties.getReportTask();
        log.info("Starting fargate task {}...", taskProperties.getTaskDefinition());
        NetworkConfiguration networkConfiguration = ecsClient
            .describeServices(
                new DescribeServicesRequest().withCluster(taskProperties.getClusterName())
                    .withServices(taskProperties.getServiceName()))
            .getServices().get(0).getNetworkConfiguration();

        RunTaskRequest req =
            new RunTaskRequest().withTaskDefinition(taskProperties.getTaskDefinition())
                .withNetworkConfiguration(networkConfiguration)
                .withCluster(taskProperties.getClusterName()).withLaunchType(LaunchType.FARGATE)
                .withCount(1).withStartedBy(
                    onBehalfOf)
                .withOverrides(new TaskOverride().withContainerOverrides(
                    new ContainerOverride().withName(taskProperties.getContainerName())
                        .withCommand(String.format(taskProperties.getStartCommand(), id,
                            version, language, onBehalfOf, taskType.name()).split("\\s+"))));
        ecsClient.runTask(req);
      }
    }
  }
}
