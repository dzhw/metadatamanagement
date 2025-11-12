package eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

/**
 * Component which implements CRUD functions for all {@link Survey}s.
 *
 * @author René Reitmann
 */
@Component
public class SurveyCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<Survey, SurveyRepository> {
  public SurveyCrudHelper(SurveyRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      ElasticsearchClient elasticsearchClient, UserInformationProvider userInformationProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, null,
        elasticsearchClient, SurveySearchDocument.class, userInformationProvider);
  }
}
