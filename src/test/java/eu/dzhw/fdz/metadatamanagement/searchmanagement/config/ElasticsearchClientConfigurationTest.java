/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties.ElasticsearchClient;
import io.searchbox.client.JestClient;

/**
 * @author Daniel Katzberg
 *
 */
public class ElasticsearchClientConfigurationTest {

  @Test
  public void testJestClient() throws Exception {

    // Arrange
    ElasticsearchClientConfiguration clientConfiguration = new ElasticsearchClientConfiguration();
    MetadataManagementProperties metadataManagementProperties =
        Mockito.mock(MetadataManagementProperties.class);
    ElasticsearchClient elasticsearchClient = Mockito.mock(ElasticsearchClient.class);
    when(metadataManagementProperties.getElasticsearchClient()).thenReturn(elasticsearchClient);
    when(elasticsearchClient.getUrl()).thenReturn("localhost");
    ReflectionTestUtils.setField(clientConfiguration, "metadataManagementProperties",
        metadataManagementProperties);

    Environment environment = Mockito.mock(Environment.class);
    when(environment.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)).thenReturn(false);
    // Act
    JestClient jestClient = clientConfiguration.jestClient(clientConfiguration.elasticSearchConnectionUrl(environment));

    // Assert
    assertThat(jestClient, not(nullValue()));
  }

}
