package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import java.net.URISyntaxException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.common.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.web.rest.util.HeaderUtil;

/**
 * REST controller for doing admin tasks.
 */
@RestController
@RequestMapping("/api/admin")
@Secured(AuthoritiesConstants.ADMIN)
public class AdminResource {

  private final Logger log = LoggerFactory.getLogger(AdminResource.class);

  @Inject
  private ElasticsearchAdminService elasticsearchAdminService;

  /**
   * POST /api/admin/elasticsearch/recreate -> recreate all elasticsearch indices.
   */
  @RequestMapping(value = "/elasticsearch/recreate", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> recreateAllElasticsearchIndices() throws URISyntaxException {
    log.debug("REST request to recreate all elasticsearch indices.");
    elasticsearchAdminService.recreateAllIndices();
    return ResponseEntity.ok()
      .headers(HeaderUtil.createAlert("health.elasticsearch.reindex.success"))
      .build();
  }
}
