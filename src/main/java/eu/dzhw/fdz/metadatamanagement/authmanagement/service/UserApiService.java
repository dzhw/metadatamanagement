package eu.dzhw.fdz.metadatamanagement.authmanagement.service;

import eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto.UserApiResponseDto;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * A Service which handles all requests to the Auth Server's User API.
 */
@Service
public class UserApiService {

  @Value("${metadatamanagement.authmanagement.server.endpoint}")
  private String authServerEndpoint;

  private final RestTemplate restTemplate;

  /**
   * A constructor for building and authenticated the {@link RestTemplate} which
   * will be used to make the requests to the AUth Server's User API.
   *
   * @param authServerUsername the username which will be used for the BASIC Auth
   * @param authServerPassword the password which will be used for the BASIC Auth
   */
  public UserApiService(
      @Value("${metadatamanagement.authmanagement.server.username}")
      final String authServerUsername,
      @Value("${metadatamanagement.authmanagement.server.password}")
      final String authServerPassword
  ) {
    restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    restTemplate.getInterceptors().add(
        new BasicAuthenticationInterceptor(authServerUsername, authServerPassword)
    );
    restTemplate.getMessageConverters().add(
        0,
        new StringHttpMessageConverter(StandardCharsets.UTF_8)
    );
  }

  /**
   * Find all the users who have the provided role.
   *
   * @param role the filtering role
   * @return a group of users that have the provided role
   * @throws InvalidResponseException when the Server's status response is not OK (i.e. code 200)
   */
  public List<UserApiResponseDto.UserDto> findAllByAuthoritiesContaining(
      String role
  ) throws InvalidResponseException {
    var results = restTemplate.getForEntity(
        String.format(
          "%s/jsonapi/user/user?filter[roles.id]={roleId}",
          authServerEndpoint
        ),
        UserApiResponseDto.class,
        role
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
