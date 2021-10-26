package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import eu.dzhw.fdz.metadatamanagement.authmanagement.service.AuthUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Test class for the UserResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserResource
 */
public class UserResourceTest extends AbstractTest {

  @Autowired
  private AuthUserService authUserService;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  private MockMvc restUserMockMvc;

  @BeforeEach
  public void setup() {
    UserResource userResource =
        new UserResource(authUserService);
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
        .setCustomArgumentResolvers(pageableArgumentResolver).build();
  }

  @Test
  public void testGetExistingUser() throws Exception {
    // Arrange

    // Act

    // Assert
    restUserMockMvc.perform(get("/api/users/admin").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.lastName").value("Administrator"));
  }

  @Test
  public void testGetUnknownUser() throws Exception {
    // Arrange

    // Act

    // Assert
    restUserMockMvc.perform(get("/api/users/unknown").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void findUserWithRole() throws Exception {
    // search with login;
    String role = AuthoritiesConstants.USER;
    String login = "user";
    restUserMockMvc
        .perform(get("/api//users/findUserWithRole/").param("login", login).param("role", role)
            .contentType(TestUtil.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].login").value("user"));
    // search with email substring
    login = "admin@local";
    role = AuthoritiesConstants.ADMIN;
    restUserMockMvc
        .perform(get("/api//users/findUserWithRole/").param("login", login).param("role", role)
            .contentType(TestUtil.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.[0].login").value("admin"));
    // search for user with false role
    login = "user";
    role = AuthoritiesConstants.ADMIN;
    restUserMockMvc
        .perform(get("/api//users/findUserWithRole/").param("login", login).param("role", role)
            .contentType(TestUtil.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().json("[]"));

  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testGetUserPublic() throws Exception {
    restUserMockMvc.perform(get("/api/users/unknown/public").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    restUserMockMvc.perform(get("/api/users/admin/public").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.login").value("admin"));
  }
}
