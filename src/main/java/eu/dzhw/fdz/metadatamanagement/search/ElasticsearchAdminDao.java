package eu.dzhw.fdz.metadatamanagement.search;

import java.io.IOException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchIndexCreateException;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchIndexDeleteException;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchIoException;
import eu.dzhw.fdz.metadatamanagement.search.exception.ElasticsearchPutMappingException;
import io.searchbox.action.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Refresh;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.settings.GetSettings;

/**
 * Data Access Object for setting up the elasticsearch indices and mappings.
 * 
 * @author René Reitmann
 */
@Component
public class ElasticsearchAdminDao {

  private final Logger log = LoggerFactory.getLogger(ElasticsearchAdminDao.class);

  @Inject
  private JestClient jestClient;

  /**
   * Create an index with the given settings as json.
   * 
   * @param index the name of the index
   * @param settings the settings json
   */
  public void createIndex(String index, JsonObject settings) {
    JestResult result = execute(new CreateIndex.Builder(index).settings(settings).build());
    if (!result.isSucceeded()) {
      throw new ElasticsearchIndexCreateException(index, result.getErrorMessage());
    }
  }

  /**
   * Get the current settings for the given index.
   * 
   * @param index The name of the index.
   * @return the current settings json
   */
  public JsonObject getSettings(String index) {
    JestResult result = execute(new GetSettings.Builder().addIndex(index).build());
    if (!result.isSucceeded()) {
      log.warn("Unable to get setting for index " + index + ": " + result.getErrorMessage());
      return null;
    }
    return result.getJsonObject().getAsJsonObject(index).getAsJsonObject("settings");
  }

  /**
   * Create or update the mapping for the given type.
   * 
   * @param index the name of the index holding the type
   * @param type the type for which the mapping is generated
   * @param mapping the mapping json
   */
  public void putMapping(String index, String type, JsonObject mapping) {
    JestResult result = execute(new PutMapping.Builder(index, type, mapping).build());
    if (!result.isSucceeded()) {
      throw new ElasticsearchPutMappingException(index, type, result.getErrorMessage());
    }
  }

  /**
   * Get the mapping for the given type.
   * 
   * @param index the name of the index
   * @param type the type for which the mapping is returned
   * @return the mapping json
   */
  public JsonObject getMapping(String index, String type) {
    JestResult result = execute(new GetMapping.Builder().addIndex(index).addType(type).build());
    if (!result.isSucceeded()) {
      log.warn("Unable to get mapping for index " + index + " and type " + type + ": "
          + result.getErrorMessage());
      return null;
    }
    return result.getJsonObject().getAsJsonObject(index).getAsJsonObject("mappings");
  }

  /**
   * Check if the given index exists.
   * 
   * @param index the name of the index
   * @return true if the index exists.
   */
  public boolean exists(String index) {
    return execute(new IndicesExists.Builder(index).build()).isSucceeded();
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
   * Delete the given index and all of its documents.
   * 
   * @param index The name of the index to delete.
   */
  public void delete(String index) {
    JestResult result = execute(new DeleteIndex.Builder(index).build());
    if (!result.isSucceeded()) {
      throw new ElasticsearchIndexDeleteException(index, result.getErrorMessage());
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
