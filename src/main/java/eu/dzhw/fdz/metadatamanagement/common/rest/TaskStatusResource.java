package eu.dzhw.fdz.metadatamanagement.common.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskService;


/**
 * controller to request task status.
 * 
 * @author tgehrke
 *
 */

@Controller
@RequestMapping("/api")
public class TaskStatusResource {
  @Autowired
  TaskService taskService;

  /**
   * get the task and return a specific http status depending on the task state.
   * 
   * @param taskId the Id of the task.
   * @return the task with specific http status.
   */
  @GetMapping("/tasks/{taskId}")
  public ResponseEntity<Task> getTaskStatus(@PathVariable String taskId) {
    Task task = taskService.getTask(taskId);
    if (task == null) {
      return ResponseEntity.notFound().build();
    }
    switch (task.getState()) {
      case DONE:
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(task);
      case FAILURE:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(task);
      case RUNNING:
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(task);
      default:
        return ResponseEntity.status(HttpStatus.SEE_OTHER).body(task);

    }
  }
}
