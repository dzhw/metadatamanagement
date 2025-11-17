package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ConceptSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Component which implements CRUD functions for all {@link Concept}s.
 *
 * @author René Reitmann
 */
@Component
public class ConceptCrudHelper extends GenericDomainObjectCrudHelper<Concept, ConceptRepository> {
  public ConceptCrudHelper(ConceptRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      ElasticsearchClient elasticsearchClient) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, null,
        elasticsearchClient, ConceptSearchDocument.class);
  }
}
