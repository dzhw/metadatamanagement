package eu.dzhw.fdz.metadatamanagement.common.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO holding all information required for sending notifications to users in case an error occurred
 * during task execution.
 * 
 * @author René Reitmann
 */
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaskErrorNotification {

  /**
   * The name of the user for which the task has been executed. May be empty.
   */
  private String onBehalfOf;

  /**
   * An error message indicating the reason of the error. Must not be empty.
   */
  @NotEmpty
  private String errorMessage;

  /**
   * The type of the task which has been executed. Must not be empty.
   */
  @NotNull
  private TaskType taskType;

  /**
   * An id of a domainObject. May be empty.
   */
  private String domainObjectId;
}
