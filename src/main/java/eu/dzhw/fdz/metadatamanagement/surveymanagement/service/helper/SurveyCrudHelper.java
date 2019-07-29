package eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

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
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, null);
  }
}
