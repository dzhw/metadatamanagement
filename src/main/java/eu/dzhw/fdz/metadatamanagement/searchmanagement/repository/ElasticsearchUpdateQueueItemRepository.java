package eu.dzhw.fdz.metadatamanagement.searchmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;

/**
 * Spring Data MongoDB repository for the Elasticsearch update queue.
 */
@RepositoryRestResource(exported = false)
public interface ElasticsearchUpdateQueueItemRepository
    extends MongoRepository<ElasticsearchUpdateQueueItem, String>,
    QueryDslPredicateExecutor<ElasticsearchUpdateQueueItem>,
    ElasticsearchUpdateQueueItemRepositoryCustom {

}
