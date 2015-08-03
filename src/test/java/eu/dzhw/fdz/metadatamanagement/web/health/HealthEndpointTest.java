package eu.dzhw.fdz.metadatamanagement.web.health;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

/**
 * Check if the health endpoint exists and contains the custom elasticsearch node name.
 * 
 * @author René Reitmann
 */
public class HealthEndpointTest extends AbstractTest {

  @Test
  public void testNodeNameExistsAndStatusIsUp() throws Exception {
    MvcResult result = this.mockMvc.perform(get("/admin/health")).andExpect(status().isOk())
        .andExpect(content().string((containsString("nodeName")))).andReturn();

    JSONObject json = new JSONObject(result.getResponse().getContentAsString());
    assertThat(json.getJSONObject("elasticsearch").getString("status"), is("UP"));
  }

}
