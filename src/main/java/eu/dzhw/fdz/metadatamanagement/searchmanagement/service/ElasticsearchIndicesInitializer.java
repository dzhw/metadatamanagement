package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchIndicesVersion;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchIndicesVersionRepository;

/**
 * Bean which recreates all elasticsearch indices on container start
 * if the version has changed.
 * 
 * @author René Reitmann
 */
@Component
@Profile("!" + Constants.SPRING_PROFILE_UNITTEST)
public class ElasticsearchIndicesInitializer {
  
  private final Logger log = LoggerFactory.getLogger(ElasticsearchIndicesInitializer.class);
  
  @Autowired
  private ResourceLoader resourceLoader;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;
  
  @Autowired
  private ElasticsearchIndicesVersionRepository indicesVersionRepository;
  
  @Autowired
  private MetadataManagementProperties metadataManagementProperties;
  
  private JsonParser jsonParser = new JsonParser();
  
  /**
   * Recreate all elasticsearch indices on container start.
   * Does not do anything if the version has not changed or if this container
   * instance is not the first one.
   */
  @PostConstruct
  public void performIndicesUpdates() {
    if (!metadataManagementProperties.getServer().getInstanceIndex().equals(0)) {
      log.debug("This is server instance {} therefore skipping indices updates.", 
          metadataManagementProperties.getServer().getInstanceIndex());
      return;
    }
    List<ElasticsearchIndicesVersion> previousVersions = indicesVersionRepository.findAll();
    String currentVersion = loadIndicesVersion().get("indicesVersion").getAsString();
    if (previousVersions.isEmpty() || !previousVersions.get(0).getId().equals(currentVersion)) {
      log.info("Going to automatically update elasticsearch indices...");
      elasticsearchAdminService.recreateAllIndices().thenRun(() -> {        
        indicesVersionRepository.deleteAll();
        indicesVersionRepository.save(new ElasticsearchIndicesVersion(currentVersion));
        log.info("Finished automatic update of elasticsearch indices!");
      });
    }
  }
  
  private JsonObject loadIndicesVersion() {
    try (InputStream inputStream = resourceLoader
        .getResource("classpath:elasticsearch/indices_version.json").getInputStream();
       Reader reader = new InputStreamReader(inputStream,"UTF-8");) {
      JsonObject mappingsVersion = jsonParser.parse(reader).getAsJsonObject();
      return mappingsVersion;
    } catch (IOException e) {
      throw new RuntimeException("Unable to load indices version!", e);
    }
  }
}
