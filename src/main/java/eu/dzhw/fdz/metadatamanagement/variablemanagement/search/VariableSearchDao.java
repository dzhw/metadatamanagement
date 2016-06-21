package eu.dzhw.fdz.metadatamanagement.variablemanagement.search;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchDocumentSaveException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.search.document.VariableSearchDocument;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

/**
 * Data access object for saving variables in elasticsearch.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Component
public class VariableSearchDao {
  /** The type of saved variables in the elasticsearch indices. */
  public static final String TYPE = ElasticsearchType.variables.name();

  private final Logger log = LoggerFactory.getLogger(VariableSearchDao.class);

  /**
   * JestClient for the communication with elasticsearch. No use of the spring boot elasticsearch
   * client.
   */
  @Inject
  private JestClient jestClient;

  @Inject
  private SurveyRepository surveyRepository;

  /**
   * Bulk save the given variables to elasticsearch.
   * 
   * @param variables the variables to save
   */
  public void index(List<Variable> variables) {
    if (variables == null || variables.isEmpty()) {
      return;
    }
    for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
      index(variables.stream()
          .map(variable -> new VariableSearchDocument(variable, getSurveys(variable), index))
          .collect(Collectors.toList()), index.getIndexName());
    }
  }

  private void index(List<VariableSearchDocument> variableSearchDocuments, String index) {
    Bulk.Builder builder = new Bulk.Builder().defaultIndex(index)
        .defaultType(TYPE);
    for (VariableSearchDocument variableSearchDocument : variableSearchDocuments) {
      builder.addAction(new Index.Builder(variableSearchDocument).id(variableSearchDocument.getId())
          .build());
    }
    Bulk bulk = builder.build();
    JestResult result = execute(bulk);
    if (!result.isSucceeded()) {
      throw new ElasticsearchDocumentSaveException(index, TYPE, result.getErrorMessage());
    }
  }

  private Iterable<Survey> getSurveys(Variable variable) {
    Iterable<Survey> surveys = null;
    if (variable.getSurveyIds() != null) {
      surveys = surveyRepository.findAll(variable.getSurveyIds());
    }
    return surveys;
  }

  /**
   * Return ALL variables stored in the given index.
   * 
   * @return A List of ALL variables in elasticsearch.
   */
  public List<VariableSearchDocument> findAll(String index) {
    SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
    queryBuilder.query(QueryBuilders.matchAllQuery());
    return this.findAllByQueryBuilder(queryBuilder, index);
  }

  /**
   * This is a find all method by a queryBuilder. The QueryBuilder can include aggregations, filter
   * or complex search queries.
   * 
   * @param queryBuilder A querybuilder with an defined query.
   * @param index the name of a index within elasticseach
   * @return the elasticsearch result as list
   */
  private List<VariableSearchDocument> findAllByQueryBuilder(SearchSourceBuilder queryBuilder,
      String index) {
    Search search = new Search.Builder(queryBuilder.toString()).addIndex(index)
        .addType(TYPE)
        .build();
    JestResult result = execute(search);

    if (!result.isSucceeded()) {
      log.warn("Unable to load variable from index " + index + ": " + result.getErrorMessage());
      return null;
    }

    return result.getSourceAsObjectList(VariableSearchDocument.class);
  }

  /**
   * Execute queries to elasticsearch.
   * 
   * @param action a query for elasticsearch
   * @return the result from elasticsearch by the jest client.
   */
  private JestResult execute(Action<?> action) {
    try {
      return jestClient.execute(action);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }
}
