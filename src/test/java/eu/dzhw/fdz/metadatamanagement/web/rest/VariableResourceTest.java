package eu.dzhw.fdz.metadatamanagement.web.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;

public class VariableResourceTest extends AbstractTest {
  private static final String API_SURVEYS_URI = "/api/variables";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private FdzProjectRepository fdzProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;
  
  @Autowired
  private VariableRepository variableRepository;
  
  @Autowired
  private ElasticsearchAdminService elasticsearchAdminService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
    elasticsearchAdminService.recreateAllIndices();
  }

  @After
  public void cleanUp() {
    fdzProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
  }
  
  @Test
  public void testCreateVariable() {
    
  }
}
