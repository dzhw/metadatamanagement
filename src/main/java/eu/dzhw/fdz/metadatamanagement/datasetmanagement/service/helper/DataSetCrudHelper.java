package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

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
      DataSetChangesProvider dataSetChangesProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService,
        dataSetChangesProvider);
  }
}
