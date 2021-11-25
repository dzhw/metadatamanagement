package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidUserApiResponseException;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.MockError;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserApiServiceTest extends AbstractUserApiTests {

  private static final String ID = "1234";
  private static final String LOGIN = "resource_server";
  private static final String NO_ID_LOGIN = "no_id_user";

  @BeforeEach
  public void initUsersToMockServer() {
    this.mockServer.users(
        new User(
            ID,
            LOGIN,
            "resource_server@example.com",
            "de",
            false,
            "user",
            "admin"
        ),
        new User(
            "",
            NO_ID_LOGIN,
            "resource_server@example.com",
            "de",
            false,
            "user",
            "admin"
        )
    );
  }

  @Test()
  public void patchDeactivatedWelcomeDialogById_Successful() throws InvalidUserApiResponseException {
    this.addFindOneByLoginRequest(LOGIN);
    this.addPatchDeactivatedWelcomeDialogById(ID)
        .withSuccess()
            .body(u -> Objects.equals(u.getId(), ID))
        .addToServer();

    userApiService.patchDeactivatedWelcomeDialogById(LOGIN, true);
  }

  @Test
  public void patchDeactivatedWelcomeDialogById_MissingUser() {
    var login = "invalid_user";
    this.addFindOneByLoginRequest(login);

    var message = assertThrows(
        InvalidUserApiResponseException.class,
        () -> userApiService.patchDeactivatedWelcomeDialogById(login, true)
    ).getMessage();

    assertEquals(
        message,
        String.format("Could not update user %s, because user was not found", login)
    );
  }

  @Test
  public void patchDeactivatedWelcomeDialogById_MissingId() {
    this.addFindOneByLoginRequest(NO_ID_LOGIN);

    var message = assertThrows(
        InvalidUserApiResponseException.class,
        () -> userApiService.patchDeactivatedWelcomeDialogById(NO_ID_LOGIN, true)
    ).getMessage();

    assertEquals(
        message,
        String.format(
            "Could not update user %s, because user id could not be found",
            NO_ID_LOGIN
        )
    );
  }

  @Test
  public void patchDeactivatedWelcomeDialogById_InternalServer() {
    this.addFindOneByLoginRequest(LOGIN);
    this.addPatchDeactivatedWelcomeDialogById(ID)
        .withServerError()
        .addToServer();

    assertThrows(
        InvalidUserApiResponseException.class,
        () -> userApiService.patchDeactivatedWelcomeDialogById(LOGIN, true)
    );
  }

  @Test
  public void patchDeactivatedWelcomeDialogById_NoResponse() {
    this.addFindOneByLoginRequest(LOGIN);
    this.addPatchDeactivatedWelcomeDialogById(ID)
      .withSuccess()
      .addToServer();

    var message = assertThrows(
        InvalidUserApiResponseException.class,
        () -> userApiService.patchDeactivatedWelcomeDialogById(LOGIN, true)
    ).getMessage();

    assertEquals(
        message,
        String.format(
            "No response received while updating deactivated welcome dialog for user '%s'. "
                + "Assuming update failed",
            LOGIN
        )
    );
  }

  @Test
  public void patchDeactivatedWelcomeDialogById_WithErrors() {
    final var error1 = new MockError("invalid_request");
    final var error2 = new MockError("bad_request");

    this.addFindOneByLoginRequest(LOGIN);
    this.addPatchDeactivatedWelcomeDialogById(ID)
      .withSuccess()
        .body(
            error1,
            error2
        )
      .addToServer();

    var message = assertThrows(
        InvalidUserApiResponseException.class,
        () -> userApiService.patchDeactivatedWelcomeDialogById(LOGIN, true)
    ).getMessage();

    assertEquals(
        message,
        String.format(
            "Errors received while updating deactivated welcome dialog for user '%s'. Errors: %s",
            LOGIN,
            String.format("%s,%s", error1.getDetail(), error2.getDetail())
        )
    );
  }

  @Test
  public void findAllByAuthoritiesContaining_WithResults() throws InvalidUserApiResponseException {
    final var ROLE = AuthoritiesConstants.ADMIN;

    this.addFindAllByAuthoritiesContainingRequest(ROLE);

    var results = userApiService.findAllByAuthoritiesContaining(ROLE);

    assertThat(results.size(), greaterThan(0));
  }

  @Test
  public void findAllByAuthoritiesContaining_NoResults() throws InvalidUserApiResponseException {
    final var ROLE = "INVALID_ROLE";

    this.addFindAllByAuthoritiesContainingRequest(ROLE);

    var results = userApiService.findAllByAuthoritiesContaining(ROLE);

    assertThat(results.size(), is(0));
  }

  @Test
  public void findAllByLoginLikeOrEmailLike_WithResults_ByEmail() throws InvalidUserApiResponseException {
    final var LOGIN = "NON_LOGIN";
    final var EMAIL_PREFIX = "resource";
    final var EMAIL_MIDDLE = "server@example";
    final var EMAIL_SUFFIX = "example.com";
    final var EMAIL = "resource_server@example.com";

    // Add mock server expectations
    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN, EMAIL_PREFIX);
    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN, EMAIL_MIDDLE);
    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN, EMAIL_SUFFIX);
    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN, EMAIL);

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
  public void findAllByLoginLikeOrEmailLike_WithResults_ByLogin() throws InvalidUserApiResponseException {
    final var INVALID_EMAIL = "NON_EMAIL";
    final var LOGIN_PREFIX = "resource";
    final var LOGIN_MIDDLE = "ce_se";
    final var LOGIN_SUFFIX = "_server";
    final var LOGIN = "resource_server";

    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN_PREFIX, INVALID_EMAIL);
    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN_MIDDLE, INVALID_EMAIL);
    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN_SUFFIX, INVALID_EMAIL);
    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN, INVALID_EMAIL);

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
  public void findAllByLoginLikeOrEmailLike_NoResults() throws InvalidUserApiResponseException {
    var LOGIN = "NOT_LOGIN";
    var EMAIL = "NOT_EMAIL";

    this.addFindAllByLoginLikeOrEmailLikeRequest(LOGIN, EMAIL);

    var results = userApiService.findAllByLoginLikeOrEmailLike(LOGIN, EMAIL);

    assertThat(results.size(), is(0));
  }

  @Test
  public void findAllByLoginIn_WithResults() throws InvalidUserApiResponseException {
    final var LOGINS = Set.of("resource_server");

    this.addFindAllByLoginInRequest(LOGINS);

    var results = userApiService.findAllByLoginIn(LOGINS);
    assertThat(results.size(), greaterThan(0));

    assertThat(results.stream().allMatch(r -> LOGINS.contains(r.getLogin())), is(true));
  }

  @Test
  public void findAllByLoginIn_NoResults() throws InvalidUserApiResponseException {
    Set<String> EMPTY_LOGINS = Set.of();
    Set<String> INVALID_LOGINS = Set.of("invalid_login");

    this.addFindAllByLoginInRequest(EMPTY_LOGINS);
    this.addFindAllByLoginInRequest(INVALID_LOGINS);

    // No logins test
    var results = userApiService.findAllByLoginIn(EMPTY_LOGINS);
    assertThat(results.size(), is(0));

    // Non-existent login test
    results = userApiService.findAllByLoginIn(INVALID_LOGINS);
    assertThat(results.size(), is(0));
  }

  @Test
  public void findOneByLoginOrEmail_WithResult_ByLogin() throws InvalidUserApiResponseException {
    final var LOGIN = "resource_server";
    final var EMAIL = "NON_EMAIL";

    this.addFindOneByLoginOrEmailRequest(LOGIN, EMAIL);

    var result = userApiService.findOneByLoginOrEmail(LOGIN, EMAIL);
    assertThat(result.isPresent(), is(true));
    assertThat(result.get().getLogin(), is(LOGIN));
  }

  @Test
  public void findOneByLoginOrEmail_WithResult_ByEmail() throws InvalidUserApiResponseException {
    final var LOGIN = "NON_LOGIN";
    final var EMAIL = "resource_server@example.com";

    this.addFindOneByLoginOrEmailRequest(LOGIN, EMAIL);

    var result = userApiService.findOneByLoginOrEmail(LOGIN, EMAIL);
    assertThat(result.isPresent(), is(true));
    assertThat(result.get().getEmail(), is (EMAIL));
  }

  @Test
  public void findOneByLoginOrEmail_NoResult() throws InvalidUserApiResponseException {
    final var LOGIN = "NON_LOGIN";
    final var EMAIL = "NON_EMAIL";

    this.addFindOneByLoginOrEmailRequest(LOGIN, EMAIL);

    var result = userApiService.findOneByLoginOrEmail(LOGIN, EMAIL);
    assertThat(result.isPresent(), is(false));
  }

  @Test
  public void findOneByLogin_WithResult() throws InvalidUserApiResponseException {
    final var LOGIN = "resource_server";

    this.addFindOneByLoginRequest(LOGIN);

    var result = userApiService.findOneByLogin(LOGIN);
    assertThat(result.isPresent(), is(true));
    assertThat(result.get().getLogin(), is(LOGIN));
  }

  @Test
  public void findOneByLogin_NoResult() throws InvalidUserApiResponseException {
    final var LOGIN = "NON_LOGIN";

    this.addFindOneByLoginRequest(LOGIN);

    var result = userApiService.findOneByLogin(LOGIN);

    assertThat(result.isPresent(), is(false));
  }
}
