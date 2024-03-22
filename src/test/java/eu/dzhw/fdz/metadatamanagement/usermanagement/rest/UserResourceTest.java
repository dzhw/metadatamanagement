package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Optional;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DataAcquisitionProjectCrudHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.AuthorityRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.MongoDbTokenStore;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto.ManagedUserDto;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;

/**
 * Test class for the UserResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserResource
 */
public class UserResourceTest extends AbstractTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthorityRepository authorityRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private MongoDbTokenStore tokenStore;

  @Autowired
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired
  private DataAcquisitionProjectRepository acquisitionProjectRepository;

  @Autowired
  private DataAcquisitionProjectCrudHelper crudHelper;

  private MockMvc restUserMockMvc;

  @BeforeEach
  public void setup() {
    UserResource userResource =
        new UserResource(userRepository, authorityRepository, tokenStore, userService, acquisitionProjectRepository, crudHelper);
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
  public void testCreateUserWithId() throws Exception {
    // Arrange
    User user = User.builder().id("testGetAllUser_ID").build();

    // Act

    // Assert
    restUserMockMvc
        .perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetAllUser() throws Exception {
    // Arrange

    // Act
    MvcResult mvcResult = restUserMockMvc.perform(get("/api/users")).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()).andReturn();
    String content = mvcResult.getResponse().getContentAsString();
    JSONArray jsonArray = new JSONArray(content);

    // Assert
    assertThat(content, not(nullValue()));
    assertThat(jsonArray.length(), is(4));
  }

  @Test
  public void testUpdateUser() throws IOException, Exception {

    assertThat(this.authorityRepository, not(nullValue()));

    // Arrange
    Optional<User> userO = this.userRepository.findOneByLogin("user");
    User user = userO.get();
    user.setEmail("userMod@localhost");
    ManagedUserDto dto = new ManagedUserDto(user);

    // Act
    MvcResult mvcResult = restUserMockMvc
        .perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dto)))
        .andExpect(status().isOk()).andReturn();
    String content = mvcResult.getResponse().getContentAsString();
    JSONObject jsonObject = new JSONObject(content);

    // Assert
    assertThat(content, not(nullValue()));
    assertThat(jsonObject.getString("email"), is("userMod@localhost"));
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

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void findUserWithFilter() throws Exception {
    restUserMockMvc.perform(get("/api/users/findUserWithFilter")
      .param("searchFilter", "user")
      .param("page", "0")
      .param("size", "10")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].login", is("anonymousUser")))
      .andExpect(jsonPath("$[0].firstName", is("Anonymous")))
      .andExpect(jsonPath("$[0].lastName", is("User")))
      .andExpect(jsonPath("$[0].email", is("anonymous@localhost")))
      .andExpect(jsonPath("$[1].login", is("user")))
      .andExpect(jsonPath("$[1].firstName", is("")))
      .andExpect(jsonPath("$[1].lastName", is("User")))
      .andExpect(jsonPath("$[1].email", is("userMod@localhost")));
  }

}
