package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;

import java.util.Set;

public class AbstractUserApiTests extends AbstractTest {

  private static final String userResponse = """
      {
        "data": [{
          "name": "resource_server",
          "mail": "resource_server@example.com",
          "langcode": "de"
        }]
      }""";

  private static final String emptyResponse = """
      {
        "data": []
      }""";

  private UriTemplateHandler uriTemplateHandler;

  @Value("${metadatamanagement.authmanagement.server.endpoint}")
  private String authServerEndpoint;

  private MockRestServiceServer mockServer;

  @Autowired
  protected UserApiService userApiService;

  @BeforeEach
  public void init() {
    DefaultUriBuilderFactory uriFactory = new DefaultUriBuilderFactory(authServerEndpoint);
    uriFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);

    this.uriTemplateHandler = uriFactory;
  }

  @AfterEach
  public void destroy() {
    if (mockServer != null) {
      mockServer.verify();

      mockServer = null;
    }
  }

  public void populatedFindAllByAuthoritiesContainingRequest(final String role) {
    generateRequest(
        1,
        UserApiService.FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT,
        userResponse,
        AuthoritiesConstants.toSearchValue(role)
    );
  }

  public void populatedFindAllByAuthoritiesContainingRequest(final int expectedRequestsCount, final String role) {
    generateRequest(
      expectedRequestsCount,
      UserApiService.FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT,
      userResponse,
      AuthoritiesConstants.toSearchValue(role)
    );
  }

  public void emptyFindAllByAuthoritiesContainingRequest(final String role) {
    generateRequest(
        1,
        UserApiService.FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT,
        emptyResponse,
      AuthoritiesConstants.toSearchValue(role)
    );
  }

  public void populatedFindAllByLoginLikeOrEmailLike(final String login, final String email) {
    generateRequest(
        1,
        UserApiService.FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT,
        userResponse,
        login,
        email
    );
  }

  public void emptyFindAllByLoginLikeOrEmailLike(final String login, final String email) {
    generateRequest(
        1,
        UserApiService.FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT,
        emptyResponse,
        login,
        email
    );
  }

  public void populatedFindAllByLoginIn(final Set<String> logins) {
    StringBuilder sb = new StringBuilder(UserApiService.FIND_ALL_BY_LOGIN_IN_ENDPOINT);

    for (var i = 0; i < logins.size(); i++) {
      sb.append(String.format(
          UserApiService.FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE,
          i
      ));
    }

    generateRequest(
        1,
        sb.toString(),
        userResponse,
        logins
    );
  }

  public void populatedFindAllByLoginIn(final int expectedRequestsCount, final Set<String> logins) {
    StringBuilder sb = new StringBuilder(UserApiService.FIND_ALL_BY_LOGIN_IN_ENDPOINT);

    for (var i = 0; i < logins.size(); i++) {
      sb.append(String.format(
        UserApiService.FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE,
        i
      ));
    }

    generateRequest(
      expectedRequestsCount,
      sb.toString(),
      userResponse,
      logins
    );
  }

  public void emptyFindAllByLoginIn(Set<String> logins) {
    StringBuilder sb = new StringBuilder(UserApiService.FIND_ALL_BY_LOGIN_IN_ENDPOINT);

    for (var i = 0; i < logins.size(); i++) {
      sb.append(String.format(
        UserApiService.FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE,
        i
      ));
    }

    generateRequest(
        1,
        sb.toString(),
        emptyResponse,
        logins
    );
  }

  public void populatedFindOneByLoginOrEmail(final String login, final String email) {
    generateRequest(
        1,
        UserApiService.FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT,
        userResponse,
        login,
        email
    );
  }

  public void emptyFindOneByLoginOrEmail(final String login, final String email) {
    generateRequest(
        1,
        UserApiService.FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT,
        emptyResponse,
        login,
        email
    );
  }

  public void populatedFindOneByLogin(final String login) {
    generateRequest(
        1,
        UserApiService.FIND_ONE_BY_LOGIN_ENDPOINT,
        userResponse,
        login
    );
  }

  public void populatedFindOneByLogin(final int expectedRequestsCount, final String login) {
    generateRequest(
      expectedRequestsCount,
      UserApiService.FIND_ONE_BY_LOGIN_ENDPOINT,
      userResponse,
      login
    );
  }

  public void emptyFindOneByLogin(final String login) {
    generateRequest(
        1,
        UserApiService.FIND_ONE_BY_LOGIN_ENDPOINT,
        emptyResponse,
        login
    );
  }

  private void generateRequest(
      final int expectedRequestsCount,
      final String url,
      final String body,
      final Object... params
  ) {
    if (mockServer == null) {
      mockServer = MockRestServiceServer
          .bindTo(userApiService.restTemplate)
          .ignoreExpectOrder(true)
          .build();
    }
    var uri = uriTemplateHandler.expand(
      url,
      params
    );
    mockServer.expect(
        ExpectedCount.times(expectedRequestsCount),
        requestTo(uri)
      )
      .andRespond(
        withSuccess()
          .body(body)
          .contentType(new MediaType("application", "vnd.api+json"))
      );
  }
}
