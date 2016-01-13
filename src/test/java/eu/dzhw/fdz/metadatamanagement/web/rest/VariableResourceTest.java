package eu.dzhw.fdz.metadatamanagement.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

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
import eu.dzhw.fdz.metadatamanagement.search.VariableSearchDao;
import eu.dzhw.fdz.metadatamanagement.service.FdzProjectService;
import eu.dzhw.fdz.metadatamanagement.service.SurveyService;
import eu.dzhw.fdz.metadatamanagement.service.VariableService;
import eu.dzhw.fdz.metadatamanagement.service.enums.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestUtils;
import eu.dzhw.fdz.metadatamanagement.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the VariableResource REST controller.
 *
 * @author Daniel Katzberg
 *
 * @see VariableResource
 */
public class VariableResourceTest extends AbstractTest {

  private static final String DEFAULT_ID = "VARID";
  private static final String DEFAULT_NAME = "AAAAA";
  private static final String UPDATED_NAME = "BBBBB";
  private static final DataType DEFAULT_DATA_TYPE = DataType.string;
  private static final DataType UPDATED_DATA_TYPE = DataType.numeric;
  private static final ScaleLevel DEFAULT_SCALE_LEVEL = ScaleLevel.ordinal;
  private static final ScaleLevel UPDATED_SCALE_LEVEL = ScaleLevel.nominal;
  private static final String DEFAULT_LABEL = "AAAAA";
  private static final String UPDATED_LABEL = "BBBBB";
  private static final String DEFAULT_FDZ_PROJECT_NAME = "AAAAA";
  private static final String UPDATED_FDZ_PROJECT_NAME = "BBBBB";
  private static final String DEFAULT_SURVEY_ID = "AAAAA";
  private static final String INVALID_SURVEY_ID = "BBBBB";

  @Inject
  private FdzProjectService fdzProjectService;

  @Inject
  private VariableService variableService;

  @Inject
  private SurveyService surveyService;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Inject
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Inject
  private ExceptionTranslator exceptionTranslator;

  @Inject
  private Validator validator;
  
  @Inject 
  private VariableSearchDao variableSearchDao;

  private MockMvc restVariableMockMvc;

  private Variable variable;

  private FdzProject fdzProject;

