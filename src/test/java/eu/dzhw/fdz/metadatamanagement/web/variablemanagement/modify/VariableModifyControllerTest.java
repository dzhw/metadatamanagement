package eu.dzhw.fdz.metadatamanagement.web.variablemanagement.modify;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;


public class VariableModifyControllerTest extends AbstractWebTest {

  @Test
  public void testget() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/variables/create")).andExpect(status().isOk())
            .andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();

    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
        .andExpect(content().string((containsString("Sprache"))))
        .andExpect(content().string(not(containsString("#{"))))
        .andExpect(content().string(not(containsString("${"))))
        .andExpect(content().string(not(containsString("??"))));
  }

  @Test
  public void testPost() throws Exception {

    /*
     * MvcResult mvcResult = this.mockMvc.perform(get("/")).andExpect(status().isOk())
     * .andExpect(request().asyncStarted())
     * .andExpect(request().asyncResult(instanceOf(String.class))).andReturn();
     * 
     * this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
     * .andExpect(model().attributeExists("person"))
     * .andExpect(content().string((not(containsString("</html>")))))
     * .andExpect(content().string((not(containsString("<body>")))))
     * .andExpect(content().string((containsString("has-error"))))
     * .andExpect(content().string(not(containsString("#{"))))
     * .andExpect(content().string(not(containsString("${"))));
     */
  }
}
