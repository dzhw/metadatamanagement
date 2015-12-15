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
import java.time.ZoneId;
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
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.domain.builders.FdzProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.domain.builders.PeriodBuilder;
import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.service.SurveyService;


/**
 * Test class for the SurveyResource REST controller.
 *
 * @see SurveyResource
 */
public class SurveyResourceIntTest extends AbstractTest {

  private static final String DEFAULT_ID = "HURZ";
  private static final String DEFAULT_TITLE = "AAAAA";
  private static final String UPDATED_TITLE = "BBBBB";

  private static final LocalDate DEFAULT_FIELD_PERIOD = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_FIELD_PERIOD = LocalDate.now(ZoneId.systemDefault());
  private static final String DEFAULT_FDZ_PROJECT_NAME = "AAAAA";
  private static final String UPDATED_FDZ_PROJECT_NAME = "BBBBB";

  @Inject
  private SurveyRepository surveyRepository;

  @Inject
  private FdzProjectRepository fdzProjectRepository;

  @Inject
  private SurveyService surveyService;

  @Inject
  private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Inject
  private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Inject
  private Validator validator;

  private MockMvc restSurveyMockMvc;

  private Survey survey;

  @PostConstruct
  public void setup() {
    MockitoAnnotations.initMocks(this);
    SurveyResource surveyResource = new SurveyResource();
    ReflectionTestUtils.setField(surveyResource, "surveyService", surveyService);
    this.restSurveyMockMvc = MockMvcBuilders.standaloneSetup(surveyResource)
      .setCustomArgumentResolvers(pageableArgumentResolver)
      .setMessageConverters(jacksonMessageConverter)
      .setValidator(validator)
      .build();
  }

  @Before
  public void initTest() {
    survey = new Survey();
    survey.setId(DEFAULT_ID);
    I18nString title = new I18nStringBuilder().withDe(DEFAULT_TITLE)
      .withEn(DEFAULT_TITLE)
      .build();
    survey.setTitle(title);
    survey.setFieldPeriod(new PeriodBuilder().withStart(DEFAULT_FIELD_PERIOD)
      .withEnd(DEFAULT_FIELD_PERIOD)
      .build());
    survey.setFdzProjectName(DEFAULT_FDZ_PROJECT_NAME);

    FdzProject fdzProject = new FdzProjectBuilder().withName(DEFAULT_FDZ_PROJECT_NAME)
      .build();
    fdzProjectRepository.insert(fdzProject);
    fdzProject = new FdzProjectBuilder().withName(UPDATED_FDZ_PROJECT_NAME)
      .build();
    fdzProjectRepository.insert(fdzProject);
  }

  @After
  public void cleanUp() {
    surveyRepository.deleteAll();
    fdzProjectRepository.deleteAll();
  }

