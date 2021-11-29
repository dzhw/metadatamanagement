package eu.dzhw.fdz.metadatamanagement.questionmanagement.service.helper;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;

/**
 * Component which implements CRUD functions for all {@link Question}s.
 *
 * @author René Reitmann
 */
@Component
public class QuestionCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<Question, QuestionRepository> {

  /**
   * Build the helper.
   *
   * @param repository the repository the helper will use
   * @param applicationEventPublisher the publisher for application events
   * @param elasticsearchUpdateQueueService the service which will be used to update Elasticsearch
   * @param changesProvider the provider which provides changes to any Question(s)
   * @param elasticsearchClient the client which will be used to connect to Elasticsearch
   * @param gson the GSON helper this class will use
   */
  public QuestionCrudHelper(
      QuestionRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      QuestionChangesProvider changesProvider,
      RestHighLevelClient elasticsearchClient,
      Gson gson
  ) {
    super(
        repository,
        applicationEventPublisher,
        elasticsearchUpdateQueueService,
        changesProvider,
        elasticsearchClient,
        QuestionSearchDocument.class,
        gson
    );
  }
}
