package eu.dzhw.fdz.metadatamanagement.authmanagement.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertThrows;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AbstractUserApiTests;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.UserApiService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.exception.InvalidUserApiResponseException;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import org.springframework.web.util.NestedServletException;

import java.util.Objects;

/**
 * Test class for the UserResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserResource
 */
public class UserResourceTests extends AbstractUserApiTests {
  private static final String USER_LOGIN = "user";
  private static final String USER_ID = "1234";

  private final MockMvc restUserMockMvc;

  public UserResourceTests(
      @Autowired final PageableHandlerMethodArgumentResolver pageableArgumentResolver,
      @Value("${metadatamanagement.authmanagement.server.endpoint}")
      final String authServerEndpoint,
      @Autowired final UserApiService userApiService
  ) {
    super(authServerEndpoint, userApiService);

    UserResource userResource =
        new UserResource(this.userApiService);
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
        .setCustomArgumentResolvers(pageableArgumentResolver).build();

    this.mockServer.users(
        new User(
            USER_ID,
            USER_LOGIN,
            "user@local",
            "de",
            false,
            "user"
        ),
        new User(
            "asdf",
            "admin",
            "admin@local",
            "de",
            false,
            "admin"
        )
    );
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.USER)
  public void patchDeactivatedWelcomeDialog_Success() throws Exception {
    this.addFindOneByLoginRequest(USER_LOGIN);
    this.addPatchDeactivatedWelcomeDialogById(USER_ID)
        .withSuccess()
            .body(u -> Objects.equals(u.getId(), USER_ID))
        .addToServer();

    // search with login;
    restUserMockMvc
      .perform(patch("/api/users/deactivatedWelcomeDialog")
        .param("deactivatedWelcomeDialog", "true")
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.USER)
  public void patchDeactivatedWelcomeDialog_Error() throws Exception {
    this.addFindOneByLoginRequest(USER_LOGIN);
    this.addPatchDeactivatedWelcomeDialogById(USER_ID)
      .withServerError()
      .addToServer();

    var exception = assertThrows(
      NestedServletException.class,
      () -> restUserMockMvc
          .perform(patch("/api/users/deactivatedWelcomeDialog")
              .param("deactivatedWelcomeDialog", "true")
              .contentType(TestUtil.APPLICATION_JSON_UTF8)
              .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isInternalServerError())
    );

    assertEquals(InvalidUserApiResponseException.class, exception.getCause().getClass());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void findUserWithRole_Success() throws Exception {
    final var USER_ROLE = AuthoritiesConstants.USER;
    final var ADMIN_EMAIL = "admin@local";
    final var ADMIN_ROLE = AuthoritiesConstants.ADMIN;

    this.addFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
        USER_LOGIN,
        USER_LOGIN,
        USER_ROLE
    );
    this.addFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
        ADMIN_EMAIL,
        ADMIN_EMAIL,
        ADMIN_ROLE
    );
    this.addFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
        USER_LOGIN,
        USER_LOGIN,
        ADMIN_ROLE
    );

    // search with login;
    restUserMockMvc
        .perform(get("/api/users/findUserWithRole/")
            .param("login", USER_LOGIN)
            .param("role", USER_ROLE)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].login").value(USER_LOGIN));

    // search with email substring
    restUserMockMvc
        .perform(get("/api/users/findUserWithRole/")
            .param("login", ADMIN_EMAIL)
            .param("role", ADMIN_ROLE)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].login").value("admin"));
    // search for user with false role

    restUserMockMvc
        .perform(get("/api/users/findUserWithRole/")
            .param("login", USER_LOGIN)
            .param("role", ADMIN_ROLE)
            .contentType(TestUtil.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json("[]"));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
  public void findUserWithRole_ServerError() {
    var role = AuthoritiesConstants.USER;
    this.startFindAllByLoginLikeOrEmailLikeAndByAuthoritiesContainingRequest(
        USER_LOGIN,
        USER_LOGIN,
        role
    )
        .withServerError()
        .addToServer();

    var exception = assertThrows(
        NestedServletException.class,
        () -> restUserMockMvc
            .perform(get("/api/users/findUserWithRole/")
                .param("login", USER_LOGIN)
                .param("role", role)
                .contentType(TestUtil.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
    );

    assertEquals(InvalidUserApiResponseException.class, exception.getCause().getClass());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testGetUserPublic_Success() throws Exception {
    this.addFindOneWithAuthoritiesByLoginRequest("unknown");
    this.addFindOneWithAuthoritiesByLoginRequest("admin");

    restUserMockMvc.perform(get("/api/users/unknown/public")
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    restUserMockMvc.perform(get("/api/users/admin/public")
          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.login").value("admin"));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testGetUserPublic_ServerError() {
    this.startFindOneWithAuthoritiesByLoginRequest("admin")
        .withServerError()
        .addToServer();

    var exception = assertThrows(
        NestedServletException.class,
        () -> restUserMockMvc.perform(get("/api/users/admin/public")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
    );

    assertEquals(InvalidUserApiResponseException.class, exception.getCause().getClass());
  }
}
