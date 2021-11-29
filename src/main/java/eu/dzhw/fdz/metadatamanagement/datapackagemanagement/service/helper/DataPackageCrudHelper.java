package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageChangesProvider;

/**
 * Component which implements CRUD functions for all {@link DataPackage}s.
 *
 * @author René Reitmann
 */
@Component
public class DataPackageCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<DataPackage, DataPackageRepository> {

  /**
   * Build the helper.
   *
   * @param repository the repository the helper will use
   * @param applicationEventPublisher the publisher for application events
   * @param elasticsearchUpdateQueueService the service which will be used to update Elasticsearch
   * @param changesProvider the provider which provides changes to any DataPackage(s)
   * @param elasticsearchClient the client which will be used to connect to Elasticsearch
   * @param gson the GSON helper this class will use
   */
  public DataPackageCrudHelper(
      DataPackageRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DataPackageChangesProvider changesProvider,
      RestHighLevelClient elasticsearchClient,
      Gson gson
  ) {
    super(
        repository,
        applicationEventPublisher,
        elasticsearchUpdateQueueService,
        changesProvider,
        elasticsearchClient,
        DataPackageSearchDocument.class,
        gson
    );
  }
}