  @Test
  public void createSurvey() throws Exception {
    int databaseSizeBeforeCreate = 0;
    // Create the Survey

    restSurveyMockMvc.perform(post("/api/surveys").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isCreated());

    // Validate the Survey in the database
    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeCreate + 1);
    Survey testSurvey = surveys.get(surveys.size() - 1);
    assertThat(testSurvey.getTitle()
      .getDe()).isEqualTo(DEFAULT_TITLE);
    assertThat(testSurvey.getFieldPeriod()
      .getStart()).isEqualTo(DEFAULT_FIELD_PERIOD);
    assertThat(testSurvey.getFdzProjectName()).isEqualTo(DEFAULT_FDZ_PROJECT_NAME);
  }

  @Test
  public void checkTitleIsRequired() throws Exception {
    int databaseSizeBeforeTest = 0;
    // set the field null
    survey.setTitle(null);

    // Create the Survey, which fails.

    restSurveyMockMvc.perform(post("/api/surveys").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest());

    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkFieldPeriodIsRequired() throws Exception {
    int databaseSizeBeforeTest = 0;
    // set the field null
    survey.setFieldPeriod(null);

    // Create the Survey, which fails.

    restSurveyMockMvc.perform(post("/api/surveys").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest());

    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void checkFieldPeriodIsInvalid() throws Exception {
    int databaseSizeBeforeTest = 0;
    // set the field null
    survey.setFieldPeriod(new PeriodBuilder().withStart(LocalDate.now())
      .withEnd(LocalDate.now()
        .minusDays(1))
      .build());

    // Create the Survey, which fails.

    restSurveyMockMvc.perform(post("/api/surveys").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest());

    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeTest);
  }
  
  @Test
  public void checkFieldPeriodIsValid() throws Exception {
    int databaseSizeBeforeTest = 0;
    // set the field null
    LocalDate now = LocalDate.now();
    survey.setFieldPeriod(new PeriodBuilder().withStart(now)
      .withEnd(now)
      .build());

    // Create the Survey, which fails.

    restSurveyMockMvc.perform(post("/api/surveys").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isCreated());

    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeTest + 1);
  }

  @Test
  public void checkFdzProjectNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = 0;
    // set the field null
    survey.setFdzProjectName(null);

    // Create the Survey, which fails.

    restSurveyMockMvc.perform(post("/api/surveys").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isBadRequest());

    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeTest);
  }

  @Test
  public void getAllPagedSurveys() throws Exception {
    // Initialize the database
    surveyRepository.save(survey);

    // Get all the surveys
    restSurveyMockMvc.perform(get("/api/surveys?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId())))
      .andExpect(jsonPath("$.[*].title.de").value(hasItem(DEFAULT_TITLE.toString())))
      .andExpect(
          jsonPath("$.[*].fieldPeriod.start").value(hasItem(DEFAULT_FIELD_PERIOD.toString())))
      .andExpect(
          jsonPath("$.[*].fdzProjectName").value(hasItem(DEFAULT_FDZ_PROJECT_NAME.toString())));
  }

  @Test
  public void getAllSurveys() throws Exception {
    // Initialize the database
    surveyRepository.save(survey);

    // Get all the surveys
    restSurveyMockMvc.perform(get("/api/surveys?getAll=true"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId())))
      .andExpect(jsonPath("$.[*].title.de").value(hasItem(DEFAULT_TITLE.toString())))
      .andExpect(
          jsonPath("$.[*].fieldPeriod.start").value(hasItem(DEFAULT_FIELD_PERIOD.toString())))
      .andExpect(
          jsonPath("$.[*].fdzProjectName").value(hasItem(DEFAULT_FDZ_PROJECT_NAME.toString())));
  }

  @Test
  public void getSurvey() throws Exception {
    // Initialize the database
    surveyRepository.save(survey);

    // Get the survey
    restSurveyMockMvc.perform(get("/api/surveys/{id}", survey.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(survey.getId()))
      .andExpect(jsonPath("$.title.de").value(DEFAULT_TITLE.toString()))
      .andExpect(jsonPath("$.fieldPeriod.start").value(DEFAULT_FIELD_PERIOD.toString()))
      .andExpect(jsonPath("$.fdzProjectName").value(DEFAULT_FDZ_PROJECT_NAME.toString()));
  }

  @Test
  public void getNonExistingSurvey() throws Exception {
    // Get the survey
    restSurveyMockMvc.perform(get("/api/surveys/{id}", Long.MAX_VALUE))
      .andExpect(status().isNotFound());
  }

  @Test
  public void updateSurvey() throws Exception {
    // Initialize the database
    surveyRepository.save(survey);

    int databaseSizeBeforeUpdate = 1;

    // Update the survey
    I18nString updatedTitle = new I18nStringBuilder().withDe(UPDATED_TITLE)
      .withEn(UPDATED_TITLE)
      .build();
    survey.setTitle(updatedTitle);
    survey.setFieldPeriod(new PeriodBuilder().withStart(UPDATED_FIELD_PERIOD)
      .withEnd(UPDATED_FIELD_PERIOD)
      .build());
    survey.setFdzProjectName(UPDATED_FDZ_PROJECT_NAME);

    restSurveyMockMvc.perform(put("/api/surveys").contentType(TestUtil.APPLICATION_JSON_UTF8)
      .content(TestUtil.convertObjectToJsonBytes(survey)))
      .andExpect(status().isOk());

    // Validate the Survey in the database
    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeUpdate);
    Survey testSurvey = surveys.get(surveys.size() - 1);
    assertThat(testSurvey.getTitle()
      .getDe()).isEqualTo(UPDATED_TITLE);
    assertThat(testSurvey.getFieldPeriod()
      .getStart()).isEqualTo(UPDATED_FIELD_PERIOD);
    assertThat(testSurvey.getFdzProjectName()).isEqualTo(UPDATED_FDZ_PROJECT_NAME);
  }

  @Test
  public void deleteSurvey() throws Exception {
    // Initialize the database
    surveyRepository.save(survey);

    int databaseSizeBeforeDelete = 1;

    // Get the survey
    restSurveyMockMvc
      .perform(delete("/api/surveys/{id}", survey.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
      .andExpect(status().isOk());

    // Validate the database is empty
    List<Survey> surveys = surveyRepository.findAll();
    assertThat(surveys).hasSize(databaseSizeBeforeDelete - 1);
  }
}
