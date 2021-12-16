package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto.UserWithRolesDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto.UserApiResponse;
import eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto.UserApiResponseDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto.UserWelcomeDialogDeactivatedPatchRequest;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidUserApiResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Service which handles all requests to the Auth Server's User API.
 */
@Service
public class UserApiService {

  static final String USER_JSON_API_PATH = "/jsonapi/user/user";

  static final String FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT =
      USER_JSON_API_PATH
      + "?filter[roles.id]={roleId}";

  static final String FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT =
      USER_JSON_API_PATH
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

  static final String FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_AND_BY_AUTHORITIES_CONTAINING_ENDPOINT =
      FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT
      + "&filter[roles.id]={roleId}"
      + "&include=roles";

  static final String FIND_ALL_BY_LOGIN_IN_ENDPOINT =
      USER_JSON_API_PATH
      + "?filter[name-filter][condition][path]=name"
      + "&filter[name-filter][condition][operator]=IN";

  static final String FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE =
      "&filter[name-filter][condition][value][%d]={login}";

  static final String FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT =
      USER_JSON_API_PATH
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

  static final String FIND_ONE_WITH_AUTHORITIES_BY_LOGIN_ENDPOINT =
      FIND_ONE_BY_LOGIN_ENDPOINT
      + "&include=roles";

  static final String PATCH_DEACTIVATED_WELCOME_DIALOG_BY_ID_ENDPOINT =
      USER_JSON_API_PATH
      + "/{id}";

