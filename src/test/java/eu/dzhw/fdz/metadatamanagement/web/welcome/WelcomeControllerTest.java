package eu.dzhw.fdz.metadatamanagement.web.welcome;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

/**
 * Test which checks if the {@link WelcomeController} answers as expected
 * 
 * @author René Reitmann
 */
public class WelcomeControllerTest extends AbstractTest {

  @Test
  public void testGermanWelcomePage() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/de")).andExpect(status().isOk())
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    mvcResult = this.mockMvc
        // wait for the async result
        .perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        // check that the german version is rendered
        .andExpect(content().string((containsString("Sprache"))))
        // ensure that all thymeleaf/spel tags are processed
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${")))).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "WelcomeControllerTest.testGermanWelcomePage");
  }

  @Test
  public void testEnglishWelcomePage() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/en")).andExpect(status().isOk())
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    mvcResult = this.mockMvc
        // wait for the async result
        .perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        // check that the german version is rendered
        .andExpect(content().string((containsString("Language"))))
        // ensure that all thymeleaf/spel tags are processed
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${")))).andReturn();

    // W3C Validation Check
    this.checkHtmlValidation(mvcResult.getResponse().getContentAsString(),
        "WelcomeControllerTest.testGermanWelcomePage");
  }

  @Test
  public void testDefaultRedirectionToDefaultLanguage() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/")).andExpect(status().isOk())
        .andExpect(request().asyncStarted())
        .andExpect(request().asyncResult(instanceOf(String.class))).andReturn();

    this.mockMvc
        // wait for the async result and ensure we are redirected
        .perform(asyncDispatch(mvcResult)).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/en")).andReturn();

    // NO W3C Validation Test, because of the redirection
  }

}
