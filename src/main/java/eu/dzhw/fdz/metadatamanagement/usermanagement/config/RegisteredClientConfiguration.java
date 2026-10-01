package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.PasswordGrantAuthenticationToken;

@Configuration
public class RegisteredClientConfiguration {

  @Autowired
  private JHipsterProperties jhipsterProperties;

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    String clientId = jhipsterProperties.getSecurity().getAuthentication().getOauth().getClientid();
    RegisteredClient client = RegisteredClient.withId(clientId)
      .clientId(clientId)
        .clientSecret(jhipsterProperties.getSecurity().getAuthentication().getOauth().getSecret())
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
      .authorizationGrantType(PasswordGrantAuthenticationToken.GRANT_TYPE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .scope("read").scope("write")
      .tokenSettings(TokenSettings.builder()
        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
        .build())
        .build();
    return new InMemoryRegisteredClientRepository(client);
  }
}
