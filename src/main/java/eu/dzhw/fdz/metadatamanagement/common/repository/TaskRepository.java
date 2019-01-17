package eu.dzhw.fdz.metadatamanagement.common.repository;

import java.time.LocalDateTime;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;
import eu.dzhw.fdz.metadatamanagement.common.domain.Task.TaskState;

/**
 * repository to handle task persistence.
 * 
 * @author tgehrke
 *
 */
public interface TaskRepository extends BaseRepository<Task, String> {
  void deleteAllByStateAndCreatedDateBefore(TaskState state, LocalDateTime createdDate);
}
