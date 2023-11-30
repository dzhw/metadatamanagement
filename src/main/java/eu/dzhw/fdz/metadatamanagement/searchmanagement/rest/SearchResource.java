package eu.dzhw.fdz.metadatamanagement.searchmanagement.rest;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.AutoTimer;
import org.springframework.boot.actuate.metrics.web.client.MetricsRestTemplateCustomizer;
import org.springframework.boot.actuate.metrics.web.client.RestTemplateExchangeTagsProvider;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for getting the search results.
 */
@RestController
@Slf4j
public class SearchResource {
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
  public SearchResource(
      @Value("${spring.elasticsearch.rest.uris[0]}") String elasticSearchConnectionUrl,
      MeterRegistry meterRegistry, RestTemplateExchangeTagsProvider tagProvider)
      throws UnsupportedEncodingException, MalformedURLException {
    this.connectionUrl = elasticSearchConnectionUrl;
    URL url = new URL(elasticSearchConnectionUrl);
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.getMessageConverters().add(0,
        new StringHttpMessageConverter(Charset.forName("UTF-8")));
    String credentials = url.getUserInfo();
    if (!StringUtils.isEmpty(credentials)) {
      byte[] plainCredsBytes = credentials.getBytes("UTF-8");
      byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
      base64Credentials = new String(base64CredsBytes, "UTF-8");
    }
    // prevent throwing exception on error codes
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
      @Override
      protected boolean hasError(HttpStatus statusCode) {
        return false;
      }
    });
    MetricsRestTemplateCustomizer customizer = new MetricsRestTemplateCustomizer(meterRegistry,
        tagProvider, "elasticsearch.client.requests", AutoTimer.ENABLED);
    customizer.customize(restTemplate);
  }

  /**
   * Redirect the search requests to Elasticsearch.
   *
   * @return The search results
   */
  @RequestMapping(value = {"/api/search/**"})
  public ResponseEntity<String> search(@RequestBody(required = false) String body,
      HttpMethod method, HttpServletRequest request)
      throws RestClientException, URISyntaxException, UnsupportedEncodingException {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    if (base64Credentials != null) {
      headers.add("authorization", "Basic " + base64Credentials);
    }

    String completePath =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String queryString = request.getQueryString();
    String path = completePath.replaceFirst("/api/search", "");
    String url = URLDecoder.decode(connectionUrl + path, "UTF-8");
    if (!StringUtils.isEmpty(queryString)) {
      url = url + "?" + URLDecoder.decode(queryString, "UTF-8");
    }
    ResponseEntity<String> responseFromElasticSearch =
        restTemplate.exchange(url, method, new HttpEntity<>(body, headers), String.class);

    return ResponseEntity.status(responseFromElasticSearch.getStatusCode())
        .cacheControl(CacheControl.noStore()).body(responseFromElasticSearch.getBody());
  }

  /**
   * Recreate all elasticsearch indices.
   */
  @RequestMapping(value = "/api/search/recreate", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured(AuthoritiesConstants.ADMIN)
  public ResponseEntity<?> recreateAllElasticsearchIndices() throws URISyntaxException {
    log.debug("REST request to recreate all elasticsearch indices.");
    elasticsearchAdminService.recreateAllIndices();
    return ResponseEntity.ok().build();
  }

  /**
   * Trigger processing of elasticsearch updates.
   */
  @RequestMapping(value = "/api/search/process-queue", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> processElasticsearchUpdateQueue(
      @RequestParam(required = false) ElasticsearchType type) throws URISyntaxException {
    log.debug("REST request to process update queue for type: " + type);
    if (type != null) {
      elasticsearchUpdateQueueService.processQueueItems(type);
    } else {
      elasticsearchUpdateQueueService.processAllQueueItems();
    }
    return ResponseEntity.ok().build();
  }
}
