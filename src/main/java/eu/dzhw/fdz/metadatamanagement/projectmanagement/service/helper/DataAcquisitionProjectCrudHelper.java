package eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Component which implements CRUD functions for all {@link DataAcquisitionProjectCrudHelper}s.
 * 
 * @author René Reitmann
 */
@Component
public class DataAcquisitionProjectCrudHelper extends
    GenericShadowableDomainObjectCrudHelper<DataAcquisitionProject, DataAcquisitionProjectRepository> {
  public DataAcquisitionProjectCrudHelper(DataAcquisitionProjectRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      DataAcquisitionProjectChangesProvider changesProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider);
  }
}
