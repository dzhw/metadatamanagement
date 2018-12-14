package eu.dzhw.fdz.metadatamanagement.common.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;


/**
 * controller to request task status.
 * 
 * @author tgehrke
 *
 */

@Controller
@RequestMapping("/api")
public class TaskResourceController
    extends GenericDomainObjectResourceController<Task, TaskRepository> {
  @Autowired
  public TaskResourceController(TaskRepository taskRepo) {
    super(taskRepo);
  }

  /**
   * get the task and return a specific http status depending on the task state.
   * 
   * @param taskId the Id of the task.
   * @return the task with specific http status.
   */
  @GetMapping("/tasks/{taskId}")
  public ResponseEntity<Task> getTaskStatus(@PathVariable String taskId) {

    return super.findDomainObject(taskId);
  }
}
