package eu.dzhw.fdz.metadatamanagement.common.service;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskState;
import eu.dzhw.fdz.metadatamanagement.common.repository.TaskRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateNotFoundException;

@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class TaskServiceTest extends AbstractTest {


  @Autowired
  private TaskService taskService;
  @Autowired
  private TaskRepository taskRepo;
  @Autowired
  CounterService counterService;

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testCreateTask() {
    String id = Long.toString(counterService.getNextSequence(Task.class.getName()));;
    taskService.createTask(id);

    Optional<Task> result = taskRepo.findById(id);
    if (result.isPresent()) {
      Task task = result.get();
      assertEquals(id, task.getId());
      assertEquals(TaskState.RUNNING, task.getState());
    } else {
      fail("result not present in db");
    }
  }

  @Test
  public void testHandleErrorTask() {
    String id = Long.toString(counterService.getNextSequence(Task.class.getName()));;
    Task taskForError = taskRepo.insert(Task.builder().id(id).state(TaskState.RUNNING).build());
    Task result = taskService.handleErrorTask(taskForError,
        new TemplateNotFoundException("test.tex", null, "message"));
    assertEquals(id, result.getId());
    assertEquals(TaskState.FAILURE, result.getState());
    assertEquals(1, result.getErrorList().getErrors().size());

  }

  @Test
  public void testHandleTaskDone() {
    String id = Long.toString(counterService.getNextSequence(Task.class.getName()));;
    String resultLocation = "/tmp/template.zip";
    Task taskForDone = taskRepo.insert(Task.builder().id(id).state(TaskState.RUNNING).build());
    Task result = taskService.handleTaskDone(taskForDone, resultLocation);
    assertEquals(id, result.getId());
    assertEquals(resultLocation, result.getLocation());
    assertEquals(TaskState.DONE, result.getState());
    assertNull(result.getErrorList());
    
  }

}
