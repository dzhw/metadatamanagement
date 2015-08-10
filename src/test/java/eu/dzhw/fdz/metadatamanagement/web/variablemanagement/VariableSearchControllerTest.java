package eu.dzhw.fdz.metadatamanagement.web.variablemanagement;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableSearchPageResource;


public class VariableSearchControllerTest extends AbstractWebTest {
  @Test
  public void testSearch() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/search?query=name"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk());

    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/variableSearch");
    ModelAndViewAssert.assertModelAttributeValue((ModelAndView) mvcResult.getAsyncResult(), "query",
        "name");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", VariableSearchPageResource.class);
  }
}
