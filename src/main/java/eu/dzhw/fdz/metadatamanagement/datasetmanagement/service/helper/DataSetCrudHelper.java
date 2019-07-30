package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import io.searchbox.client.JestClient;

/**
 * Component which implements CRUD functions for all {@link DataSet}s.
 * 
 * @author René Reitmann
 */
@Component
public class DataSetCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<DataSet, DataSetRepository> {
  public DataSetCrudHelper(DataSetRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DataSetChangesProvider dataSetChangesProvider, JestClient jestClient,
      UserInformationProvider userInformationProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService,
        dataSetChangesProvider, jestClient, DataSetSearchDocument.class, userInformationProvider);
  }
}
