package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.helper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Component which implements CRUD functions for all {@link RelatedPublication}s.
 * 
 * @author René Reitmann
 */
@Component
public class RelatedPublicationCrudHelper
    extends GenericDomainObjectCrudHelper<RelatedPublication, RelatedPublicationRepository> {
  public RelatedPublicationCrudHelper(RelatedPublicationRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      RelatedPublicationChangesProvider changesProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider);
  }
}
