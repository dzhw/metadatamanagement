package eu.dzhw.fdz.metadatamanagement.common.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;

/**
 * repository to handle task persistence.
 * @author tgehrke
 *
 */
@RepositoryRestResource(path = "/studies")
public interface TaskRepository extends BaseRepository<Task, String> {

}
