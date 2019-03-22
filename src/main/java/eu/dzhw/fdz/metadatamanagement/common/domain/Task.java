package eu.dzhw.fdz.metadatamanagement.common.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Task entity holding the current state of a long running task.
 * 
 * @author tgehrke
 */
@Document(collection = "tasks")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Task extends AbstractRdcDomainObject {
  /**
   * The id or task number of the task.
   */
  @Id
  private String id;
  
  /**
   * The current state of the task.
   */
  private TaskState state;
  
  /**
   * The location URI of the result of the task.
   */
  private String location;
  
  /**
   * The type of the task.
   */
  private TaskType type;
  
  /**
   * The list of errors which occurred during execution of the task.
   */
  private ErrorListDto errorList;

  /**
   * State of tasks.
   * 
   * @author tgehrke
   *
   */
  public enum TaskState {
    RUNNING, DONE, FAILURE;
  }

  /**
   * type of tasks.
   * 
   * @author tgehrke
   *
   */
  public enum TaskType {
    DATA_SET_REPORT,
    PROJECT_RELEASE
  }
}
