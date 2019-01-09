package eu.dzhw.fdz.metadatamanagement.common.service;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
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
 * Service to handle the management of long running tasks.
 *
 * @author tgehrke
 *
 */
@Slf4j
@Service
public class TaskService {
  @Autowired
  private TaskRepository taskRepo;

  @Autowired
  private CounterService counterService;

  /**
   * Create a task.
   *
   * @return the created task.
   */
  public Task createTask() {
    String taskId = Long.toString(counterService.getNextSequence(Task.class.getName()));
    Task task =
        Task.builder().state(TaskState.RUNNING).id(taskId).type(TaskType.DATA_SET_REPORT).build();
    return taskRepo.insert(task);
  }

  /**
   * Mark the task as errored.
   *
   * @param task the task
   * @param exception the reason for failing.
   */
  public Task handleErrorTask(@NotNull Task task, Exception exception) {
    log.debug("Exception occurred in task {}", task.getId());
    task.setState(TaskState.FAILURE);
    task.setErrorList(createErrorListFromException(exception));
    return taskRepo.save(task);

  }

  /**
   * Mark the task as done.
   * 
   * @param task the task id
   * @param resultLocation the location to get the result processed by the task.
   */
  public Task handleTaskDone(@NotNull Task task, String resultLocation) {
    task.setState(TaskState.DONE);
    task.setLocation(resultLocation);
    return taskRepo.save(task);
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
}
