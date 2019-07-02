package eu.dzhw.fdz.metadatamanagement.common.rest;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.TaskErrorNotification;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;

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
  private UserService userService;

  private TaskService taskService;

  @Autowired
  public TaskResourceController(TaskRepository taskRepo, UserService userService,
      TaskService taskService) {
    super(taskRepo);
    this.userService = userService;
    this.taskService = taskService;
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

  /**
   * get the task state by id.
   * 
   * @param taskId the Id of the task.
   * @return the task object.
   */
  @PostMapping("/tasks/error-notification")
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> getTaskStatus(
      @RequestBody @Valid TaskErrorNotification errorNotification) {
    if (StringUtils.isEmpty(errorNotification.getOnBehalfOf())) {
      taskService.handleErrorNotification(errorNotification, null);
    } else {
      Optional<User> user =
          userService.getUserWithAuthoritiesByLogin(errorNotification.getOnBehalfOf());
      if (user.isPresent()) {
        taskService.handleErrorNotification(errorNotification, user.get());
      } else {
        return ResponseEntity.badRequest()
            .body("User with name '" + errorNotification.getOnBehalfOf() + "' does not exist!");
      }
    }
    return ResponseEntity.ok().build();
  }
}
