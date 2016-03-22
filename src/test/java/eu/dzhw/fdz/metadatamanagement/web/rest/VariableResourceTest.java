package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonSyntaxException;

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
import eu.dzhw.fdz.metadatamanagement.search.VariableSearchDao;
import eu.dzhw.fdz.metadatamanagement.search.document.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.service.enums.ElasticsearchIndices;

/**
 * Tests for the variable resource.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class VariableResourceTest extends AbstractTest {
  private static final String API_VARIABLES_URI = "/api/variables";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private VariableSearchDao variableSearchDao;

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
    dataAcquisitionProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
  }

  @Test
  public void testCreateVariable() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    // check that there are two variable search documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));

    // check that auditing attributes have been set
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdAt", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedAt", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("system")))
      .andExpect(jsonPath("$.lastModifiedBy", is("system")));

    // call toString for test coverage :-)
    variable.toString();
  }

  @Test
  public void testCreateVariableWithSurveyButWithoutProject() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(null)
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithDuplicateNameWithinProject() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the first variable
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    Variable variable2 = new VariableBuilder().withId("testVariable2")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the second variable with the same name
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable2.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable2)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testCreateVariableWithUnknownSurvey() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with the given id but with an unknown survey
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testCreateVariableWithSurveyFromDifferentProject() throws Exception {
    DataAcquisitionProject project1 = new DataAcquisitionProjectBuilder().withId("testProject1")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project1);

    DataAcquisitionProject project2 = new DataAcquisitionProjectBuilder().withId("testProject2")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project2);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project2.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project1.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with the given id but with a survey from a different project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void deleteVariable() throws JsonSyntaxException, IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    // delete the variable
    mockMvc.perform(delete(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the variable has been deleted
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isNotFound());

    // check that there are no variable search documents anymore
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }

  @Test
  public void testUpdateVariable() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    variable.setLabel(new I18nStringBuilder().withDe("modified")
      .withEn("modified")
      .build());

    // update the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is2xxSuccessful());

    // read the updated variable and check the version
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(variable.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.label.de", is("modified")));

    // check that the variable search documents have been updated
    elasticsearchAdminService.refreshAllIndices();
    for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
      List<VariableSearchDocument> variableSearchDocuments =
          variableSearchDao.findAll(index.getIndexName());
      assertThat(variableSearchDocuments.size(), is(1));
      assertThat(variableSearchDocuments.get(0)
        .getLabel(), is("modified"));
    }
  }

  // TODO @Amine L. Fix the Test please. :) D. Katzberg
  // @Test
  public void testDeletingSurveyDeletesVariable() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/surveys/" + survey.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the variable has been deleted
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isNotFound());

    // check that there are no variable search documents anymore
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }

  @Test
  public void testDeletingProjectDeletesVariable() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withDataAcquisitionProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .withQuestionnaireId("QuestionnaireId")
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataTypes.numeric)
      .withScaleLevel(ScaleLevel.continous)
      .withDataAcquisitionProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel(new I18nStringBuilder().withDe("label")
        .withEn("label")
        .build())
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data_acquisition_projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the variable has been deleted
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isNotFound());

    // check that there are no variable search documents anymore
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }
}
