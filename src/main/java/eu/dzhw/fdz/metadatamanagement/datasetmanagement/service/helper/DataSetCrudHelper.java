package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

/**
 * Component which implements CRUD functions for all {@link DataSet}s.
 *
 * @author René Reitmann
 */
@Component
public class DataSetCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<DataSet, DataSetRepository> {
  /**
   * Construct the helper.
   */
  public DataSetCrudHelper(DataSetRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DataSetChangesProvider dataSetChangesProvider, ElasticsearchClient elasticsearchClient,
      UserInformationProvider userInformationProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService,
        dataSetChangesProvider, elasticsearchClient, DataSetSearchDocument.class,
        userInformationProvider);
  }
}
