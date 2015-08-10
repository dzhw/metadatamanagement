package eu.dzhw.fdz.metadatamanagement.service.variablemanagement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplicationSmokeTest;


public class VariableServiceTest extends MetaDataManagementApplicationSmokeTest {

  @Autowired
  private VariableService variableService;

  @Test
  public void testSearchWithQuery() {

    Pageable pageable = new PageRequest(0, 10);

    assertThat(variableService.search("name", pageable).getSize(), is(10));

  }

  @Test
  public void testSearchWithoutQuery() {

    Pageable pageable = new PageRequest(0, 10);

    assertThat(variableService.search(null, pageable).getSize(), is(10));

  }
}
