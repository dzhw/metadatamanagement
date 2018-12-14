package eu.dzhw.fdz.metadatamanagement.common.service;

import java.net.URI;
import java.util.Optional;

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
  public void handleErrorTask(@NotNull Task task, Exception exception) {
      task.setState(TaskState.FAILURE);
      task.setErrorList(crerateErrorListFromException(exception));
      taskRepo.save(task);
    
  }

  /**
   * save task as done.
   * 
   * @param taskId the task id
   * @param resultUri the URI to handle the task result
   */
  public void handleTaskDone(@NotNull Task task, URI resultUri) {
      task.setState(TaskState.DONE);
      task.setLocation(resultUri);
      taskRepo.save(task);
  }

  private ErrorListDto crerateErrorListFromException(Exception exception) {
    ErrorListDto errorListDto = new ErrorListDto();
    log.info("handle exception", exception);
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
}
