/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties.ElasticsearchAngularClient;

/**
 * @author Daniel Katzberg
 *
 */
public class MetadataManagementPropertiesTest {

  @Test
  public void testElasticSearchAngularClient() {
    // Arrange
    MetadataManagementProperties managementProperties = new MetadataManagementProperties();

    // Act
    ElasticsearchAngularClient angularClient = managementProperties.getElasticsearchAngularClient();
    angularClient.setLogLevel("info");
    angularClient.setUrl("http://localhost:1234");
    angularClient.setApiVersion("5.1");

    // Assert
    assertThat(angularClient, not(nullValue()));
    assertThat(angularClient.getLogLevel(), is("info"));
    assertThat(angularClient.getApiVersion(), is("5.1"));
    assertThat(angularClient.getUrl(), is("http://localhost:1234"));
  }
}
