package eu.dzhw.fdz.metadatamanagement.studymanagement.service.helper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyChangesProvider;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import io.searchbox.client.JestClient;

/**
 * Component which implements CRUD functions for all {@link Study}s.
 * 
 * @author René Reitmann
 */
@Component
public class StudyCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<Study, StudyRepository> {
  public StudyCrudHelper(StudyRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      StudyChangesProvider changesProvider, JestClient jestClient,
      UserInformationProvider userInformationProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider,
        jestClient, StudySearchDocument.class, userInformationProvider);
  }
}
