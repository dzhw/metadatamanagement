package eu.dzhw.fdz.metadatamanagement.common.rest;

import java.net.URI;

import javax.validation.Valid;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
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
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * Task REST Controller.
 *
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@Hidden
public class TaskResourceController
    extends GenericDomainObjectResourceController<Task, TaskManagementService> {

  private final UserApiService userApiService;

  private final TaskManagementService taskService;

  /**
   * Construct the controller.
   */
  public TaskResourceController(
      CrudService<Task> crudService,
      UserApiService userApiService,
      TaskManagementService taskService
  ) {
    super(crudService);
    this.userApiService = userApiService;
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
  @Hidden
  public ResponseEntity<Task> getDomainObject(@PathVariable String taskId) {
    return super.getDomainObject(taskId);
  }

  /**
   * Notify admins and optionally the user for whom the task was executed about an error.
   *
   * @param errorNotification A valid {@link TaskErrorNotification} object.
   */
  @PostMapping("/tasks/error-notification")
  @Hidden
  @Secured(value = {AuthoritiesConstants.TASK_USER})
  public ResponseEntity<?> reportTaskError(
      @RequestBody @Valid TaskErrorNotification errorNotification) {
    if (StringUtils.isEmpty(errorNotification.getOnBehalfOf())) {
      taskService.handleErrorNotification(errorNotification, null);
    } else {
      var user =
          userApiService.findOneByLogin(errorNotification.getOnBehalfOf());

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
