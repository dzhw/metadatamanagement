package eu.dzhw.fdz.metadatamanagement.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.inject.Inject;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.dzhw.fdz.metadatamanagement.search.ElasticsearchAdminDao;
import eu.dzhw.fdz.metadatamanagement.search.VariableSearchDao;

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
  private VariableService variableService;

  @Inject
  private ResourceLoader resourceLoader;

  private JsonParser jsonParser = new JsonParser();

  static final String[] INDICES = {"metadata_de", "metadata_en"};

  private static final String[] TYPES = {VariableSearchDao.TYPE};

  /**
   * Recreate the indices and all their mappings.
   */
  public void recreateAllIndices() {
    for (String index : INDICES) {
      recreateIndex(index);
    }
    variableService.reindexAllVariables();
  }

  private void recreateIndex(String index) {
    if (elasticsearchAdminDao.exists(index)) {
      elasticsearchAdminDao.delete(index);
    }
    elasticsearchAdminDao.createIndex(index, loadSettings(index));
    for (String type : TYPES) {
      elasticsearchAdminDao.putMapping(index, type, loadMapping(index, type));
    }
  }

  private JsonObject loadSettings(String index) {
    try {
      Reader reader = new InputStreamReader(resourceLoader
          .getResource("classpath:elasticsearch/" + index + "/settings.json").getInputStream(),
          "UTF-8");
      JsonObject settings = jsonParser.parse(reader).getAsJsonObject();
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
      JsonObject mapping = jsonParser.parse(reader).getAsJsonObject();
      return mapping;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load mapping for index " + index + " and type " + type,
          e);
    }
  }
}
