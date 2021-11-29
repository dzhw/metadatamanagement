package eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableChangesProvider;

/**
 * Component which implements CRUD functions for all {@link Variable}s.
 *
 * @author René Reitmann
 */
@Component
public class VariableCrudHelper
    extends GenericShadowableDomainObjectCrudHelper<Variable, VariableRepository> {

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
  public VariableCrudHelper(
      VariableRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      VariableChangesProvider changesProvider,
      RestHighLevelClient elasticsearchClient,
      Gson gson
  ) {
    super(
        repository,
        applicationEventPublisher,
        elasticsearchUpdateQueueService, changesProvider,
        elasticsearchClient,
        VariableSearchDocument.class,
        gson
    );
  }
}
