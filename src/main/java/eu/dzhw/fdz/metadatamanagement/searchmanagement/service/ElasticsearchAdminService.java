package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.inject.Inject;

import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchAdminDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Service which sets up all indices.
 * 
 * @author René Reitmann
 */
@Service
public class ElasticsearchAdminService {
  @Inject
  private ElasticsearchAdminDao elasticsearchAdminDao;

  @Inject
  private VariableRepository variableRepository;
  
  @Inject
  private ElasticsearchUpdateQueueService updateQueueService;

  @Inject
  private ResourceLoader resourceLoader;

  private JsonParser jsonParser = new JsonParser();

  /**
   * Recreate the indices and all their mappings.
   */
  public void recreateAllIndices() {
    for (ElasticsearchIndices index : ElasticsearchIndices.values()) {      
      recreateIndex(index.getIndexName());
    }
    this.enqueueAllVariables();
    updateQueueService.processQueue();
  }
  
  /**
   * Load all variables from mongo and enqueue them for updating.
   */
  private void enqueueAllVariables() {
    Pageable pageable = new PageRequest(0, 100);
    Slice<Variable> variables = variableRepository.findBy(pageable);

    while (variables.hasContent()) {
      variables.forEach(variable -> {
        updateQueueService.enqueue(
            variable.getId(), 
            ElasticsearchType.variables, 
            ElasticsearchUpdateQueueAction.UPSERT);
      });
      pageable = pageable.next();
      variables = variableRepository.findBy(pageable);
    }
  }

  private void recreateIndex(String index) {
    if (elasticsearchAdminDao.exists(index)) {
      elasticsearchAdminDao.delete(index);
      // deleting is asynchronous and thus searchly complains if we create the new index to early
      elasticsearchAdminDao.refresh(index);
    }
    elasticsearchAdminDao.createIndex(index, loadSettings(index));
    // TODO add mappings for all types
    //for (ElasticsearchType type : ElasticsearchType.values()) {
    elasticsearchAdminDao.putMapping(index, ElasticsearchType.variables.name(), 
        loadMapping(index, ElasticsearchType.variables.name()));
    //}
  }
  
  private JsonObject loadSettings(String index) {
    try {
      Reader reader = new InputStreamReader(
          resourceLoader.getResource("classpath:elasticsearch/" + index + "/settings.json")
            .getInputStream(),
          "UTF-8");
      JsonObject settings = jsonParser.parse(reader)
          .getAsJsonObject();
      return settings;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load settings for index " + index, e);
    }
  }

  private JsonObject loadMapping(String index, String type) {
    try {
      Reader reader = new InputStreamReader(resourceLoader
          .getResource("classpath:elasticsearch/" + index + "/" + type + "/mapping.json")
          .getInputStream(), "UTF-8");
      JsonObject mapping = jsonParser.parse(reader)
          .getAsJsonObject();
      return mapping;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load mapping for index " + index + " and type " + type,
          e);
    }
  }
  
  /**
   * Refresh all elasticsearch indices.
   */
  public void refreshAllIndices() {
    for (ElasticsearchIndices index : ElasticsearchIndices.values()) {      
      elasticsearchAdminDao.refresh(index.getIndexName());
    }
  }
  
  public Double countAllDocuments() {
    return elasticsearchAdminDao.countAllDocuments();
  }
}
