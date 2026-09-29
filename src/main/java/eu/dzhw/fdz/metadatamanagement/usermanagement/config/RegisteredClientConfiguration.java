package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties;

@Configuration
public class RegisteredClientConfiguration {

  @Autowired
  private JHipsterProperties jhipsterProperties;

  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId(jhipsterProperties.getSecurity().getAuthentication().getOauth().getClientid())
        .clientSecret(jhipsterProperties.getSecurity().getAuthentication().getOauth().getSecret())
        .authorizationGrantType(AuthorizationGrantType.PASSWORD)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .scope("read").scope("write")
        .build();
    return new InMemoryRegisteredClientRepository(client);
  }
}
