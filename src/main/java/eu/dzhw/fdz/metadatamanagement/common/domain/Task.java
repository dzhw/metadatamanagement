package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.net.URI;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * entity for task handling.
 * 
 * @author tgehrke
 *
 */
@Document(collection = "tasks")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Task extends AbstractRdcDomainObject {
  private String id;
  private TaskState state;
  private URI location;
  private TaskType type;
  private String message;

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
    DATA_SET_REPORT;
  }
}
