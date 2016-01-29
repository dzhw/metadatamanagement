package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestUtils;

public class AdminResourceTest extends AbstractTest {
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
  }
  
  @After
  public void cleanUp() {
    UnitTestUtils.logout();
    fdzProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
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
  
  @Test
  public void testRecreateIndicesWithExistingVariables() throws Exception {
    UnitTestUtils.login("admin", "admin");
    FdzProject project = new FdzProjectBuilder().withId("testId")
      .withSufDoi("testDoi")
      .withCufDoi("testDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testId")
      .withFdzProject(project)
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withFdzProject(project)
      .withSurvey(survey)
      .withId("testId")
      .withLabel("label")
      .withName("name")
      .withScaleLevel(ScaleLevel.nominal)
      .withDataType(DataType.numeric)
      .build();
    variableRepository.save(variable);
    
    //test recreation of all elasticsearch indices with previously created variable
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());
    
    elasticsearchAdminService.refreshAllIndices();
    
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
}
