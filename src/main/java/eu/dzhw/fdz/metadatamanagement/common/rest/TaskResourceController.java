package eu.dzhw.fdz.metadatamanagement.common.rest;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.TaskErrorNotification;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.common.service.TaskManagementService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;

/**
 * Task REST Controller.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class TaskResourceController
    extends GenericDomainObjectResourceController<Task, TaskManagementService> {
  private final UserService userService;
  
  private final TaskManagementService taskService;

  /**
   * Construct the controller.
   */
  public TaskResourceController(CrudService<Task> crudService, UserService userService,
      TaskManagementService taskService) {
    super(crudService);
    this.userService = userService;
    this.taskService = taskService;
  }

  /**
   * get the task state by id.
   * 
   * @param taskId the Id of the task.
   * @return the task object.
   */
  @Override
  @GetMapping("/tasks/{taskId}")
  public ResponseEntity<Task> getDomainObject(@PathVariable String taskId) {
    return super.getDomainObject(taskId);
  }

  /**
   * Notify admins and optionally the user for whom the task was executed about an error.
   * 
   * @param errorNotification A valid {@link TaskErrorNotification} object.
   */
  @PostMapping("/tasks/error-notification")
  @Secured(value = {AuthoritiesConstants.TASK_USER})
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

  @Override
  protected URI buildLocationHeaderUri(Task domainObject) {
    return UriComponentsBuilder.fromPath("/api/tasks/" + domainObject.getId()).build().toUri();
  }
}
