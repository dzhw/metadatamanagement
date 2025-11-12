package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.helper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

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
      DataPackageChangesProvider changesProvider, ElasticsearchClient elasticsearchClient,
      UserInformationProvider userInformationProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider,
        elasticsearchClient, DataPackageSearchDocument.class, userInformationProvider);
  }
}
