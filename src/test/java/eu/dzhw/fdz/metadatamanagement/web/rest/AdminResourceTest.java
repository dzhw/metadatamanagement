package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestUtils;

public class AdminResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }
  
  @After
  public void cleanUp() {
    UnitTestUtils.logout();
  }
  
  @Test
  public void testRecreateAllIndices() throws Exception {
    UnitTestUtils.login("admin", "admin");
    
    // test recreation of all elasticsearch indices
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());
    
    // test recreation of all elasticsearch indices (should delete the previously
    // created indices)
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());
  }
  
  @Test(expected=NestedServletException.class)
  public void testRecreateAllIndicesWithoutValidCredentials() throws Exception {
    // test recreation of all elasticsearch indices
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());
    
    // TODO this should result in a more user friendly error message 
  }
}
