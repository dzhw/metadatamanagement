package eu.dzhw.fdz.metadatamanagement.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.search.document.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchDocumentDeleteException;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchDocumentSaveException;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchIoException;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.Refresh;

/**
 * Data access object for saving variables in elasticsearch.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Component
public class VariableSearchDao {

  /** The type of saved variables in the elasticsearch indices. */
  public static final String TYPE = "variables";

  private final Logger log = LoggerFactory.getLogger(VariableSearchDao.class);

  /** JestClient for the communication with elasticsearch. 
   * No use of the spring boot elasticsearch client. */
  @Inject
  private JestClient jestClient;

  /**
   * Save the given variable to the given index.
   * 
   * @param variableSearchDocument the variable to save
   * @param index the name of the index
   */
  public void save(VariableSearchDocument variableSearchDocument, String index) {
    JestResult result = execute(new Index.Builder(variableSearchDocument).index(index)
        .type(TYPE)
        .build());
    if (!result.isSucceeded()) {

      String id = "null";
      if (variableSearchDocument != null) {
        id = variableSearchDocument.getId();
      }

      throw new ElasticsearchDocumentSaveException(index, TYPE, id, result.getErrorMessage());
    }
  }

  /**
   * Bulk save the given variables to the given index.
   * 
   * @param variableSearchDocuments the variables to save
   * @param index the name of the index
   */
  public void save(List<VariableSearchDocument> variableSearchDocuments, String index) {
    if (variableSearchDocuments == null || variableSearchDocuments.isEmpty()) {
      return;
    }

    Bulk.Builder builder = new Bulk.Builder().defaultIndex(index)
        .defaultType(TYPE);
    for (VariableSearchDocument variableSearchDocument : variableSearchDocuments) {
      builder.addAction(new Index.Builder(variableSearchDocument).build());
    }
    Bulk bulk = builder.build();
    JestResult result = execute(bulk);
    if (!result.isSucceeded()) {
      throw new ElasticsearchDocumentSaveException(index, TYPE, result.getErrorMessage());
    }
  }

  /**
   * Return ALL variables stored in the given index.
   * 
   * @param index The index to query
   * @return A List of ALL variables
   */
  public List<VariableSearchDocument> findAll(String index) {
    SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
    queryBuilder.query(QueryBuilders.matchAllQuery());
    return this.findAllByQueryBuilder(queryBuilder, index);
  }

  /**
   * Delete the {@link VariableSearchDocument} with the given id.
   * 
   * @param id the id of the document to delete
   */
  public void delete(String id, String index) {
    JestResult result = execute(new Delete.Builder(id).index(index)
        .type(TYPE)
        .build());
    if (!result.isSucceeded()) {
      throw new ElasticsearchDocumentDeleteException(index, TYPE, id, result.getErrorMessage());
    }
  }

  /**
   * This method deletes elements by a given field and the depending values within an index.
   * 
   * @param fieldName the name of a field of the document
   * @param value the value of the fieldName
   * @param index the intex, where the document is saved.
   */  
  private void deleteByField(String fieldName, String value, String index) {

    // Search elements by field
    SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
    queryBuilder.query(QueryBuilders.boolQuery()
        .filter(QueryBuilders.matchQuery(fieldName, value)));
    List<VariableSearchDocument> variableSearchDocumentList =
        this.findAllByQueryBuilder(queryBuilder, index);

    // delete elements
    for (VariableSearchDocument variableSearchDocument : variableSearchDocumentList) {
      this.delete(variableSearchDocument.getId(), index);
    }
  }

  /**
   * This is a find all method by a queryBuilder. The QueryBuilder can include aggregations, filter
   * or complex search queries.
   * 
   * @param queryBuilder A querybuilder with an defined query.
   * @param index the name of a index within elasticseach
   * @return a list of values with matches by the filter.
   */
  private List<VariableSearchDocument> findAllByQueryBuilder(SearchSourceBuilder queryBuilder,
      String index) {
    Search search = new Search.Builder(queryBuilder.toString()).addIndex(index)
        .addType(TYPE)
        .build();
    JestResult result = execute(search);

    if (!result.isSucceeded()) {
      log.warn("Unable to load variable search documents from index " + index + ": "
          + result.getErrorMessage());
      return new ArrayList<>();
    }

    return result.getSourceAsObjectList(VariableSearchDocument.class);
  }

  /**
   * Sends a delete query by a given fdz project name to elasticsearch.
   * @param fdzProjectName the name of a fdzproject
   * @param index the name of the elasticsearch index
   */
  public void deleteByFdzProjectName(String fdzProjectName, String index) {
    deleteByField("fdzProjectName", fdzProjectName, index);
  }

  /**
   * Sends a delete query by a given survey id to elasticsearch.
   * @param surveyId the id of a survey
   * @param index the name of the elasticsearch index.
   */
  //TODO Katzberg writing unit tests for check the boolquery (filter since 2.0.0)
  public void deleteBySurveyId(String surveyId, String index) {
    deleteByField("surveyId", surveyId, index);
  }

  /**
   * Refresh the given index synchronously.
   * 
   * @param index the index to refresh.
   */
  public void refresh(String index) {
    JestResult result = execute(new Refresh.Builder().addIndex(index)
        .build());
    if (!result.isSucceeded()) {
      log.warn("Unable to refresh index " + index + ": " + result.getErrorMessage());
    }
  }

  /**
   * Delete all {@link VariableSearchDocument} documents from the given index.
   * 
   * @param index the index to delete from.
   */
  public void deleteAll(String index) {

    // get all saved variables
    List<VariableSearchDocument> variables = this.findAll(index);

    // delete all variables
    for (VariableSearchDocument variable : variables) {
      this.delete(variable.getId(), index);
    }
  }

  /**
   * Execute queries to elasticsearch.
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
