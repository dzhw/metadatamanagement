package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;

/**
 * Test which checks if the {@link VariableSearchController} answers as expected
 * 
 * @author Amine Limouri
 */
public class VariableSearchControllerTest extends AbstractWebTest {

  @Test
  public void testGermanTemplate() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/search"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))));

    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");
    assertThat(resource.getPage().getContent().size(), is(greaterThan(0)));
  }

  @Test
  public void testEnglishTemplate() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/en/variables/search"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Language"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))));

    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");
    assertThat(resource.getPage().getContent().size(), is(greaterThan(0)));
  }

  @Test
  public void testSearch() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de/variables/search?query=ALLBUS"))
        .andExpect(status().isOk()).andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/variableSearch");
    ModelAndViewAssert.assertModelAttributeValue((ModelAndView) mvcResult.getAsyncResult(), "query",
        "ALLBUS");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", VariableSearchPageResource.class);

    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");
    assertThat(resource.getPage().getContent().size(), is(8));
  }

  @Test
  public void testSearchWithPage() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/variables/search?query=ALLBUS&page=1&size=3"))
            .andExpect(status().isOk()).andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/variableSearch");
    ModelAndViewAssert.assertModelAttributeValue((ModelAndView) mvcResult.getAsyncResult(), "query",
        "ALLBUS");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", VariableSearchPageResource.class);

    VariableSearchPageResource resource =
        (VariableSearchPageResource) ((ModelAndView) mvcResult.getAsyncResult()).getModelMap()
            .get("resource");

    assertThat(resource.getPage().getContent().size(), is(3));
    assertThat(resource.getPage().getId().getHref()
        .contains("/de/variables/search?query=ALLBUS&page=1&size=3"), is(true));
    assertThat(resource.getPage().getPreviousLink().getHref()
        .contains("/de/variables/search?query=ALLBUS&page=0&size=3"), is(true));
    assertThat(resource.getPage().getNextLink().getHref()
        .contains("/de/variables/search?query=ALLBUS&page=2&size=3"), is(true));

  }
}
