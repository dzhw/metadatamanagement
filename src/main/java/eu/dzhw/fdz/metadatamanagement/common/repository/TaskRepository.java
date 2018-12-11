package eu.dzhw.fdz.metadatamanagement.common.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import eu.dzhw.fdz.metadatamanagement.common.domain.Task;

/**
 * repository to handle task persostence.
 * @author tgehrke
 *
 */
public interface TaskRepository extends MongoRepository<Task, String> {

}
