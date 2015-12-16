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
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Data access object for saving variables in elasticsearch.
 * 
 * @author René Reitmann
 */
@Component
public class VariableSearchDao {

  public static final String TYPE = "variables";

  private final Logger log = LoggerFactory.getLogger(VariableSearchDao.class);

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
  @SuppressWarnings("deprecation")
  public List<VariableSearchDocument> findAll(String index) {
    SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
    queryBuilder.query(QueryBuilders.matchAllQuery());
    Search search = new Search.Builder(queryBuilder.toString()).addIndex(index)
        .addType(TYPE)
        .build();
    SearchResult result = (SearchResult) execute(search);
    if (!result.isSucceeded()) {
      log.warn("Unable to load variable search documents from index " + index + ": "
          + result.getErrorMessage());
      return new ArrayList<>();
    }
    List<VariableSearchDocument> variables =
        result.getSourceAsObjectList(VariableSearchDocument.class);

    return variables;
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

  private void deleteByField(String fieldName, String value, String index) {
    SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
    queryBuilder.query(QueryBuilders.matchQuery(fieldName, value));
    JestResult result = execute(new DeleteByQuery.Builder(queryBuilder.toString()).addIndex(index)
        .addType(TYPE)
        .build());
    if (!result.isSucceeded()) {
      throw new ElasticsearchDocumentDeleteException(
          index, TYPE, fieldName, value, result.getErrorMessage());
    }
  }
  
  public void deleteByFdzProjectName(String fdzProjectName, String index) {
    deleteByField("fdzProjectName", fdzProjectName, index);
  }
  
  public void deleteBySurveyId(String surveyId, String index) {
    deleteByField("surveyId", surveyId, index);
  }

  /**
   * Delete all {@link VariableSearchDocument} documents from the given index.
   * 
   * @param index the index to delete from.
   */
  public void deleteAll(String index) {
    SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
    queryBuilder.query(QueryBuilders.matchAllQuery());
    JestResult result = execute(new DeleteByQuery.Builder(queryBuilder.toString()).addIndex(index)
        .addType(TYPE)
        .build());
    if (!result.isSucceeded()) {
      throw new ElasticsearchDocumentDeleteException(index, TYPE, result.getErrorMessage());
    }
  }

  private JestResult execute(Action<?> action) {
    try {
      return jestClient.execute(action);
    } catch (IOException e) {
      throw new ElasticsearchIoException(e);
    }
  }
}
