package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

public class AdminResourceTest extends AbstractTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

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
    UnitTestUserManagementUtils.logout();
    dataAcquisitionProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
  }

  @Test
  public void testRecreateAllIndices() throws Exception {
    UnitTestUserManagementUtils.login("admin", "admin");

    // test recreation of all elasticsearch indices
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());

    // test recreation of all elasticsearch indices (should delete the previously
    // created indices)
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());
  }

  @Test(expected = NestedServletException.class)
  public void testRecreateAllIndicesWithoutValidCredentials() throws Exception {
    // test recreation of all elasticsearch indices
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());

    // TODO this should result in a more user friendly error message
  }

  @Test
  public void testRecreateIndicesWithExistingVariables() throws Exception {
    UnitTestUserManagementUtils.login("admin", "admin");
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testId")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variableRepository.save(variable);

    // test recreation of all elasticsearch indices with previously created variable
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());

    elasticsearchAdminService.refreshAllIndices();

    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
}
