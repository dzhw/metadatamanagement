package eu.dzhw.fdz.metadatamanagement.common.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;

/**
 * Rest controller to request task status.
 * 
 * @author tgehrke
 *
 */
@RestController
@RequestMapping("/api")
public class TaskResourceController
    extends GenericDomainObjectResourceController<Task, TaskRepository> {
  @Autowired
  public TaskResourceController(TaskRepository taskRepo) {
    super(taskRepo);
  }

  /**
   * get the task state by id.
   * 
   * @param taskId the Id of the task.
   * @return the task object.
   */
  @GetMapping("/tasks/{taskId}")
  public ResponseEntity<Task> getTaskStatus(@PathVariable String taskId) {
    return super.findDomainObject(taskId);
  }
}
