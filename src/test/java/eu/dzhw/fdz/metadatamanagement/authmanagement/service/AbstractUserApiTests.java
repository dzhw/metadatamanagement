package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.MockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

  public void addFindAllByAuthoritiesContainingRequest(final String role) {
    addFindAllByAuthoritiesContainingRequest(1, role);
  }

  public void addFindAllByAuthoritiesContainingRequest(
      final int expectedRequests,
      final String role
  ) {
    var roleAsSearch = AuthoritiesConstants.toSearchValue(role);

    mockServer.multipleRequests(
      UserApiService.FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT,
      u -> u.getRoles().contains(roleAsSearch),
      expectedRequests,
      AuthoritiesConstants.toSearchValue(roleAsSearch)
    );
  }

  public void addFindAllByLoginLikeOrEmailLikeRequest(
      final String login,
      final String email
  ) {
    mockServer.request(
        UserApiService.FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT,
        u -> u.getName().contains(login) || u.getMail().contains(email),
        login,
        email
    );
  }

  public void addFindAllByLoginInRequest(final Set<String> logins) {
    addFindAllByLoginInRequest(1, logins);
  }

  public void addFindAllByLoginInRequest(
      final int expectedRequests,
      final Set<String> logins
  ) {
    StringBuilder sb = new StringBuilder(UserApiService.FIND_ALL_BY_LOGIN_IN_ENDPOINT);

    for (var i = 0; i < logins.size(); i++) {
      sb.append(String.format(
        UserApiService.FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE,
        i
      ));
    }

    this.mockServer.multipleRequests(
        sb.toString(),
        u -> logins.contains(u.getName()),
        expectedRequests,
        logins
    );
  }

  public void addFindOneByLoginOrEmailRequest(final String login, final String email) {
    this.mockServer.request(
        UserApiService.FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT,
        u -> u.getName().equals(login) || u.getMail().equals(email),
        login,
        email
    );
  }

  public void addFindOneByLoginRequest(final String login) {
    addFindOneByLoginRequest(1, login);
  }

  public void addFindOneByLoginRequest(
      final int expectedRequests,
      final String login
  ) {
    this.mockServer.multipleRequests(
        UserApiService.FIND_ONE_BY_LOGIN_ENDPOINT,
        u -> u.getName().equals(login),
        expectedRequests,
        login
    );
  }

  public void addFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
      final String login,
      final String email,
      final String role
  ) {
    var roleAsSearch = AuthoritiesConstants.toSearchValue(role);
    this.mockServer.request(
      UserApiService.FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_AND_BY_AUTHORITIES_CONTAINING_ENDPOINT,
      u -> (u.getName().contains(login) || u.getMail().contains(email)) && u.getRoles().contains(roleAsSearch),
      login,
      email,
      roleAsSearch
    );
  }

  public void addFindOneWithAuthoritiesByLoginRequest(final String login) {
    this.mockServer.request(
        UserApiService.FIND_ONE_WITH_AUTHORITIES_BY_LOGIN_ENDPOINT,
        u -> u.getName().equals(login),
        login
    );
  }
}
