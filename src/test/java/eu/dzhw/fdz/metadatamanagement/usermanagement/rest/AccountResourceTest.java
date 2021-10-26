package eu.dzhw.fdz.metadatamanagement.usermanagement.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;

/**
 * Test class for the AccountResource REST controller.
 *
 * @author Daniel Katzberg
 *
 */
public class AccountResourceTest extends AbstractTest {

  private MockMvc restUserMockMvc;

  private MockMvc restMvc;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);

    AccountResource accountResource =
        new AccountResource();

    AccountResource accountUserMockResource =
        new AccountResource();

    this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
    this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
  }

  @AfterEach
  public void cleanUp() {
    UnitTestUserManagementUtils.logout();
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

}
