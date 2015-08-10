package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = MetaDataManagementApplication.class)
public class VariableServiceTest {

  @Autowired
  private VariableService variableService;

  @Test
  public void testSearch() {

    Pageable pageable = new PageRequest(0, 10);

    assertThat(variableService.search("name", pageable).getSize(), is(10));

  }
}
