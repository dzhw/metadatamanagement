package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.helper;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.InstrumentSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

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
  public InstrumentCrudHelper(
      InstrumentRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      InstrumentChangesProvider instrumentChangesProvider,
      RestHighLevelClient elasticsearchClient,
      Gson gson
  ) {
    super(
        repository,
        applicationEventPublisher,
        elasticsearchUpdateQueueService,
        instrumentChangesProvider,
        elasticsearchClient,
        InstrumentSearchDocument.class,
        gson
    );
  }
}
