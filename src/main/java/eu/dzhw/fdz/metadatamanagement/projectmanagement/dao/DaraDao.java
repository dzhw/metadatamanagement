package eu.dzhw.fdz.metadatamanagement.projectmanagement.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;

/**
 * Access component for getting health information or registration or updates for dara and the doi.
 * 
 * @author Daniel Katzberg
 */
@Component
public class DaraDao {
  
  private static final String IS_ALiVE_ENDPOINT = "isAlive"; 
      
  @Autowired
  private MetadataManagementProperties metadataManagementProperties;
  
  /**
   * Check the dara health endpoint. 
   * @return Returns the status of the dara server.
   */
  public boolean isDaraHealth() {
    
    final String uri = metadataManagementProperties.getDara().getEndpoint() + IS_ALiVE_ENDPOINT;
    
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    
    return result.getStatusCode().equals(HttpStatus.OK);
  }
}
