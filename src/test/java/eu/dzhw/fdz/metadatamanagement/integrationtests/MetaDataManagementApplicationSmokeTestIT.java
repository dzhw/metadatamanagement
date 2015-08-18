package eu.dzhw.fdz.metadatamanagement.integrationtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

/**
 * Simple integration test which ensures that the jar has been started successfully and that the
 * server responds with 200 on root url.
 * 
 * @author René Reitmann
 */
public class MetaDataManagementApplicationSmokeTestIT {

  // the reserved random port of the test node
  private String serverPort = System.getProperty("test.server.port", "8080");

  // spring test rest template
  private RestTemplate template = new TestRestTemplate();

  @Test
  public void testServerIsAlive() throws Exception {
    HttpStatus status =
        template.getForEntity("http://localhost:" + serverPort, String.class).getStatusCode();
    assertThat(status, is(HttpStatus.OK));
  }
}
