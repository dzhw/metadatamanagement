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
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

/**
 * Component which implements CRUD functions for all {@link DataPackage}s.
 * 
 * @author René Reitmann
 */
@Component
public class DataPackageCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<DataPackage, DataPackageRepository> {
  public DataPackageCrudHelper(DataPackageRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DataPackageChangesProvider changesProvider, RestHighLevelClient elasticsearchClient,
      UserInformationProvider userInformationProvider, Gson gson) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider,
        elasticsearchClient, DataPackageSearchDocument.class, userInformationProvider, gson);
  }
}
