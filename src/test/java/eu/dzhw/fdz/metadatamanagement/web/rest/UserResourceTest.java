package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Optional;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.dzhw.fdz.metadatamanagement.BasicTest;
import eu.dzhw.fdz.metadatamanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.domain.builders.UserBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.AuthorityRepository;
import eu.dzhw.fdz.metadatamanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.service.UserService;
import eu.dzhw.fdz.metadatamanagement.util.UnitTestReflectionUtils;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.ManagedUserDTO;

/**
 * Test class for the UserResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserResource
 */
public class UserResourceTest extends BasicTest{

  @Inject
  private UserRepository userRepository;

  @Inject
  private AuthorityRepository authorityRepository;

  @Inject
  private UserService userService;

  @Inject
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  private MockMvc restUserMockMvc;

  @Before
  public void setup() {
    UserResource userResource = new UserResource();
    ReflectionTestUtils.setField(userResource, "userRepository", this.userRepository);
    ReflectionTestUtils.setField(userResource, "userService", this.userService);
    ReflectionTestUtils.setField(userResource, "authorityRepository", this.authorityRepository);
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .build();
  }

  @Test
  public void testGetExistingUser() throws Exception {
    // Arrange

    // Act

    // Assert
    restUserMockMvc.perform(get("/api/users/admin").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"))
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
  public void testCreateUserWithID() throws Exception {
    // Arrange
    User user = new UserBuilder().withId("testGetAllUser_ID")
      .build();

    // Act

    // Assert
    restUserMockMvc.perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(user)))
      .andExpect(status().is4xxClientError());
  }

  //TODO Disable. It works local, but not in the cloud.
//  @Test
  public void testCreateUser() throws Exception {
    // Arrange
    String jsonCreateUser =
        "{\"login\":\"logintest\",\"password\":\"xwqyUOk2LCQ7IsuhhzVvxwqyUOk2LCQ7IsuhhzVvxwqyUOk2LCQ7IsuhhzVv\",\"email\":\"test@test.test\",\"activated\":false,\"langKey\":\"de\"}";

    // Act
    //With reflection, push the passwort.
    Field field = User.class.getDeclaredField("password");
    final JsonIgnore jsonIgnoreAnnotation = field.getAnnotation(JsonIgnore.class);
    boolean oldValue = (boolean) UnitTestReflectionUtils.changeAnnotationValue(jsonIgnoreAnnotation, "value", false);
    
    // Assert
    restUserMockMvc.perform(post("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(jsonCreateUser))
      .andExpect(status().isCreated());
    
    UnitTestReflectionUtils.changeAnnotationValue(jsonIgnoreAnnotation, "value", oldValue);
  }

  @Test
  public void testGetAllUser() throws Exception {
    // Arrange

    // Act
    MvcResult mvcResult = restUserMockMvc.perform(get("/api/users"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful())
      .andReturn();
    String content = mvcResult.getResponse()
      .getContentAsString();
    JSONArray jsonArray = new JSONArray(content);

    // Assert
    assertThat(content, not(nullValue()));
    assertThat(jsonArray.length(), is(8));
  }

  @Test
  public void testUpdateUser() throws IOException, Exception {

    assertThat(this.authorityRepository, not(nullValue()));

    // Arrange
    Optional<User> userO = this.userRepository.findOneByLogin("user");
    User user = userO.get();
    user.setEmail("userMod@localhost");
    ManagedUserDTO dto = new ManagedUserDTO(user);

    // Act
    MvcResult mvcResult =
        restUserMockMvc.perform(put("/api/users").contentType(TestUtil.APPLICATION_JSON_UTF8)
          .content(TestUtil.convertObjectToJsonBytes(dto)))
          .andExpect(status().isOk())
          .andReturn();
    String content = mvcResult.getResponse()
      .getContentAsString();
    JSONObject jsonObject = new JSONObject(content);

    // Assert
    assertThat(content, not(nullValue()));
    assertThat(jsonObject.getString("email"), is("userMod@localhost"));
  }
}
