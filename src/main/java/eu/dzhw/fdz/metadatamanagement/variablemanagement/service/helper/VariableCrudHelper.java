package eu.dzhw.fdz.metadatamanagement.variablemanagement.service.helper;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.common.service.GenericShadowableDomainObjectCrudHelper;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
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
  public VariableCrudHelper(VariableRepository repository,
      ApplicationEventPublisher applicationEventPublisher,
      ElasticsearchUpdateQueueService elasticsearchUpdateQueueService,
      VariableChangesProvider changesProvider, RestHighLevelClient elasticsearchClient,
      UserInformationProvider userInformationProvider, Gson gson) {
    super(repository, applicationEventPublisher, elasticsearchUpdateQueueService, changesProvider,
        elasticsearchClient, VariableSearchDocument.class, userInformationProvider, gson);
  }
}