  final String authServerEndpoint;

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
    this.authServerEndpoint = authServerEndpoint;

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
   * @throws InvalidUserApiResponseException when the Server's status response is not OK
   *                                         (i.e. code 200)
   */
  public List<UserDto> findAllByAuthoritiesContaining(
      final String role
  ) throws InvalidUserApiResponseException {
    return this.doFindAllApiCall(
      FIND_ALL_BY_AUTHORITIES_CONTAINING_ENDPOINT,
      UserApiResponseDto.Users.class,
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
   * @throws InvalidUserApiResponseException when the Server's status response is not OK
   *                                         (i.e. code 200)
   */
  public List<UserDto> findAllByLoginLikeOrEmailLike(
      final String login,
      final String email
  ) throws InvalidUserApiResponseException {
    return this.doFindAllApiCall(
        FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_ENDPOINT,
        UserApiResponseDto.Users.class,
        login,
        email
    );
  }

  /**
   * Find all the users whose login is like the provided {@code login} parameter or whose email is
   * like the provided {@code email} paramater AND the users' have the role with a name matching
   * the {@code role} parameter.
   *
   * @param login a "CONTAINS" search parameter for the user's login
   * @param email a "CONTAINS" search parameter for the user's email
   * @param role a "EQUALS" search parameter for the name of a role a user is has
   * @return a group of users whose login contains {@code login} or the email contains {@code}
   *         email and has the role with the name {@code role}.
   * @throws InvalidUserApiResponseException when the Server's status response is not OK
   *                                         (i.e. code 200)
   */
  public List<UserWithRolesDto> findAllByLoginLikeOrEmailLikeAndByAuthoritiesContaining(
      final String login,
      final String email,
      final String role
  ) throws InvalidUserApiResponseException {
    return this.doFindAllApiCall(
        FIND_ALL_BY_LOGIN_LIKE_OR_EMAIL_LIKE_AND_BY_AUTHORITIES_CONTAINING_ENDPOINT,
        UserApiResponseDto.UsersWithRoles.class,
        login,
        email,
        AuthoritiesConstants.toSearchValue(role)
    );
  }

  /**
   * Find all the users whose login is included in the {@code logins} parameters.
   *
   * @param logins a group of search parameters that will be used to try to find users whose
   *               logins are included in the group
   * @return a group of users whose login field is included in the {@code logins} search parameter
   * @throws InvalidUserApiResponseException when the Server's status response is not OK
   *                                         (i.e. code 200)
   */
  public List<UserDto> findAllByLoginIn(final Set<String> logins)
      throws InvalidUserApiResponseException {

    StringBuilder sb = new StringBuilder(FIND_ALL_BY_LOGIN_IN_ENDPOINT);
    for (var i = 0; i < logins.size(); i++) {
      sb.append(
          String.format(FIND_ALL_BY_LOGIN_IN_PARAMETER_TEMPLATE, i)
      );
    }

    return doFindAllApiCall(
        sb.toString(),
        UserApiResponseDto.Users.class,
        logins.toArray()
    );
  }

  /**
   * Find a specific user based on either the user's login or the user's email.
   *
   * @param login a search parameter which should match a user's login
   * @param email a search parameter which should match a user's email
   * @return a user with either the provided login, email, or both
   * @throws InvalidUserApiResponseException when the Server's status response is not OK
   *                                         (i.e. code 200)
   */
  public Optional<UserDto> findOneByLoginOrEmail(
      final String login,
      final String email
  ) throws InvalidUserApiResponseException {
    return this.doFindAllApiCall(
        FIND_ONE_BY_LOGIN_OR_EMAIL_ENDPOINT,
        UserApiResponseDto.Users.class,
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
   * @throws InvalidUserApiResponseException when the Server's status response is not OK
   *                                         (i.e. code 200)
   */
  public Optional<UserDto> findOneByLogin(
      final String login
  ) throws InvalidUserApiResponseException {
    return this.doFindAllApiCall(
        FIND_ONE_BY_LOGIN_ENDPOINT,
        UserApiResponseDto.Users.class,
        login
    )
      // There should only be one or none response.
      .stream()
      .findFirst();
  }

  /**
   * Attempt to find a specific user based on the user's login, including the users roles.
   *
   * @param login a search parameter which will be used to match a user's login
   * @return info on the user whose login matches the {@code login} search parameter
   * @throws InvalidUserApiResponseException when the Server's status response is not OK
   *                                         (i.e. code 200)
   */
  public Optional<UserWithRolesDto> findOneWithAuthoritiesByLogin(
      final String login
  ) throws InvalidUserApiResponseException {
    return this.doFindAllApiCall(
        FIND_ONE_WITH_AUTHORITIES_BY_LOGIN_ENDPOINT,
        UserApiResponseDto.UsersWithRoles.class,
        login
      )
      // There should only be one or none response.
      .stream()
      .findFirst();
  }

  /**
   * Attempt to update the 'deactivated welcome dialog' flag for a specific user via the user API.
   *
   * @param login the login/name of the user which will be updated
   * @param deactivatedWelcomeDialog the value to which the deactivated welcome dialog flag will be
   *                                 updated to
   * @throws InvalidUserApiResponseException when the User API server returns a response whose
   *                                         status code is not 2XX, the response has errors, or no
   *                                         response was received at all.
   */
  public void patchDeactivatedWelcomeDialogById(
      final String login,
      final boolean deactivatedWelcomeDialog
  ) throws InvalidUserApiResponseException {
    var user = findOneByLogin(login);

    if (user.isEmpty()) {
      throw new InvalidUserApiResponseException(String.format(
          "Could not update user %s, because user was not found",
          login
      ));
    }

    var id = user.get().getId();
    if (!StringUtils.hasText(id)) {
      throw new InvalidUserApiResponseException(String.format(
          "Could not update user %s, because user id could not be found",
          login
      ));
    }

    var headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "vnd.api+json"));

    var request = new HttpEntity<>(
        new UserWelcomeDialogDeactivatedPatchRequest(id, deactivatedWelcomeDialog),
        headers
    );

    UserApiResponse response;
    try {
      response = restTemplate.patchForObject(
        PATCH_DEACTIVATED_WELCOME_DIALOG_BY_ID_ENDPOINT,
        request,
        UserApiResponse.class,
        id
      );
    } catch (RestClientException e) {
      throw new InvalidUserApiResponseException(String.format(
          "Could not update deactivated welcome dialog flag for user '%s'. Cause: %s",
          login,
          e.getMessage()
      ));
    }

    if (response == null) {
      throw new InvalidUserApiResponseException(String.format(
          "No response received while updating deactivated welcome dialog for user '%s'. "
          + "Assuming update failed",
          login
      ));
    }

    if (!response.isSuccessful()) {
      throw new InvalidUserApiResponseException(String.format(
          "Errors received while updating deactivated welcome dialog for user '%s'. Errors: %s",
          login,
          response.getErrors().stream()
              .map(UserApiResponse.Error::getDetail)
              .collect(Collectors.joining(","))
      ));
    }
  }

  /**
   * A check to see if the server(s) providing the User API are up and healthy.
   *
   * @return is/are User API Server(s) healthy?
   */
  public boolean isHealthy() {
    try {
      return this.restTemplate.getForEntity(
          USER_JSON_API_PATH,
          Void.class
      )
          .getStatusCode()
          .is2xxSuccessful();
    } catch (RestClientException e) {
      return false;
    }
  }

  private <T extends UserDto> List<T> doFindAllApiCall(
      final String apiUri,
      final Class<? extends UserApiResponseDto<T>> responseClazz,
      final Object... uriVariables
  ) throws InvalidUserApiResponseException {
    ResponseEntity<? extends UserApiResponseDto<T>> results;
    try {
      results = restTemplate.getForEntity(
        apiUri,
        responseClazz,
        uriVariables
      );
    } catch (RestClientException e) {
      throw new InvalidUserApiResponseException(String.format(
          "Error for GET request: %s. Cause: %s",
          apiUri,
          e.getMessage()
      ));
    }

    if (results.getBody() == null || results.getBody().getData() == null) {
      return List.of();
    }

    return results.getBody().getData();
  }
}
