package eu.dzhw.fdz.metadatamanagement.searchmanagement.rest;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

import com.codahale.metrics.annotation.Timed;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.common.rest.util.HeaderUtil;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for getting the search results.
 */
@RestController
public class SearchResource {

  private final Logger log = LoggerFactory.getLogger(SearchResource.class);

  private RestTemplate restTemplate;

  private String connectionUrl;

  private String base64Credentials;

  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  /**
   * Create the search proxy with the given elasticsearch host url.
   *
   * @param elasticSearchConnectionUrl the elasticsearch host url
   */
  @Autowired
  @SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
  public SearchResource(String elasticSearchConnectionUrl)
      throws UnsupportedEncodingException, MalformedURLException {
    this.connectionUrl = elasticSearchConnectionUrl;
    URL url = new URL(elasticSearchConnectionUrl);
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.getMessageConverters()
      .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    String credentials = url.getUserInfo();
    if (!StringUtils.isEmpty(credentials)) {
      byte[] plainCredsBytes = credentials.getBytes("UTF-8");
      byte[] base64CredsBytes = Base64.encode(plainCredsBytes);
      base64Credentials = new String(base64CredsBytes, "UTF-8");
    }
    // prevent throwing exception on error codes
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
      protected boolean hasError(HttpStatus statusCode) {
          return false;
      }
    });
  }

  /**
   * Redirect the search requests to Elasticsearch.
   *
   * @return The search results
   */
  @RequestMapping(value = {"/api/search/**"})
  @Timed
  public ResponseEntity<String> search(@RequestBody(required = false) String body,
      HttpMethod method,
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
    ResponseEntity<String> responseFromElasticSearch = restTemplate.exchange(
        connectionUrl + path, method, new HttpEntity<>(body, headers), String.class);
    ResponseEntity<String> finalResponse = new ResponseEntity<String>(
        responseFromElasticSearch.getBody(), responseFromElasticSearch.getStatusCode());
    return finalResponse;
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
      .headers(HeaderUtil.createAlert("administration.health.elasticsearch.reindex-success"))
      .build();
  }

  /**
   * POST /api/search/process-queue -> trigger processing of elasticsearch updates.
   */
  @RequestMapping(value = "/api/search/process-queue", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> processElasticsearchUpdateQueue() throws URISyntaxException {
    log.debug("REST request to process update queue.");
    elasticsearchUpdateQueueService.processQueue();
    elasticsearchAdminService.refreshAllIndices();
    return ResponseEntity.ok()
      .build();
  }
}
