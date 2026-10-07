package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.MongoDbOAuth2AuthorizationService;

public class MongoDbOAuth2AuthorizationServiceTest extends AbstractTest {

  @Autowired
  private OAuth2AuthorizationRepository repository;

  private MongoDbOAuth2AuthorizationService service;
  private OAuth2Authorization authorization;

  @BeforeEach
  public void beforeEachTest() {
    RegisteredClient registeredClient = RegisteredClient.withId("client-id")
        .clientId("web-app")
        .authorizationGrantType(AuthorizationGrantType.PASSWORD)
        .build();

    RegisteredClientRepository registeredClientRepository =
        new InMemoryRegisteredClientRepository(registeredClient);
    service = new MongoDbOAuth2AuthorizationService(repository, registeredClientRepository);

    Instant now = Instant.now();
    authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
        .id(UUID.randomUUID().toString())
        .principalName("principal")
        .authorizationGrantType(AuthorizationGrantType.PASSWORD)
        .token(new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
            "accessToken", now, now.plusSeconds(3600)))
        .token(new OAuth2RefreshToken("refreshToken", now, now.plusSeconds(86400)))
        .build();

    service.save(authorization);
  }

  @AfterEach
  public void afterEachTest() {
    service.remove(authorization);
  }

  @Test
  public void testFindByAccessToken() {
    OAuth2Authorization found = service.findByToken("accessToken", OAuth2TokenType.ACCESS_TOKEN);
    assertThat(found, not(nullValue()));
    assertThat(found.getPrincipalName(), is("principal"));
  }

  @Test
  public void testFindByRefreshToken() {
    OAuth2Authorization found = service.findByToken("refreshToken", OAuth2TokenType.REFRESH_TOKEN);
    assertThat(found, not(nullValue()));
    assertThat(found.getPrincipalName(), is("principal"));
  }

  @Test
  public void testFindById() {
    OAuth2Authorization found = service.findById(authorization.getId());
    assertThat(found, not(nullValue()));
    assertThat(found.getPrincipalName(), is("principal"));
  }

  @Test
  public void testRemove() {
    service.remove(authorization);
    assertThat(service.findById(authorization.getId()), is(nullValue()));
  }

  @Test
  public void testRemoveByPrincipalName() {
    service.removeByPrincipalName("principal");
    assertThat(service.findByToken("accessToken", OAuth2TokenType.ACCESS_TOKEN), is(nullValue()));
  }
}
