package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.domain.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto.UserApiResponseDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A Service which handles all requests to the Auth Server's User API.
 */
@Service
public class UserApiService {

  static final String FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT =
      "/jsonapi/user/user?filter[roles.id]={roleId}";

  static final String FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT =
      "/jsonapi/user/user"
      // Create an OR group
      + "?filter[or-group][group][conjunction]=OR"
      // Add a name CONTAINS filter
      + "&filter[name-filter][condition][path]=name"
      + "&filter[name-filter][condition][operator]=CONTAINS"
      + "&filter[name-filter][condition][value]={name}"
      + "&filter[name-filter][condition][memberOf]=or-group"
      // Add an email CONTAINS filter
      + "&filter[email-filter][condition][path]=mail"
      + "&filter[email-filter][condition][operator]=CONTAINS"
      + "&filter[email-filter][condition][value]={email}"
      + "&filter[email-filter][condition][memberOf]=or-group";

  static final String FIND_ALL_BY_LOGIN_IN_ENDPOINT =
      "/jsonapi/user/user"
      + "?filter[name-filter][condition][path]=name"
      + "&filter[name-filter][condition][operator]=IN";

  static final String FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE =
      "&filter[name-filter][condition][value][%d]={login}";

  static final String FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT =
      "/jsonapi/user/user"
      // Create an OR group
      + "?filter[or-group][group][conjunction]=OR"
      // Add a name equals filter
      + "&filter[name-filter][condition][path]=name"
      // The HTMLTemplate will handle the encoding of =
      + "&filter[name-filter][condition][operator]=="
      + "&filter[name-filter][condition][value]={name}"
      + "&filter[name-filter][condition][memberOf]=or-group"
      // Add an email equals filter
      + "&filter[email-filter][condition][path]=mail"
      // The HTMLTemplate will handle the encoding of =
      + "&filter[email-filter][condition][operator]=="
      + "&filter[email-filter][condition][value]={email}"
      + "&filter[email-filter][condition][memberOf]=or-group";

  static final String FIND_ONE_BY_LOGIN_ENDPOINT = "/jsonapi/user/user?filter[name]={name}";

  final RestTemplate restTemplate;

  /**
   * A constructor for building and authenticated the {@link RestTemplate} which
   * will be used to make the requests to the AUth Server's User API.
   *
   * @param authServerEndpoint the base of the URL to which each User API request will be sent
   * @param authServerUsername the username which will be used for the BASIC Auth
   * @param authServerPassword the password which will be used for the BASIC Auth
   */
  public UserApiService(
      @Value("${metadatamanagement.authmanagement.server.endpoint}")
      final String authServerEndpoint,
      @Value("${metadatamanagement.authmanagement.server.username}")
      final String authServerUsername,
      @Value("${metadatamanagement.authmanagement.server.password}")
      final String authServerPassword
  ) {
    restTemplate = new RestTemplateBuilder()
        .rootUri(authServerEndpoint)
        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
        .interceptors(new BasicAuthenticationInterceptor(authServerUsername, authServerPassword))
        .messageConverters(
            new MappingJackson2HttpMessageConverter()
        )
        .build();
  }

  /**
   * Find all the users who have the provided role.
   *
   * @param role the filtering role
   * @return a group of users that have the provided role
   * @throws InvalidResponseException when the Server's status response is not OK (i.e. code 200)
   */
  public List<UserDto> findAllByAuthoritiesContaining(
      final String role
  ) throws InvalidResponseException {
    return this.doFindAllApiCall(
      FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT,
      AuthoritiesConstants.toSearchValue(role)
    );
  }

  /**
   * Find all the users whose login is like the provided {@code login} parameter or whose email is
   * like the provided {@code email} parameter.
   *
   * @param login a "CONTAINS" search parameter for the user's login
   * @param email a "CONTAINS" search parameter for the user's email
   * @return a group of users whose login contains the {@code login} parameter or whose email
   *         contains the {@code email} parameter
   * @throws InvalidResponseException when the Server's status response is not OK (i.e. code 200)
   */
  public List<UserDto> findAllByLoginLikeOrEmailLike(
      final String login,
      final String email
  ) throws InvalidResponseException {
    return this.doFindAllApiCall(
        FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT,
        login,
        email
    );
  }

  /**
   * Find all the users whose login is included in the {@code logins} parameters.
   *
   * @param logins a group of search parameters that will be used to try to find users whose
   *               logins are included in the group
   * @return a group of users whose login field is included in the {@code logins} search parameter
   * @throws InvalidResponseException when the Server's status response is not OK (i.e. code 200)
   */
  public List<UserDto> findAllByLoginIn(final Set<String> logins)
      throws InvalidResponseException {

    StringBuilder sb = new StringBuilder(FIND_ALL_BY_LOGIN_IN_ENDPOINT);
    for (var i = 0; i < logins.size(); i++) {
      sb.append(
          String.format(FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE, i)
      );
    }

    return doFindAllApiCall(sb.toString(), logins);
  }

  /**
   * Find a specific user based on either the user's login or the user's email.
   *
   * @param login a search parameter which should match a user's login
   * @param email a search parameter which should match a user's email
   * @return a user with either the provided login, email, or both
   * @throws InvalidResponseException when the Server's status response is not OK (i.e. code 200)
   */
  public Optional<UserDto> findOneByLoginOrEmail(
      final String login,
      final String email
  ) throws InvalidResponseException {
    return this.doFindAllApiCall(
        FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT,
        login,
        email
    )
        // There should only be one or none response.
        .stream()
        .findFirst();
  }

  /**
   * Attempt to find a specific user based on the user's login.
   *
   * @param login a search parameter which will be used to match a user's login
   * @return info on the user whose login matches the {@code login} search parameter
   * @throws InvalidResponseException when the Server's status response is not OK (i.e. code 200)
   */
  public Optional<UserDto> findOneByLogin(
      final String login
  ) throws InvalidResponseException {
    return this.doFindAllApiCall(
        FIND_ONE_BY_LOGIN_ENDPOINT,
        login
      )
      // There should only be one or none response.
      .stream()
      .findFirst();
  }

  private List<UserDto> doFindAllApiCall(
      final String apiUri,
      final Object... uriVariables
  ) throws InvalidResponseException {
    var results = restTemplate.getForEntity(
        apiUri,
        UserApiResponseDto.class,
        uriVariables
    );

    if (results.getStatusCode() != HttpStatus.OK) {
      throw new InvalidResponseException(
        String.format(
          "Invalid Server status code received. Response Status: %s",
          results.getStatusCode()
        )
      );
    }

    if (results.getBody() == null) {
      return List.of();
    }

    return results.getBody().getData();
  }
}
