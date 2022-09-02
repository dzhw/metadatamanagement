package eu.dzhw.fdz.metadatamanagement.common.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskState;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateNotFoundException;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class TaskServiceTest extends AbstractTest {
  @Autowired
  private TaskManagementService taskService;
  @Autowired
  private TaskRepository taskRepo;
  @Autowired
  private CounterService counterService;

  @AfterEach
  public void teardown() throws Exception {
    taskRepo.deleteAll();
  }

  @Test
  public void testCreateTask() {
    Task task = taskService.createTask(Task.TaskType.DATA_SET_REPORT);

    Optional<Task> result = taskRepo.findById(task.getId());
    if (result.isPresent()) {
      Task task2 = result.get();
      assertEquals(TaskState.RUNNING, task2.getState());
    } else {
      fail("result not present in db");
    }
  }

  @Test
  public void testHandleErrorTask() {
    String id = Long.toString(counterService.getNextSequence("tasks"));
    Task taskForError = taskRepo.insert(Task.builder().id(id).state(TaskState.RUNNING).build());
    Task result = taskService.handleErrorTask(taskForError,
        new TemplateNotFoundException("test.tex", null, "message"));
    assertEquals(id, result.getId());
    assertEquals(TaskState.FAILURE, result.getState());
    assertEquals(1, result.getErrorList().getErrors().size());

  }

  @Test
  public void testHandleTaskDone() {
    String id = Long.toString(counterService.getNextSequence("tasks"));
    String resultLocation = "/tmp/template.zip";
    Task taskForDone = taskRepo.insert(Task.builder().id(id).state(TaskState.RUNNING).build());
    Task result = taskService.handleTaskDone(taskForDone, resultLocation);
    assertEquals(id, result.getId());
    assertEquals("/public/files" + resultLocation, result.getLocation());
    assertEquals(TaskState.DONE, result.getState());
    assertNull(result.getErrorList());

  }

}
