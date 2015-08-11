package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.details;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * Test which checks if the {@link VariableDetailsController} answers as expected
 * 
 * @author René Reitmann
 */
public class VariableDetailsControllerTest extends AbstractWebTest {

  @Test
  public void testGetByValidId() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/variables/FdZ_ID01")).andExpect(status().isOk())
            .andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(VariableResource.class))).andReturn();

    this.mockMvc
    // wait for the async result
        .perform(asyncDispatch(mvcResult)).andExpect(status().isOk());
  }

  @Test
  public void testGetByInValidId() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/variables/fjsgjfd")).andExpect(status().isOk())
            .andExpect(request().asyncStarted()).andReturn();

    this.mockMvc
    // wait for the async result
        .perform(asyncDispatch(mvcResult)).andExpect(status().isOk());

    assertThat(mvcResult.getAsyncResult(), is(nullValue()));
  }
}
