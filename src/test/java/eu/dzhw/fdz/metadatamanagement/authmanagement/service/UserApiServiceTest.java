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
  public void findAllByAuthoritiesContaining_WithResults() throws InvalidResponseException {
    var results = userApiService.findAllByAuthoritiesContaining("editor");

    assertThat(results.size(), greaterThan(0));
  }

  @Test
  public void findAllByAuthoritiesContaining_NoResults() throws InvalidResponseException {
    var results = userApiService.findAllByAuthoritiesContaining("NON_ROLE");

    assertThat(results.size(), is(0));
  }

  @Test
  public void findAllByLoginLikeOrEmailLike_WithResults_ByEmail() throws InvalidResponseException {
    // stats with test
    var results = userApiService.findAllByLoginLikeOrEmailLike("NON_LOGIN", "resource");
    assertThat(results.size(), greaterThan(0));

    // contains test
    results = userApiService.findAllByLoginLikeOrEmailLike("NON_LOGIN","server@example");
    assertThat(results.size(), greaterThan(0));

    // ends with test
    results = userApiService.findAllByLoginLikeOrEmailLike("NON_LOGIN","example.com");
    assertThat(results.size(), greaterThan(0));

    // equals test
    results = userApiService.findAllByLoginLikeOrEmailLike("NON_LOGIN","resource_server@example.com");
    assertThat(results.size(), greaterThan(0));
  }

  @Test
  public void findAllByLoginLikeOrEmailLike_WithResults_ByLogin() throws InvalidResponseException {
    // starts with test
    var results = userApiService.findAllByLoginLikeOrEmailLike("resource", "NON_EMAIL");
    assertThat(results.size(), greaterThan(0));

    // contains test
    results = userApiService.findAllByLoginLikeOrEmailLike("ce_se","NON_EMAIL");
    assertThat(results.size(), greaterThan(0));

    // ends with test
    results = userApiService.findAllByLoginLikeOrEmailLike("_server","NON_EMAIL");
    assertThat(results.size(), greaterThan(0));

    // equals test
    results = userApiService.findAllByLoginLikeOrEmailLike("resource_server","NON_EMAIL");
    assertThat(results.size(), greaterThan(0));
  }

  @Test
  public void findAllByLoginLikeOrEmailLike_NoResults() throws InvalidResponseException {
    var results = userApiService.findAllByLoginLikeOrEmailLike("NOT_LOGIN", "NOT_EMAIL");

    assertThat(results.size(), is(0));
  }
}
