package eu.dzhw.fdz.metadatamanagement.authmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.authmanagement.config.MockJwtDecoder;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AbstractUserApiTests;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorizedRequestsTests extends AbstractUserApiTests {
  private static final User USER = new User(
      "1234",
      "admin",
      "admin@local",
      "de",
      false,
      AuthoritiesConstants.toSearchValue(AuthoritiesConstants.USER),
      AuthoritiesConstants.toSearchValue(AuthoritiesConstants.ADMIN)
  );

  private final MockMvc mvc;
  private final MockJwtDecoder jwtDecoder;

  public AuthorizedRequestsTests(
      @Value("${metadatamanagement.authmanagement.server.endpoint}") String authServerEndpoint,
      @Autowired UserApiService userApiService,
      @Autowired WebApplicationContext wac,
      @Autowired MockJwtDecoder jwtDecoder
  ) {
    super(authServerEndpoint, userApiService);

    mvc = MockMvcBuilders
        .webAppContextSetup(wac)
        .apply(springSecurity())
        .build();

    this.jwtDecoder = jwtDecoder;

    this.mockServer.users(USER);
  }

  @Test
  public void withoutJwtTest() throws Exception {
    mvc.perform(
        patch("/api/users/deactivatedWelcomeDialog")
    )
        .andExpect(unauthenticated());
  }

  @Test
  public void withEmptyJwtTest() throws Exception {
    mvc.perform(
        patch("/api/users/deactivatedWelcomeDialog")
            .param("deactivatedWelcomeDialog", "false")
            .with(jwt())
    )
        .andExpect(authenticated())
        .andExpect(status().isForbidden());
  }

  @Test
  public void withAuthorizationHeaderTest() throws Exception {
    this.addFindOneByLoginRequest(USER.getName());
    this.addPatchDeactivatedWelcomeDialogById(USER.getId())
        .withSuccess()
            .body(u -> Objects.equals(u.getId(), USER.getId()))
        .addToServer();

    mvc.perform(
        patch("/api/users/deactivatedWelcomeDialog")
            .param("deactivatedWelcomeDialog", "false")
            .header("Authorization", "Bearer " + jwtDecoder.generateTokenForUser(USER))
    )
        .andExpect(status().isOk());
  }

  @Test
  public void withXForwardProtoHeaderTest() throws Exception {
    mvc.perform(
        patch("/api/users/deactivatedWelcomeDialog")
            .param("deactivatedWelcomeDialog", "false")
            .header("X-Forwarded-Proto", "http")
            .with(jwt().jwt(jwt ->
                jwt
                    .header("alg", "rs256")
                    .subject("xForwardProtoTester"))
                    .authorities(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
            )
    )
        .andExpect(status().is(HttpStatus.SC_MOVED_TEMPORARILY))
        .andExpect(
            header().string("Location", "https://localhost/api/users/deactivatedWelcomeDialog")
        );

    mvc.perform(
        patch("/api/users/deactivatedWelcomeDialog")
            .param("deactivatedWelcomeDialog", "false")
            .header("X-Forwarded-Proto", "https")
            .with(jwt().jwt(jwt ->
                jwt
                    .header("alg", "rs256")
                    .subject("xForwardProtoTester"))
                    .authorities(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
            )
    )
        .andExpect(status().is(HttpStatus.SC_MOVED_TEMPORARILY))
        .andExpect(
            header().string("Location", "https://localhost/api/users/deactivatedWelcomeDialog")
        );
  }
}
