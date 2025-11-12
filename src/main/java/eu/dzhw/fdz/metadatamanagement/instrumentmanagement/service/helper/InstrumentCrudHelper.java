package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.InstrumentSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

/**
 * Component which implements CRUD functions for all {@link Instrument}s.
 *
 * @author René Reitmann
 */
@Component
public class InstrumentCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<Instrument, InstrumentRepository> {
  /**
   * Construct the helper.
   */
  public InstrumentCrudHelper(InstrumentRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      InstrumentChangesProvider instrumentChangesProvider, ElasticsearchClient elasticsearchClient,
      UserInformationProvider userInformationProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService,
        instrumentChangesProvider, elasticsearchClient, InstrumentSearchDocument.class,
        userInformationProvider);
  }
}
