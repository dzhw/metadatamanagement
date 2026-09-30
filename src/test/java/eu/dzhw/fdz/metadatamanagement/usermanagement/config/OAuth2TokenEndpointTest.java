package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.PasswordGrantAuthenticationToken;

@AutoConfigureMockMvc
public class OAuth2TokenEndpointTest extends AbstractTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private OAuth2AuthorizationService authorizationService;

  @Autowired
  private OpaqueTokenIntrospector opaqueTokenIntrospector;

  @MockBean
  private RegisteredClientRepository registeredClientRepository;

  @MockBean(name = "userDetailsService")
  private UserDetailsService userDetailsService;

  @Test
  public void testPasswordAndRefreshTokenGrants() throws Exception {
    RegisteredClient registeredClient = RegisteredClient.withId("test-client-id")
        .clientId("test-client")
        .clientSecret(passwordEncoder.encode("test-client-secret"))
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
        .authorizationGrantType(PasswordGrantAuthenticationToken.GRANT_TYPE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        .scope("read")
        .tokenSettings(TokenSettings.builder()
            .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
            .build())
        .build();
    when(registeredClientRepository.findByClientId("test-client")).thenReturn(registeredClient);
    when(registeredClientRepository.findById("test-client-id")).thenReturn(registeredClient);
    when(userDetailsService.loadUserByUsername("test-user"))
        .thenReturn(User.withUsername("test-user").password(passwordEncoder.encode("test-password"))
            .roles("USER").build());

    mockMvc.perform(post("/oauth/token")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("grant_type", "password")
      .param("client_id", "test-client")
      .param("client_secret", "test-client-secret")
      .param("username", "test-user")
      .param("password", "wrong-password")
      .param("scope", "read"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.error", is("invalid_grant")));

    mockMvc.perform(post("/oauth/token")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("grant_type", "password")
      .param("client_id", "test-client")
      .param("client_secret", "test-client-secret")
      .param("username", "test-user")
      .param("password", "test-password")
      .param("scope", "admin"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.error", is("invalid_scope")));

    MvcResult tokenResponse = mockMvc.perform(post("/oauth/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("grant_type", "password")
        .param("client_id", "test-client")
        .param("client_secret", "test-client-secret")
        .param("username", "test-user")
        .param("password", "test-password")
        .param("scope", "read"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token_type", is("Bearer")))
        .andExpect(jsonPath("$.access_token").isNotEmpty())
        .andExpect(jsonPath("$.refresh_token").isNotEmpty())
        .andReturn();

    String accessToken = JsonPath.read(tokenResponse.getResponse().getContentAsString(), "$.access_token");
    String refreshToken = JsonPath.read(tokenResponse.getResponse().getContentAsString(), "$.refresh_token");
    try {
      OAuth2Authorization authorization = authorizationService.findByToken(accessToken,
          OAuth2TokenType.ACCESS_TOKEN);
      assertThat(authorization, notNullValue());
      assertThat(authorization.getAccessToken().isActive(), is(true));
      assertThat(opaqueTokenIntrospector.introspect(accessToken).getName(), is("test-user"));

      mockMvc.perform(post("/oauth/token")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .param("grant_type", "refresh_token")
          .param("client_id", "test-client")
          .param("client_secret", "test-client-secret")
          .param("refresh_token", refreshToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.token_type", is("Bearer")))
          .andExpect(jsonPath("$.access_token").isNotEmpty())
          .andExpect(jsonPath("$.refresh_token").isNotEmpty());
    } finally {
      OAuth2Authorization authorization = authorizationService.findByToken(accessToken,
          OAuth2TokenType.ACCESS_TOKEN);
      if (authorization != null) {
        authorizationService.remove(authorization);
      }
    }
  }
}