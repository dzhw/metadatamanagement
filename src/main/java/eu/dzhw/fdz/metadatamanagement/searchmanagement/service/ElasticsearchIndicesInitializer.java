package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchIndicesVersion;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchIndicesVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Bean which recreates all elasticsearch indices on container start
 * if the version has changed.
 *
 * @author René Reitmann
 */
@Component
@Profile("!" + Constants.SPRING_PROFILE_UNITTEST)
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchIndicesInitializer {

  private final ResourceLoader resourceLoader;

  private final ElasticsearchAdminService elasticsearchAdminService;

  private final ElasticsearchIndicesVersionRepository indicesVersionRepository;

  private final MetadataManagementProperties metadataManagementProperties;

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
    String currentVersion = loadIndicesVersion().get("indicesVersion").textValue();
    if (previousVersions.isEmpty() || !previousVersions.get(0).getId().equals(currentVersion)) {
      log.info("Going to automatically update elasticsearch indices...");
      elasticsearchAdminService.recreateAllIndices().thenRun(() -> {
        indicesVersionRepository.deleteAll();
        indicesVersionRepository.save(new ElasticsearchIndicesVersion(currentVersion));
        log.info("Finished automatic update of elasticsearch indices!");
      });
    }
  }

  @SuppressFBWarnings("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  private JsonNode loadIndicesVersion() {
    var resource = resourceLoader.getResource("classpath:elasticsearch/indices_version.json");
    try (var inputStream = resource.getInputStream()) {
      return new ObjectMapper().readTree(inputStream);
    } catch (IOException e) {
      throw new RuntimeException("Unable to load indices version!", e);
    }
  }
}
