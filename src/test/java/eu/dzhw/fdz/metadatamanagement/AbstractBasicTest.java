package eu.dzhw.fdz.metadatamanagement;

import javax.inject.Inject;

import org.elasticsearch.node.Node;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import eu.dzhw.fdz.metadatamanagement.notest.util.elasticsearch.EmbeddedElasticsearch;

/**
 * This class is a basic class for the most unit tests.
 * 
 * @author Daniel Katzberg
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ComponentScan(basePackages = {"eu.dzhw.fdz.metadatamanagement.notest.util.config"})
public abstract class AbstractBasicTest {

  protected Node node;

  @Inject
  private EmbeddedElasticsearch embeddedElasticsearch;

  @Before
  public void before() {
    this.node = this.embeddedElasticsearch.getNode();
  }
}
