package eu.dzhw.fdz.metadatamanagement.projectmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;

/**
 * Spring Data MongoDB repository for the Dara update queue.
 * 
 * @author Daniel Katzberg
 *
 */
@Repository
public interface DaraUpdateQueueItemRepository extends 
    MongoRepository<DaraUpdateQueueItem, String>, DaraUpdateQueueItemRepositoryCustom {

  DaraUpdateQueueItem findOneByProjectId(String projectId);  
}
