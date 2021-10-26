package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.service.UserService;

/**
 * Test class for the AccountResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see UserService
 */
public class AccountResourceTest extends AbstractTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Mock
  private UserService mockUserService;

  private MockMvc restUserMockMvc;

  private MockMvc restMvc;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);

    AccountResource accountResource =
        new AccountResource(userService);

    AccountResource accountUserMockResource =
        new AccountResource(mockUserService);

    this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
  }

  @AfterEach
  public void cleanUp() {
    UnitTestUserManagementUtils.logout();
    // assert that all tests remove their users!
    assertThat(userRepository.count()).isEqualTo(4);
  }

  @Test
  public void testNonAuthenticatedUser() throws Exception {
    restUserMockMvc.perform(get("/api/authenticate").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(content().string(""));
  }

  @Test
  public void testAuthenticatedUser() throws Exception {
    restUserMockMvc.perform(get("/api/authenticate").with(request -> {
      request.setRemoteUser("test");
      return request;
    }).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().string("test"));
  }


  @Test
  public void testChangePassword() throws Exception {
    // Arrange

    // Act

    // Assert
    this.restMvc.perform(
        post("/api/account/change-password").accept(MediaType.APPLICATION_JSON).content("password"))
        .andExpect(status().isOk());
  }

  @Test
  public void testChangePasswordError() throws Exception {
    // Arrange

    // Act

    // Assert
    this.restMvc
        .perform(
            post("/api/account/change-password").accept(MediaType.APPLICATION_JSON).content("no"))
        .andExpect(status().is4xxClientError());
  }
}
