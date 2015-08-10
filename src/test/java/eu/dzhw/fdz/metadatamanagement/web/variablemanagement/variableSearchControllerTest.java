package eu.dzhw.fdz.metadatamanagement.web.variablemanagement;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;
import eu.dzhw.fdz.metadatamanagement.service.variablemanagement.VariableService;
import eu.dzhw.fdz.metadatamanagement.web.AbstractWebTest;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableResourceAssembler;
import eu.dzhw.fdz.metadatamanagement.web.variablemanagement.resources.VariableSearchPageResource;


public class variableSearchControllerTest extends AbstractWebTest {

  private MockMvc mockMvc;

  @Autowired
  private ControllerLinkBuilderFactory controllerLinkBuilderFactory;

  @Autowired
  private VariableService variableService;

  @Autowired
  private VariableResourceAssembler variableResourceAssembler;

  @Autowired
  private PagedResourcesAssembler<VariableDocument> pagedResourcesAssembler;


  @Override
  @Before
  public void setup() {
    this.mockMvc =
        MockMvcBuilders
            .standaloneSetup(
                new VariableSearchController(variableService, controllerLinkBuilderFactory,
                    variableResourceAssembler, pagedResourcesAssembler))
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setViewResolvers(new ViewResolver() {

              @Override
              public View resolveViewName(String viewName, Locale locale) throws Exception {
                // TODO Auto-generated method stub
                return new MappingJackson2JsonView();
              }
            }).build();
  }

  @Test
  public void testSearch() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/de/variables/search?query=name")).andExpect(status().isOk())
            .andExpect(request().asyncStarted())
            .andExpect(request().asyncResult(instanceOf(ModelAndView.class))).andReturn();
    this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk());

    ModelAndViewAssert.assertViewName((ModelAndView) mvcResult.getAsyncResult(),
        "variables/variableSearch");
    ModelAndViewAssert.assertModelAttributeValue((ModelAndView) mvcResult.getAsyncResult(),
        "query", "name");
    ModelAndViewAssert.assertModelAttributeAvailable((ModelAndView) mvcResult.getAsyncResult(),
        "resource");
    ModelAndViewAssert.assertAndReturnModelAttributeOfType(
        (ModelAndView) mvcResult.getAsyncResult(), "resource", VariableSearchPageResource.class);
  }
}
