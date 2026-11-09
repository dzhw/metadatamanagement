package eu.dzhw.fdz.metadatamanagement.usermanagement.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;

import jakarta.servlet.Filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;
import com.nimbusds.jose.jwk.source.JWKSource;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

/**
 * Tests the URL based rules of the security filter chains against the real configuration.
 */
// use the bcrypt hash (cost 4) of the client secret which is configured for dev, test and prod
@TestPropertySource(properties = "jhipster.security.authentication.oauth.secret="
    + "$2a$04$1M3yLuQsTu2dJGl0Rj60c.d9dLt1hHpRNMLzilziLM50VgGJ52ckG")
@AutoConfigureMockMvc
public class SecurityFilterChainTest extends AbstractTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  @Qualifier("springSecurityFilterChain")
  private Filter springSecurityFilterChain;

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private OAuth2AuthorizationService authorizationService;

  @Autowired
  private OpaqueTokenIntrospector opaqueTokenIntrospector;

  @Test
  public void publicSearchRequestsBypassSecurity() {
    for (String uri : new String[] {"/api/search/variables/_search", "/api/search/_msearch",
        "/api/search/data_packages/_count", "/api/search/questions/_doc/_mget",
        "/api/search/surveys,instruments/_search"}) {
      assertThat(uri, firstMatchingChain("POST", uri).getFilters().isEmpty(), is(true));
    }
    assertThat(firstMatchingChain("GET", "/api/search/variables/_doc/var-1").getFilters()
        .isEmpty(), is(true));
    assertThat(firstMatchingChain("GET", "/scripts/app/app.js").getFilters().isEmpty(), is(true));
    assertThat(firstMatchingChain("GET", "/scripts/app/views/main.html").getFilters().isEmpty(),
        is(true));
  }

  @Test
  public void searchAdministrationIsNotBypassed() {
    for (String uri : new String[] {"/api/search/recreate", "/api/search/process-queue",
        "/api/search/variables/_doc", "/api/search/variables/_delete_by_query"}) {
      assertThat(uri, firstMatchingChain("POST", uri).getFilters().isEmpty(), is(false));
    }
  }

  @Test
  public void releasesAndAttachmentsOfProjectsArePublic() throws Exception {
    mockMvc.perform(get("/api/data-acquisition-projects/unknown/releases"))
        .andExpect(result -> assertThat(result.getResponse().getStatus(), not(401)));
    mockMvc.perform(get("/api/data-acquisition-projects/unknown/attachments"))
        .andExpect(result -> assertThat(result.getResponse().getStatus(), not(401)));
    // only paths ending with /releases are public
    mockMvc.perform(get("/api/data-acquisition-projects/unknown/releases/last"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void apiRequiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/users")).andExpect(status().isUnauthorized());
    mockMvc.perform(get("/api/data-acquisition-projects/unknown"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void invalidBearerTokenIsRejected() throws Exception {
    mockMvc.perform(get("/api/users").header(HttpHeaders.AUTHORIZATION, "Bearer unknown-token"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(authorities = "ROLE_USER")
  public void managementRequiresAdmin() throws Exception {
    mockMvc.perform(get("/management/configprops")).andExpect(status().isForbidden());
  }

  @Test
  public void forwardedHttpRequestsAreRedirectedToHttps() throws Exception {
    mockMvc.perform(get("/api/users").header("X-Forwarded-Proto", "http"))
        .andExpect(status().is3xxRedirection())
        .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl(),
            startsWith("https://")));
    // requests which did not pass a proxy are not redirected
    mockMvc.perform(get("/api/users")).andExpect(status().isUnauthorized());
  }

  @Test
  public void noJwkSetIsExposed() {
    assertThat(applicationContext.getBeanNamesForType(JWKSource.class).length, is(0));
  }

  /**
   * Replays the login request of the frontend (see auth.oauth2.service.js) against the real
   * registered client and the users created by the mongock changelog.
   */
  @Test
  public void frontendLoginWithSeededUser() throws Exception {
    MvcResult tokenResponse = mockMvc.perform(post("/oauth/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder()
            .encodeToString("metadatamanagementapp:metadatamanagementClient".getBytes()))
        .content("username=user&password=user&grant_type=password&scope=read%20write&"
            + "client_secret=mySecretOAuthSecret&client_id=metadatamanagementapp"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token_type", is("Bearer")))
        .andExpect(jsonPath("$.access_token").isNotEmpty())
        .andExpect(jsonPath("$.refresh_token").isNotEmpty())
        .andReturn();
    String accessToken =
        JsonPath.read(tokenResponse.getResponse().getContentAsString(), "$.access_token");
    try {
      assertThat(opaqueTokenIntrospector.introspect(accessToken).getName(), is("user"));
      mockMvc.perform(get("/api/account")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.login", is("user")));

      // logout revokes the token
      mockMvc.perform(post("/api/logout")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
          .andExpect(status().isOk());
      mockMvc.perform(get("/api/account")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
          .andExpect(status().isUnauthorized());
    } finally {
      OAuth2Authorization authorization =
          authorizationService.findByToken(accessToken, OAuth2TokenType.ACCESS_TOKEN);
      if (authorization != null) {
        authorizationService.remove(authorization);
      }
    }
  }

  @Test
  public void wrongPasswordOfSeededUserIsRejected() throws Exception {
    mockMvc.perform(post("/oauth/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder()
            .encodeToString("metadatamanagementapp:metadatamanagementClient".getBytes()))
        .content("username=user&password=wrong&grant_type=password&scope=read"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid_grant")));
  }

  @Test
  public void wrongClientSecretIsRejected() throws Exception {
    mockMvc.perform(post("/oauth/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder()
            .encodeToString("metadatamanagementapp:wrong".getBytes()))
        .content("username=user&password=user&grant_type=password&scope=read"))
        .andExpect(status().isUnauthorized());
  }

  private SecurityFilterChain firstMatchingChain(String method, String uri) {
    MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
    request.setServletPath(uri);
    FilterChainProxy filterChainProxy = (FilterChainProxy) springSecurityFilterChain;
    SecurityFilterChain chain = filterChainProxy.getFilterChains().stream()
        .filter(candidate -> candidate.matches(request)).findFirst().orElse(null);
    assertThat(uri, chain, notNullValue());
    return chain;
  }
}
