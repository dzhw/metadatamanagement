package eu.dzhw.fdz.metadatamanagement.searchmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;

/**
 * Spring Data MongoDB repository for the Elasticsearch update queue.
 */
@RepositoryRestResource(exported = false)
public interface ElasticsearchUpdateQueueItemRepository
    extends MongoRepository<ElasticsearchUpdateQueueItem, String>,
    QueryDslPredicateExecutor<ElasticsearchUpdateQueueItem>,
    ElasticsearchUpdateQueueItemRepositoryCustom {
  
  ElasticsearchUpdateQueueItem findOneByDocumentTypeAndDocumentIdAndAction(
      ElasticsearchType documentType, String documentId, ElasticsearchUpdateQueueAction action);

}
