package eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;

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
      QuestionChangesProvider changesProvider, ElasticsearchClient elasticsearchClient,
      UserInformationProvider userInformationProvider) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider,
        elasticsearchClient, QuestionSearchDocument.class, userInformationProvider);
  }
}
