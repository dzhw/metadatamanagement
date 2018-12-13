package eu.dzhw.fdz.metadatamanagement.common.service;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskState;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskType;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * Service to handle the task management.
 * 
 * @author tgehrke
 *
 */
@Slf4j
@Service
public class TaskService {
  @Autowired
  TaskRepository taskRepo;

  /**
   * Create a task with given id and URI for polling the task state.
   * 
   * @param taskId the task id
   * 
   * @return the created task.
   */
  public Task createTask(String taskId) {
    Task task =
        Task.builder().state(TaskState.RUNNING).id(taskId).type(TaskType.DATA_SET_REPORT).build();
    taskRepo.insert(task);
    return task;
  }

  /**
   * save task as error.
   * 
   * @param taskId the task
   * @param exception the reason for failing.
   */
  public void handleErrorTask(String taskId, Exception exception) {
    ErrorListDto errorList = crerateErrorListFromException(exception);
    Optional<Task> findById = loadTask(taskId);
    if (findById.isPresent()) {
      Task task = findById.get();
      task.setState(TaskState.FAILURE);
      task.setErrorList(errorList);
      taskRepo.save(task);
    } else {
      log.warn("task with id {} not exists", taskId);
    }
  }

  /**
   * save task as done.
   * 
   * @param taskId the task id
   * @param resultUri the URI to handle the task result
   */
  public void handleTaskDone(String taskId, URI resultUri) {
    Optional<Task> optionalTask = loadTask(taskId);
    if (optionalTask.isPresent()) {
      Task task = optionalTask.get();
      task.setState(TaskState.DONE);
      task.setLocation(resultUri);
      taskRepo.save(task);
    } else {
      log.warn("task with id {} not exists", taskId);
    }
  }

  private Optional<Task> loadTask(String taskId) {
    return taskRepo.findById(taskId);
  }

  /**
   * ececuter config for dataset repot.
   * 
   * @return the executor.
   */
  @Bean(name = "datasetReportExecutor")
  public TaskExecutor datasetReportExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("MyThread-");
    executor.initialize();
    return executor;
  }

  private ErrorListDto crerateErrorListFromException(Exception exception) {
    ErrorListDto errorListDto = new ErrorListDto();

    if (exception instanceof TemplateException) {
      // The message of the exception is the error message of freemarker.
      // The manually added message for the dto can be translated into i18n strings
      String messageKey = "data-set-management.error.tex-template-error";
      errorListDto.add(new ErrorDto(null, messageKey, exception.getMessage(), null));
    }
    if (exception instanceof TemplateIncompleteException) {
      TemplateIncompleteException te = (TemplateIncompleteException) exception;
      // All missing files
      for (String missingFile : te.getMissingFiles()) {
        errorListDto.add(new ErrorDto(null, te.getMessage(), missingFile, null));
      }
    }
    return errorListDto;
  }

  /**
   * Get the Task by Id.
   * 
   * @param taskId the id of the task
   * @return the task or null if not present.
   */
  public Task getTask(String taskId) {
    Optional<Task> loadTask = loadTask(taskId);
    return loadTask.orElse(null);
  }
}
