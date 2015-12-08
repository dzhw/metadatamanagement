package eu.dzhw.fdz.metadatamanagement;

import org.elasticsearch.node.Node;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import eu.dzhw.fdz.metadatamanagement.util.elasticsearch.EmbeddedElasticsearch;

/**
 * This class is a basic class for the most unit tests.
 * 
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public abstract class BasicTest {

  protected Node node;

  @Before
  public void before() {
    if(node == null) {
      this.node = EmbeddedElasticsearch.getEmbeddedElasticsearch().getNode();
    }  
  }
}
