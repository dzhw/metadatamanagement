package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidResponseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;

public class UserApiServiceTest extends AbstractTest {

  @Autowired
  private UserApiService userApiService;

  @Test
  public void findAllByAuthoritiesContainingWithResults() throws InvalidResponseException {
    var results = userApiService.findAllByAuthoritiesContaining("editor");

    assertThat(results.size(), greaterThan(0));
  }

  @Test
  public void findAllByAuthoritiesContainingNoResults() throws InvalidResponseException {
    var results = userApiService.findAllByAuthoritiesContaining("NON_ROLE");

    assertThat(results.size(), is(0));
  }
}
