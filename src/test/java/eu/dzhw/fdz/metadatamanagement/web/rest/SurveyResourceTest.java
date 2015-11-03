package eu.dzhw.fdz.metadatamanagement.web.rest;

import eu.dzhw.fdz.metadatamanagement.Application;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.repository.search.SurveySearchRepository;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.SurveyDTO;
import eu.dzhw.fdz.metadatamanagement.web.rest.mapper.SurveyMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SurveyResource REST controller.
 *
 * @see SurveyResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SurveyResourceTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final LocalDate DEFAULT_BEGIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private SurveyRepository surveyRepository;

    @Inject
    private SurveyMapper surveyMapper;

    @Inject
    private SurveySearchRepository surveySearchRepository;

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
        ReflectionTestUtils.setField(surveyResource, "surveyRepository", surveyRepository);
        ReflectionTestUtils.setField(surveyResource, "surveyMapper", surveyMapper);
        ReflectionTestUtils.setField(surveyResource, "surveySearchRepository", surveySearchRepository);
        this.restSurveyMockMvc = MockMvcBuilders.standaloneSetup(surveyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        survey = new Survey();
        survey.setTitle(DEFAULT_TITLE);
        survey.setBegin(DEFAULT_BEGIN);
        survey.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createSurvey() throws Exception {
        int databaseSizeBeforeCreate = surveyRepository.findAll().size();

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.surveyToSurveyDTO(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
                .andExpect(status().isCreated());

        // Validate the Survey in the database
        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeCreate + 1);
        Survey testSurvey = surveys.get(surveys.size() - 1);
        assertThat(testSurvey.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSurvey.getBegin()).isEqualTo(DEFAULT_BEGIN);
        assertThat(testSurvey.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setTitle(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.surveyToSurveyDTO(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
                .andExpect(status().isBadRequest());

        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSurveys() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get all the surveys
        restSurveyMockMvc.perform(get("/api/surveys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].begin").value(hasItem(DEFAULT_BEGIN.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get the survey
        restSurveyMockMvc.perform(get("/api/surveys/{id}", survey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.begin").value(DEFAULT_BEGIN.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSurvey() throws Exception {
        // Get the survey
        restSurveyMockMvc.perform(get("/api/surveys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

		int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Update the survey
        survey.setTitle(UPDATED_TITLE);
        survey.setBegin(UPDATED_BEGIN);
        survey.setEndDate(UPDATED_END_DATE);
        SurveyDTO surveyDTO = surveyMapper.surveyToSurveyDTO(survey);

        restSurveyMockMvc.perform(put("/api/surveys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
                .andExpect(status().isOk());

        // Validate the Survey in the database
        List<Survey> surveys = surveyRepository.findAll();
        assertThat(surveys).hasSize(databaseSizeBeforeUpdate);
        Survey testSurvey = surveys.get(surveys.size() - 1);
        assertThat(testSurvey.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSurvey.getBegin()).isEqualTo(UPDATED_BEGIN);
        assertThat(testSurvey.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

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
