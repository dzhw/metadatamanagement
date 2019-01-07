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
  @Id
  private String id;
  private TaskState state;
  private String location;
  private TaskType type;
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
    DATA_SET_REPORT;
  }
}
