package eu.dzhw.fdz.metadatamanagement.searchmanagement.rest;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.common.rest.util.HeaderUtil;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for getting the search results.
 */
@RestController
public class SearchResource {

  private final Logger log = LoggerFactory.getLogger(SearchResource.class);
  
  private RestTemplate restTemplate = new RestTemplate(
      new HttpComponentsClientHttpRequestFactory());

  private String connectionUrl;

  private String base64Credentials;
  
  @Inject
  private ElasticsearchAdminService elasticsearchAdminService;

  /**
   * Create the search proxy with the given elasticsearch host url.
   * 
   * @param elasticSearchConnectionUrl the elasticsearch host url
   */
  @Autowired
  public SearchResource(String elasticSearchConnectionUrl)
      throws UnsupportedEncodingException, MalformedURLException {
    this.connectionUrl = elasticSearchConnectionUrl;
    URL url = new URL(elasticSearchConnectionUrl);
    String credentials = url.getUserInfo();
    if (!StringUtils.isEmpty(credentials)) {
      byte[] plainCredsBytes = credentials.getBytes("UTF-8");
      byte[] base64CredsBytes = Base64.encode(plainCredsBytes);
      base64Credentials = new String(base64CredsBytes, "UTF-8");
    }
  }

  /**
   * Redirect the search requests to Elasticsearch.
   * 
   * @return The search results
   */
  @RequestMapping(value = "/api/search/**/_search")
  @Timed
  public ResponseEntity<String> search(@RequestBody String body, HttpMethod method,
      @RequestHeader MultiValueMap<String, String> headers, HttpServletRequest request)
          throws RestClientException, URISyntaxException {
    headers.remove("authorization");
    headers.remove("cookie");
    headers.remove("x-forwarded-proto");
    headers.remove("x-forwarded-port");
    headers.remove("x-request-start");
    headers.remove("x-vcap-request-id");
    headers.remove("x-cf-applicationid");
    headers.remove("x-cf-instanceid");
    if (base64Credentials != null) {
      headers.add("authorization", "Basic " + base64Credentials);
    }
    
    String completePath =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String path = completePath.replaceFirst("/api/search", "");
    ResponseEntity<String> response = restTemplate.exchange(connectionUrl + path, method,
        new HttpEntity<String>(body, headers), String.class);
    return response;
  }
  
  /**
   * POST /api/search/recreate -> recreate all elasticsearch indices.
   */
  @RequestMapping(value = "/api/search/recreate", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<?> recreateAllElasticsearchIndices() throws URISyntaxException {
    log.debug("REST request to recreate all elasticsearch indices.");
    elasticsearchAdminService.recreateAllIndices();
    return ResponseEntity.ok()
      .headers(HeaderUtil.createAlert("health.elasticsearch.reindex.success"))
      .build();
  }
}
