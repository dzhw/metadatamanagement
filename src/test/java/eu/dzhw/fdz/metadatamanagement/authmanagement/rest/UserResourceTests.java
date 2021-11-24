package eu.dzhw.fdz.metadatamanagement.authmanagement.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AbstractUserApiTests;
import eu.dzhw.fdz.metadatamanagement.authmanagement.service.utils.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;

/**
 * Test class for the UserResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserResource
 */
public class UserResourceTests extends AbstractUserApiTests {

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  private MockMvc restUserMockMvc;

  @BeforeEach
  public void setup() {
    UserResource userResource =
        new UserResource(this.userApiService);
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
        .setCustomArgumentResolvers(pageableArgumentResolver).build();
  }

  @BeforeEach
  public void addUsers() {
    this.mockServer.users(
        new User(
            "1234",
            "user",
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
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void findUserWithRole() throws Exception {
    final var USER_LOGIN = "user";
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
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testGetUserPublic() throws Exception {
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
}
