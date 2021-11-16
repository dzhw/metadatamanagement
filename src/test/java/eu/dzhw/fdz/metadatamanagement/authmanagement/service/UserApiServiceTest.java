package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidResponseException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserApiServiceTest extends AbstractUserApiTests {

  @Test
  public void findAllByAuthoritiesContaining_WithResults() throws InvalidResponseException {
    final var ROLE = "ROLE_ADMIN";

    this.populatedFindAllByAuthoritiesContainingRequest(ROLE);

    var results = userApiService.findAllByAuthoritiesContaining(ROLE);

    assertThat(results.size(), greaterThan(0));
  }

  @Test
  public void findAllByAuthoritiesContaining_NoResults() throws InvalidResponseException {
    final var ROLE = "INVALID_ROLE";

    this.emptyFindAllByAuthoritiesContainingRequest(ROLE);

    var results = userApiService.findAllByAuthoritiesContaining(ROLE);

    assertThat(results.size(), is(0));
  }

  @Test
  public void findAllByLoginLikeOrEmailLike_WithResults_ByEmail() throws InvalidResponseException {
    final var LOGIN = "NON_LOGIN";
    final var EMAIL_PREFIX = "resource";
    final var EMAIL_MIDDLE = "server@example";
    final var EMAIL_SUFFIX = "example.com";
    final var EMAIL = "resource_server@example.com";

    // Add mock server expectations
    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN, EMAIL_PREFIX);
    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN, EMAIL_MIDDLE);
    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN, EMAIL_SUFFIX);
    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN, EMAIL);

    // stats with test
    var results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN, EMAIL_PREFIX);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getEmail(), is(EMAIL));
    }

    // contains test
    results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN, EMAIL_MIDDLE);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getEmail(), is(EMAIL));
    }

    // ends with test
    results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN, EMAIL_SUFFIX);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getEmail(), is(EMAIL));
    }

    // equals test
    results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN, EMAIL);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getEmail(), is(EMAIL));
    }
  }

  @Test
  public void findAllByLoginLikeOrEmailLike_WithResults_ByLogin() throws InvalidResponseException {
    final var INVALID_EMAIL = "NON_EMAIL";
    final var LOGIN_PREFIX = "resource";
    final var LOGIN_MIDDLE = "ce_se";
    final var LOGIN_SUFFIX = "_server";
    final var LOGIN = "resource_server";

    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN_PREFIX, INVALID_EMAIL);
    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN_MIDDLE, INVALID_EMAIL);
    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN_SUFFIX, INVALID_EMAIL);
    this.populatedFindAllByLoginLikeOrEmailLike(LOGIN, INVALID_EMAIL);

    // starts with test
    var results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN_PREFIX, INVALID_EMAIL);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getLogin(), is(LOGIN));
    }

    // contains test
    results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN_MIDDLE, INVALID_EMAIL);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getLogin(), is(LOGIN));
    }

    // ends with test
    results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN_SUFFIX, INVALID_EMAIL);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getLogin(), is(LOGIN));
    }

    // equals test
    results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN, INVALID_EMAIL);
    assertThat(results.size(), greaterThan(0));
    for (var result : results) {
      assertThat(result.getLogin(), is(LOGIN));
    }
  }

  @Test
  public void findAllByLoginLikeOrEmailLike_NoResults() throws InvalidResponseException {
    var LOGIN = "NOT_LOGIN";
    var EMAIL = "NOT_EMAIL";

    this.emptyFindAllByLoginLikeOrEmailLike(LOGIN, EMAIL);

    var results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN, EMAIL);

    assertThat(results.size(), is(0));
  }

  @Test
  public void findAllByLoginIn_WithResults() throws InvalidResponseException {
    final var LOGINS = Set.of("resource_server");

    this.populatedFindAllByLoginIn(LOGINS);

    var results = userApiService.findAllByLoginIn(LOGINS);
    assertThat(results.size(), greaterThan(0));

    boolean foundAll = true;
    for (var result : results) {
      if (!LOGINS.contains(result.getLogin())) {
        foundAll = false;
        break;
      }
    }
    assertThat(foundAll, is(true));
  }

  @Test
  public void findAllByLoginIn_NoResults() throws InvalidResponseException {
    Set<String> EMPTY_LOGINS = Set.of();
    Set<String> INVALID_LOGINS = Set.of("invalid_login");

    this.emptyFindAllByLoginIn(EMPTY_LOGINS);
    this.emptyFindAllByLoginIn(INVALID_LOGINS);

    // No logins test
    var results = userApiService.findAllByLoginIn(EMPTY_LOGINS);
    assertThat(results.size(), is(0));

    // Non-existent login test
    results = userApiService.findAllByLoginIn(INVALID_LOGINS);
    assertThat(results.size(), is(0));
  }

  @Test
  public void findOneByLoginOrEmail_WithResult_ByLogin() throws InvalidResponseException {
    final var LOGIN = "resource_server";
    final var EMAIL = "NON_EMAIL";

    this.populatedFindOneByLoginOrEmail(LOGIN, EMAIL);

    var result = userApiService.findOneByLoginOrEmail(LOGIN, EMAIL);
    assertThat(result.isPresent(), is(true));
    assertThat(result.get().getLogin(), is(LOGIN));
  }

  @Test
  public void findOneByLoginOrEmail_WithResult_ByEmail() throws InvalidResponseException {
    final var LOGIN = "NON_LOGIN";
    final var EMAIL = "resource_server@example.com";

    this.populatedFindOneByLoginOrEmail(LOGIN, EMAIL);

    var result = userApiService.findOneByLoginOrEmail(LOGIN, EMAIL);
    assertThat(result.isPresent(), is(true));
    assertThat(result.get().getEmail(), is (EMAIL));
  }

  @Test
  public void findOneByLoginOrEmail_NoResult() throws InvalidResponseException {
    final var LOGIN = "NON_LOGIN";
    final var EMAIL = "NON_EMAIL";

    this.emptyFindOneByLoginOrEmail(LOGIN, EMAIL);

    var result = userApiService.findOneByLoginOrEmail(LOGIN, EMAIL);
    assertThat(result.isPresent(), is(false));
  }

  @Test
  public void findOneByLogin_WithResult() throws InvalidResponseException {
    final var LOGIN = "resource_server";

    this.populatedFindOneByLogin(LOGIN);

    var result = userApiService.findOneByLogin(LOGIN);
    assertThat(result.isPresent(), is(true));
    assertThat(result.get().getLogin(), is(LOGIN));
  }

  @Test
  public void findOneByLogin_NoResult() throws InvalidResponseException {
    final var LOGIN = "NON_LOGIN";

    this.emptyFindOneByLogin(LOGIN);

    var result = userApiService.findOneByLogin(LOGIN);

    assertThat(result.isPresent(), is(false));
  }
}
