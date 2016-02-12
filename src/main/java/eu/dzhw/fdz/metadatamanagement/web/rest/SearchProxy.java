package eu.dzhw.fdz.metadatamanagement.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;

/**
 * REST controller for getting the search results.
 */
@RestController
public class SearchProxy {

  private RestTemplate restTemplate = new RestTemplate();

  private String connectionUrl;

  @Autowired
  public SearchProxy(String elasticSearchConnectionUrl) {
    this.connectionUrl = elasticSearchConnectionUrl;
  }

  /**
   * Redirect the search requests to Elasticsearch.
   * 
   * @return The search results
   */
  @RequestMapping(value = "/api/search/**")
  public ResponseEntity<String> home(@RequestBody String body, HttpMethod method,
      HttpServletRequest request) {
    String completePath =
        (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String path = completePath.replaceFirst("/api/search", "");

    ResponseEntity<String> response = restTemplate.exchange(connectionUrl + path, method,
        new HttpEntity<String>(body), String.class);
    return response;
  }
}
