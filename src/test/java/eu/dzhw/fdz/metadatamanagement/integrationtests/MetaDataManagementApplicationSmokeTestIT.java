package eu.dzhw.fdz.metadatamanagement.integrationtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

public class MetaDataManagementApplicationSmokeTestIT {

  private String serverPort = System.getProperty("test.server.port");

  private RestTemplate template = new TestRestTemplate();

  @Test
  public void testServerIsAlive() throws Exception {
    HttpStatus status =
        template.getForEntity("http://localhost:" + serverPort, String.class).getStatusCode();
    assertThat(status, is(HttpStatus.OK));
  }
}
