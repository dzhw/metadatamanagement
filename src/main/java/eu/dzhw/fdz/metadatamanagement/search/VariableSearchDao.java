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
  public List<VariableSearchDocument> findAll(String index) {
    SearchSourceBuilder queryBuilder = new SearchSourceBuilder();
    queryBuilder.query(QueryBuilders.matchAllQuery());
    
    //TODO REBUILD AS FILTER
    Search search = new Search.Builder(queryBuilder.toString()).addIndex(index)
        .addType(TYPE)
        .build();
    JestResult result = execute(search);
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
    
    //TODO Katzberg REBUILD FILTER ... ENTKOPPELN SUCHE / FILTER ZU EIGENER METHODE
    Search search = new Search.Builder(queryBuilder.toString()).addIndex(index)
        .addType(TYPE)
        .build();
    JestResult result = execute(search);
    List<VariableSearchDocument> variableSearchDocumentList = 
        result.getSourceAsObjectList(VariableSearchDocument.class);
    
    //TODO Katzberg ENDE ENTKOPPELN
    
    for (VariableSearchDocument variableSearchDocument : variableSearchDocumentList) {
      this.delete(variableSearchDocument.getId(), index);
    }
  }
  
  public void deleteByFdzProjectName(String fdzProjectName, String index) {
    deleteByField("fdzProjectName", fdzProjectName, index);
  }
  
  public void deleteBySurveyId(String surveyId, String index) {
    deleteByField("surveyId", surveyId, index);
  }
  
  /**
   * Refresh the given index synchronously.
   * @param index the index to refresh.
   */
  public void refresh(String index) {
    JestResult result = execute(new Refresh.Builder().addIndex(index).build());
    if (!result.isSucceeded()) {
      log.warn("Unable to refresh index " + index + ": " + result.getErrorMessage());
    }
  }

  /**
   * Delete all {@link VariableSearchDocument} documents from the given index.
   * 
   * @param index the index to delete from.
   */
  //TODO Katzberg: first workaround!?
  public void deleteAll(String index) {
    //Refresh index
    this.refresh(index);
    
    //get all saved variables
    List<VariableSearchDocument> variables = this.findAll(index);
    
    //delete all variables
    for (VariableSearchDocument variable : variables) {
      this.delete(variable.getId(), index);
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