  private Survey survey;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    VariableResource variableResource = new VariableResource();
    ReflectionTestUtils.setField(variableResource, "variableService", variableService);
    this.restVariableMockMvc = MockMvcBuilders.standaloneSetup(variableResource)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setMessageConverters(jacksonMessageConverter)
      .setControllerAdvice(exceptionTranslator)
      .setValidator(validator)
      .build();
  }

  @Before
  public void initTest() {
    this.fdzProject = new FdzProjectBuilder().withName(DEFAULT_FDZ_PROJECT_NAME)
      .withCufDoi("CufDoi")
      .withSufDoi("SufDoi")
      .build();

    this.fdzProjectService.createFdzProject(this.fdzProject);

    this.survey = new SurveyBuilder().withFdzProjectName(DEFAULT_FDZ_PROJECT_NAME)
      .withId(DEFAULT_SURVEY_ID)
      .withTitle(new I18nStringBuilder().withDe("Test")
        .build())
      .withFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
        .withEnd(LocalDate.now())
        .build())
      .build();
    
    this.surveyService.createSurvey(survey);

    this.variable = new VariableBuilder().withId(DEFAULT_ID)
      .withName(DEFAULT_NAME)
      .withDataType(DEFAULT_DATA_TYPE)
      .withScaleLevel(DEFAULT_SCALE_LEVEL)
      .withLabel(DEFAULT_LABEL)
      .withFdzProjectName(DEFAULT_FDZ_PROJECT_NAME)
      .build();
  }

  @After
  public void afterEachTest() {
    for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
      this.variableSearchDao.refresh(index.getIndexName());
    }
    this.variableService.deleteAll();
    this.fdzProjectService.deleteAll();
    this.surveyService.deleteAll();
  }

  @Test
  public void createVariable() throws Exception {
    // Arrange

    // Act
    int databaseSizeBeforeCreate = 0;

    this.restVariableMockMvc
      .perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    // Validate the Variable in the database
    List<Variable> variables = variableService.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeCreate + 1);
    Variable testVariable = variables.get(variables.size() - 1);
    assertThat(testVariable.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testVariable.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
    assertThat(testVariable.getScaleLevel()).isEqualTo(DEFAULT_SCALE_LEVEL);
    assertThat(testVariable.getLabel()).isEqualTo(DEFAULT_LABEL);
    assertThat(testVariable.getFdzProjectName()).isEqualTo(DEFAULT_FDZ_PROJECT_NAME);
  }

  @Test
  public void createVariableWithError() throws Exception {
    // Arrange

    // Act
    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(this.variable)))
      .andExpect(status().isCreated());

    // Assert
    // Bad Request -> ID exist
    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(this.variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void checkNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = 0;

    // set the field null
    variable.setName(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableService.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkDataTypeIsRequired() throws Exception {
    int databaseSizeBeforeTest = 0;

    // set the field null
    variable.setDataType(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableService.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkScaleLevelIsRequired() throws Exception {
    int databaseSizeBeforeTest = 0;

    // set the field null
    variable.setScaleLevel(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableService.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkLabelIsRequired() throws Exception {
    int databaseSizeBeforeTest = 0;

    // set the field null
    variable.setLabel(null);

    // Create the Variable, which fails.

    restVariableMockMvc.perform(post("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());

    List<Variable> variables = variableService.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void getAllVariables() throws Exception {
    // Initialize the database
    variableService.createVariable(variable);

    // Get all the variables
    restVariableMockMvc.perform(get("/api/variables"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(variable.getId())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
      .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
      .andExpect(jsonPath("$.[*].scaleLevel").value(hasItem(DEFAULT_SCALE_LEVEL.toString())))
      .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
      .andExpect(
          jsonPath("$.[*].fdzProjectName").value(hasItem(DEFAULT_FDZ_PROJECT_NAME.toString())));
  }

  @Test
  public void getVariable() throws Exception {
    // Initialize the database
    variableService.createVariable(variable);

    // Get the variable
    restVariableMockMvc.perform(get("/api/variables/{id}", variable.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(variable.getId()))
      .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
      .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
      .andExpect(jsonPath("$.scaleLevel").value(DEFAULT_SCALE_LEVEL.toString()))
      .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
      .andExpect(jsonPath("$.fdzProjectName").value(DEFAULT_FDZ_PROJECT_NAME.toString()));
  }

  @Test
  public void getNonExistingVariable() throws Exception {
    // Get the variable
    restVariableMockMvc.perform(get("/api/variables/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  public void updateVariable() throws Exception {
    // Initialize the database
    variableService.createVariable(variable);

    int databaseSizeBeforeUpdate = 1;

    // Update the variable
    this.variable.setName(UPDATED_NAME);
    this.variable.setDataType(UPDATED_DATA_TYPE);
    this.variable.setScaleLevel(UPDATED_SCALE_LEVEL);
    this.variable.setLabel(UPDATED_LABEL);
    this.variable.setFdzProjectName(DEFAULT_FDZ_PROJECT_NAME);

    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isOk());

    // Validate the Variable in the database
    List<Variable> variables = variableService.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeUpdate);
    Variable testVariable = variables.get(variables.size() - 1);
    assertThat(testVariable.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testVariable.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    assertThat(testVariable.getScaleLevel()).isEqualTo(UPDATED_SCALE_LEVEL);
    assertThat(testVariable.getLabel()).isEqualTo(UPDATED_LABEL);
    assertThat(testVariable.getFdzProjectName()).isEqualTo(DEFAULT_FDZ_PROJECT_NAME);
  }

  @Test
  public void updateVariableWrongFdZName() throws Exception {
    // Arrange
    variableService.createVariable(variable);
    this.variable.setName(UPDATED_NAME);
    this.variable.setDataType(UPDATED_DATA_TYPE);
    this.variable.setScaleLevel(UPDATED_SCALE_LEVEL);
    this.variable.setLabel(UPDATED_LABEL);
    this.variable.setFdzProjectName(UPDATED_FDZ_PROJECT_NAME);

    // Act and Assert
    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void updateVariableWithError() throws Exception {

    // Arrange
    this.variableService.createVariable(variable);
    this.variable.setName(UPDATED_NAME);
    this.variable.setDataType(UPDATED_DATA_TYPE);
    this.variable.setScaleLevel(UPDATED_SCALE_LEVEL);
    this.variable.setLabel(UPDATED_LABEL);
    this.variable.setId("WrongID");

    // Act

    // Assert
    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void updateVariableWithInvalidSurveyId() throws Exception {

    // Arrange
    this.variableService.createVariable(variable);
    UnitTestUtils<Variable> test2 = new UnitTestUtils<>();
    
    this.variable.setSurveyId(INVALID_SURVEY_ID);
    test2.checkAndPrintValidation(variable, (javax.validation.Validator) this.validator);

    // Act

    // Assert
    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void updateVariableWithValidSurveyId() throws Exception {

    // Arrange
    this.variableService.createVariable(variable);
    this.variable.setSurveyId(DEFAULT_SURVEY_ID);

    // Act

    // Assert
    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isOk());
  }
  
  @Test
  public void updateVariableWithEmptySurveyId() throws Exception {

    // Arrange
    this.variableService.createVariable(variable);
    this.variable.setSurveyId(null);

    // Act

    // Assert
    restVariableMockMvc.perform(put("/api/variables").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isOk());
  }

  @Test
  public void deleteVariable() throws Exception {
    // Initialize the database
    variableService.createVariable(variable);

    int databaseSizeBeforeDelete = 1;

    // Get the variable
    restVariableMockMvc.perform(
        delete("/api/variables/{id}", variable.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    // Validate the database is empty
    List<Variable> variables = variableService.findAll();
    assertThat(variables).hasSize(databaseSizeBeforeDelete - 1);
  }
}
