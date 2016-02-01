package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
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
import eu.dzhw.fdz.metadatamanagement.search.VariableSearchDao;
import eu.dzhw.fdz.metadatamanagement.search.document.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.service.enums.ElasticsearchIndices;

/**
 * Tests for the variable resource.
 * 
 * @author René Reitmann
 */
public class VariableResourceTest extends AbstractTest {
  private static final String API_VARIABLES_URI = "/api/variables";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private FdzProjectRepository fdzProjectRepository;

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
    fdzProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
  }

  @Test
  public void testCreateVariable() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withFdzProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    // check that there are two variable search documents
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(2.0));
    
    // call toString for test coverage :-)
    variable.toString();
  }
  
  @Test
  public void testCreateVariableWithSurveyButWithoutProject() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withFdzProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(null)
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with a survey but without a project
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateVariableWithUnknownSurvey() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withFdzProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id but with an unknown survey
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }
  
  @Test
  public void testCreateVariableWithUnknownProject() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id but with an unknown project
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }
  
  @Test
  public void testCreateVariableWithSurveyFromDifferentProject() throws Exception {
    FdzProject project1 = new FdzProjectBuilder().withId("testProject1")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project1);
    
    FdzProject project2 = new FdzProjectBuilder().withId("testProject2")
        .withCufDoi("testDoi")
        .withSufDoi("sufDoi")
        .build();
    fdzProjectRepository.save(project2);
    
    Survey survey = new SurveyBuilder().withId("testSurvey")
        .withFdzProjectId(project2.getId())
        .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
          .withEnd(LocalDate.now())
          .build())
        .withTitle(new I18nStringBuilder().withDe("Titel")
          .withEn("title")
          .build())
        .build();
    surveyRepository.save(survey);
    
    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project1.getId())
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id but with a survey from a different project
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void deleteVariable() throws JsonSyntaxException, IOException, Exception {
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withFdzProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
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
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withFdzProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    variable.setLabel("modified");

    // update the variable with the given id
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is2xxSuccessful());

    // read the updated variable and check the version
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId() + "?projection=complete"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(variable.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.label", is("modified")));

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

  @Test
  public void testDeletingSurveyDeletesVariable() throws Exception {
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withFdzProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
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
    FdzProject project = new FdzProjectBuilder().withId("testProject")
      .withCufDoi("testDoi")
      .withSufDoi("sufDoi")
      .build();
    fdzProjectRepository.save(project);

    Survey survey = new SurveyBuilder().withId("testSurvey")
      .withFdzProjectId(project.getId())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .withTitle(new I18nStringBuilder().withDe("Titel")
        .withEn("title")
        .build())
      .build();
    surveyRepository.save(survey);

    Variable variable = new VariableBuilder().withId("testVariable")
      .withDataType(DataType.numeric)
      .withScaleLevel(ScaleLevel.metric)
      .withFdzProjectId(project.getId())
      .withSurveyId(survey.getId())
      .withLabel("label")
      .withName("name")
      .build();

    // create the variable with the given id
    mockMvc.perform(
        put(API_VARIABLES_URI + "/" + variable.getId()).content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/fdz_projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());
    
    // check that the variable has been deleted
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isNotFound());
    
    // check that there are no variable search documents anymore
    elasticsearchAdminService.refreshAllIndices();
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0.0));
  }
}
