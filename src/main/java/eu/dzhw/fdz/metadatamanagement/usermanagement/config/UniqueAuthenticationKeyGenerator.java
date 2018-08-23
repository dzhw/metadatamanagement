package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

/**
 * The default key generator reuses the access token of other devices/browsers of the same user.
 * This one creates a new token for every login request.
 *
 * @author René Reitmann
 */
public class UniqueAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {

  private static final String CLIENT_ID = "client_id";

  private static final String SCOPE = "scope";

  private static final String USERNAME = "username";

  private static final String UUID_KEY = "uuid";

  /**
   * Generate an access_token for the given oauth2 request.
   */
  public String extractKey(OAuth2Authentication authentication) {
    Map<String, String> values = new LinkedHashMap<String, String>();
    OAuth2Request authorizationRequest = authentication.getOAuth2Request();
    if (!authentication.isClientOnly()) {
      values.put(USERNAME, authentication.getName());
    }
    values.put(CLIENT_ID, authorizationRequest.getClientId());
    if (authorizationRequest.getScope() != null) {
      values.put(SCOPE,
          OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
    }
    Map<String, Serializable> extentions = authorizationRequest.getExtensions();
    String uuid = (String) extentions.get(UUID_KEY);
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
      extentions.put(UUID_KEY, uuid);
    }
    values.put(UUID_KEY, uuid);

    return super.generateKey(values);
  }
}
