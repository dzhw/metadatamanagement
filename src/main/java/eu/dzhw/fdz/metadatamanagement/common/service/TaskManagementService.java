package eu.dzhw.fdz.metadatamanagement.common.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskState;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskType;
import eu.dzhw.fdz.metadatamanagement.common.domain.TaskErrorNotification;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing the domain object/aggregate {@link Task}.
 * 
 * @author René Reitmann
 */
@Slf4j
@Service
public class TaskManagementService implements CrudService<Task> {
  private final TaskRepository taskRepo;

  private final CounterService counterService;

  private final UserRepository userRepository;

  private final MailService mailService;
  
  private final Integer instanceId;

  private final String projectManagementEmailSender;

  private final TaskCrudHelper crudHelper;

  public TaskManagementService(TaskRepository taskRepo, CounterService counterService,
      UserRepository userRepository, MailService mailService, 
      @Value("${metadatamanagement.server.instance-index}") Integer instanceId,
      @Value("${metadatamanagement.projectmanagement.email}") String projectManagementEmailSender,
      TaskCrudHelper crudHelper) {
    super();
    this.taskRepo = taskRepo;
    this.counterService = counterService;
    this.userRepository = userRepository;
    this.mailService = mailService;
    this.instanceId = instanceId;
    this.projectManagementEmailSender = projectManagementEmailSender;
    this.crudHelper = crudHelper;
  }

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
   * @param onBehalfUser The {@link User} for whom the task has been executed.
   */
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.ADMIN,
      AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.TASK_USER})
  public void handleErrorNotification(TaskErrorNotification errorNotification, User onBehalfUser) {
    switch (errorNotification.getTaskType()) {
      case DATA_SET_REPORT:
        handleDataSetReportError(errorNotification, onBehalfUser, projectManagementEmailSender);
        break;
      default:
        throw new NotImplementedException("Handling of errors for "
            + errorNotification.getTaskType() + " has not been implemented yet.");
    }
  }

  private void handleDataSetReportError(TaskErrorNotification errorNotification, User onBehalfUser,
      String projectManagementEmailSender) {
    List<User> admins =
        userRepository.findAllByAuthoritiesContaining(new Authority(AuthoritiesConstants.ADMIN));
    mailService.sendDataSetReportErrorMail(onBehalfUser, admins, errorNotification,
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
    log.info("Starting deletion of completed tasks...");
    LocalDateTime yesterday = LocalDateTime.now().minusDays(14);
    taskRepo.deleteAllByStateAndCreatedDateBefore(TaskState.DONE, yesterday);
    taskRepo.deleteAllByStateAndCreatedDateBefore(TaskState.FAILURE, yesterday);
    log.info("Finished deleting completed tasks.");
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
}
