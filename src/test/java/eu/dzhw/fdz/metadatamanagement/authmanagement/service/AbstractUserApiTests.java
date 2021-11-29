package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.MockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;

import java.util.Set;

public class AbstractUserApiTests extends AbstractTest {

  @Value("${metadatamanagement.authmanagement.server.endpoint}")
  private String authServerEndpoint;

  @Autowired
  protected UserApiService userApiService;

  protected MockServer mockServer;

  @BeforeEach
  public void init() {
    this.mockServer = new MockServer(authServerEndpoint, userApiService.restTemplate);
  }

  @AfterEach
  public void destroy() {
    if (mockServer != null) {
      mockServer.verify();

      mockServer = null;
    }
  }

  public MockServer.MockRequest startFindAllByAuthoritiesContainingRequest(final String role) {
    var roleAsSearch = AuthoritiesConstants.toSearchValue(role);

    return mockServer.request(
      UserApiService.FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT,
      AuthoritiesConstants.toSearchValue(roleAsSearch)
    );
  }

  public void addFindAllByAuthoritiesContainingRequest(final String role) {
    addFindAllByAuthoritiesContainingRequest(1, role);
  }

  public void addFindAllByAuthoritiesContainingRequest(
      final int expectedRequests,
      final String role
  ) {
    startFindAllByAuthoritiesContainingRequest(role)
        .expectedCount(expectedRequests)
        .withSuccess()
            .body(u -> u.getRoles().contains(AuthoritiesConstants.toSearchValue(role)))
        .addToServer();
  }

  public MockServer.MockRequest startFindAllByLoginLikeOrEmailLikeRequest(
      final String login,
      final String email
  ) {
    return mockServer.request(
        UserApiService.FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT,
        login,
        email
    );
  }

  public void addFindAllByLoginLikeOrEmailLikeRequest(
      final String login,
      final String email
  ) {
    startFindAllByLoginLikeOrEmailLikeRequest(login, email)
        .withSuccess()
            .body(u -> u.getName().contains(login) || u.getMail().contains(email))
        .addToServer();
  }

  public MockServer.MockRequest startFindAllByLoginInRequest(final Set<String> logins) {
    StringBuilder sb = new StringBuilder(UserApiService.FIND_ALL_BY_LOGIN_IN_ENDPOINT);

    for (var i = 0; i < logins.size(); i++) {
      sb.append(String.format(
        UserApiService.FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE,
        i
      ));
    }

    return this.mockServer.request(
      sb.toString(),
      logins
    );
  }

  public void addFindAllByLoginInRequest(final Set<String> logins) {
    addFindAllByLoginInRequest(1, logins);
  }

  public void addFindAllByLoginInRequest(
      final int expectedRequests,
      final Set<String> logins
  ) {
    startFindAllByLoginInRequest(logins)
        .expectedCount(expectedRequests)
        .withSuccess()
            .body(u -> logins.contains(u.getName()))
        .addToServer();
  }

  public MockServer.MockRequest startFindOneByLoginOrEmailRequest(final String login, final String email) {
    return this.mockServer.request(
        UserApiService.FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT,
        login,
        email
    );
  }

  public void addFindOneByLoginOrEmailRequest(final String login, final String email) {
    startFindOneByLoginOrEmailRequest(login, email)
        .withSuccess()
            .body(u -> u.getName().equals(login) || u.getMail().equals(email))
        .addToServer();
  }

  public MockServer.MockRequest startFindOneByLoginRequest(final String login) {
    return this.mockServer.request(
        UserApiService.FIND_ONE_BY_LOGIN_ENDPOINT,
        login
    );
  }

  public void addFindOneByLoginRequest(final String login) {
    addFindOneByLoginRequest(1, login);
  }

  public void addFindOneByLoginRequest(
      final int expectedRequests,
      final String login
  ) {
    startFindOneByLoginRequest(login)
      .expectedCount(expectedRequests)
      .withSuccess()
          .body(u -> u.getName().equals(login))
      .addToServer();
  }

  public MockServer.MockRequest startFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
      final String login,
      final String email,
      final String role
  ) {
    var roleAsSearch = AuthoritiesConstants.toSearchValue(role);

    return this.mockServer.request(
      UserApiService.FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_AND_BY_AUTHORITIES_CONTAINING_ENDPOINT,
      login,
      email,
      roleAsSearch
    );
  }

  public void addFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
      final String login,
      final String email,
      final String role
  ) {
    startFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
        login,
        email,
        role
    )
        .withSuccess()
            .body(u ->
                (u.getName().contains(login) || u.getMail().contains(email))
                    && u.getRoles().contains(AuthoritiesConstants.toSearchValue(role))
            )
        .addToServer();
  }

  public MockServer.MockRequest startFindOneWithAuthoritiesByLoginRequest(final String login) {
    return this.mockServer.request(
        UserApiService.FIND_ONE_WITH_AUTHORITIES_BY_LOGIN_ENDPOINT,
        login
    );
  }

  public void addFindOneWithAuthoritiesByLoginRequest(final String login) {
    startFindOneWithAuthoritiesByLoginRequest(login)
        .withSuccess()
            .body(u -> u.getName().equals(login))
        .addToServer();
  }

  public MockServer.MockRequest addPatchDeactivatedWelcomeDialogById(final String id) {
    return this.mockServer.request(
        UserApiService.PATCH_DEACTIVATED_WELCOME_DIALOG_BY_ID_ENDPOINT,
        id
    )
        .httpMethod(HttpMethod.PATCH);
  }
}
