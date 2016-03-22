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
import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.domain.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.SurveyBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.VariableBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestUserManagementUtils;

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

    Survey survey = new SurveyBuilder().withId("testId")
      .withDataAcquisitionProjectId(project.getId())
      .withTitle(new I18nStringBuilder().withDe("titel")
        .withEn("title")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withId("testId")
      .withLabel(new I18nStringBuilder().withDe("label").withEn("label").build())
      .withName("name")
      .withScaleLevel(ScaleLevel.nominal)
      .withDataType(DataTypes.numeric)
      .build();
    variableRepository.save(variable);

    // test recreation of all elasticsearch indices with previously created variable
    mockMvc.perform(post("/api/admin/elasticsearch/recreate"))
      .andExpect(status().isOk());

    elasticsearchAdminService.refreshAllIndices();

    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
  }
}
