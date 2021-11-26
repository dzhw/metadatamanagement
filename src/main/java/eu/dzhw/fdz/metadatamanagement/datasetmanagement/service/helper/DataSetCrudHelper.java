package eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.helper;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

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
  public DataSetCrudHelper(
      DataSetRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DataSetChangesProvider dataSetChangesProvider,
      RestHighLevelClient elasticsearchClient,
      Gson gson
  ) {
    super(
        repository,
        applicationEventPublisher,
        elasticsearchUpdateQueueService,
        dataSetChangesProvider,
        elasticsearchClient,
        DataSetSearchDocument.class,
        gson
    );
  }
}
