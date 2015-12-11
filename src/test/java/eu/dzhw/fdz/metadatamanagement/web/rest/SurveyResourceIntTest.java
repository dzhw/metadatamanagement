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

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
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
    private SurveyService surveyService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSurveyMockMvc;

    private Survey survey;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SurveyResource surveyResource = new SurveyResource();
        ReflectionTestUtils.setField(surveyResource, "surveyService", surveyService);
        this.restSurveyMockMvc = MockMvcBuilders.standaloneSetup(surveyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        surveyRepository.deleteAll();
        survey = new Survey();
        survey.setId(DEFAULT_ID);
        survey.setTitle(DEFAULT_TITLE);
        survey.setFieldPeriod(DEFAULT_FIELD_PERIOD);
        survey.setFdzProjectName(DEFAULT_FDZ_PROJECT_NAME);
    }

    @Test
    public void createSurvey() throws Exception {
        int databaseSizeBeforeCreate = surveyRepository.findAll().size();

        // Create the Survey

        restSurveyMockMvc.perform(post("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(survey)))
                .andExpect(status().isCreated());

        // Validate the Survey in the database
        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeCreate + 1);
        Survey testSurvey = surveys.get(surveys.size() - 1);
        assertThat(testSurvey.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSurvey.getFieldPeriod()).isEqualTo(DEFAULT_FIELD_PERIOD);
        assertThat(testSurvey.getFdzProjectName()).isEqualTo(DEFAULT_FDZ_PROJECT_NAME);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setTitle(null);

        // Create the Survey, which fails.

        restSurveyMockMvc.perform(post("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(survey)))
                .andExpect(status().isBadRequest());

        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFieldPeriodIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setFieldPeriod(null);

        // Create the Survey, which fails.

        restSurveyMockMvc.perform(post("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(survey)))
                .andExpect(status().isBadRequest());

        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFdzProjectNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setFdzProjectName(null);

        // Create the Survey, which fails.

        restSurveyMockMvc.perform(post("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(survey)))
                .andExpect(status().isBadRequest());

        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllSurveys() throws Exception {
        // Initialize the database
        surveyRepository.save(survey);

        // Get all the surveys
        restSurveyMockMvc.perform(get("/api/surveys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].fieldPeriod").value(hasItem(DEFAULT_FIELD_PERIOD.toString())))
                .andExpect(jsonPath("$.[*].fdzProjectName").value(hasItem(DEFAULT_FDZ_PROJECT_NAME.toString())));
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
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.fieldPeriod").value(DEFAULT_FIELD_PERIOD.toString()))
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

		int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Update the survey
        survey.setTitle(UPDATED_TITLE);
        survey.setFieldPeriod(UPDATED_FIELD_PERIOD);
        survey.setFdzProjectName(UPDATED_FDZ_PROJECT_NAME);

        restSurveyMockMvc.perform(put("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(survey)))
                .andExpect(status().isOk());

        // Validate the Survey in the database
        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeUpdate);
        Survey testSurvey = surveys.get(surveys.size() - 1);
        assertThat(testSurvey.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSurvey.getFieldPeriod()).isEqualTo(UPDATED_FIELD_PERIOD);
        assertThat(testSurvey.getFdzProjectName()).isEqualTo(UPDATED_FDZ_PROJECT_NAME);
    }

    @Test
    public void deleteSurvey() throws Exception {
        // Initialize the database
        surveyRepository.save(survey);

		int databaseSizeBeforeDelete = surveyRepository.findAll().size();

        // Get the survey
        restSurveyMockMvc.perform(delete("/api/surveys/{id}", survey.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
