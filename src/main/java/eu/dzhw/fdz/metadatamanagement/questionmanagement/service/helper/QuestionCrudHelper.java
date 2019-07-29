package eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Component which implements CRUD functions for all {@link Question}s.
 * 
 * @author René Reitmann
 */
@Component
public class QuestionCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<Question, QuestionRepository> {
  public QuestionCrudHelper(QuestionRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      QuestionChangesProvider changesProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider);
  }
}
