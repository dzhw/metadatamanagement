package eu.dzhw.fdz.metadatamanagement.web.rest;

import eu.dzhw.fdz.metadatamanagement.Application;
import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.repository.search.VariableSearchRepository;
import eu.dzhw.fdz.metadatamanagement.web.rest.dto.VariableDto;
import eu.dzhw.fdz.metadatamanagement.web.rest.mapper.VariableMapper;

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
import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eu.dzhw.fdz.metadatamanagement.domain.enumeration.DataTypes;
import eu.dzhw.fdz.metadatamanagement.domain.enumeration.ScaleLevel;

/**
 * Test class for the VariableResource REST controller.
 *
 * @see VariableResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class VariableResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";


private static final DataTypes DEFAULT_DATA_TYPE = DataTypes.string;
    private static final DataTypes UPDATED_DATA_TYPE = DataTypes.numeric;
    private static final String DEFAULT_LABEL = "AAAAA";
    private static final String UPDATED_LABEL = "BBBBB";
    private static final String DEFAULT_QUESTION = "AAAAA";
    private static final String UPDATED_QUESTION = "BBBBB";


private static final ScaleLevel DEFAULT_SCALE_LEVEL = ScaleLevel.nominal;
    private static final ScaleLevel UPDATED_SCALE_LEVEL = ScaleLevel.ordinal;

    @Inject
    private VariableRepository variableRepository;

    @Inject
    private VariableMapper variableMapper;

    @Inject
    private VariableSearchRepository variableSearchRepository;

//    @Inject
    @Resource(name="mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVariableMockMvc;

    private Variable variable;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VariableResource variableResource = new VariableResource();
        ReflectionTestUtils.setField(variableResource, "variableRepository", variableRepository);
        ReflectionTestUtils.setField(variableResource, "variableMapper", variableMapper);
        ReflectionTestUtils.setField(variableResource, "variableSearchRepository", variableSearchRepository);
        this.restVariableMockMvc = MockMvcBuilders.standaloneSetup(variableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        variable = new Variable();
        variable.setName(DEFAULT_NAME);
        variable.setDataType(DEFAULT_DATA_TYPE);
        variable.setLabel(DEFAULT_LABEL);
        variable.setQuestion(DEFAULT_QUESTION);
        variable.setScaleLevel(DEFAULT_SCALE_LEVEL);
    }

    @Test
    @Transactional
    public void createVariable() throws Exception {
        int databaseSizeBeforeCreate = variableRepository.findAll().size();

        // Create the Variable
        VariableDto variableDto = variableMapper.variableToVariableDto(variable);

        restVariableMockMvc.perform(post("/api/variables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variableDto)))
                .andExpect(status().isCreated());

        // Validate the Variable in the database
        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeCreate + 1);
        Variable testVariable = variables.get(variables.size() - 1);
        assertThat(testVariable.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVariable.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testVariable.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testVariable.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testVariable.getScaleLevel()).isEqualTo(DEFAULT_SCALE_LEVEL);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = variableRepository.findAll().size();
        // set the field null
        variable.setName(null);

        // Create the Variable, which fails.
        VariableDto variableDto = variableMapper.variableToVariableDto(variable);

        restVariableMockMvc.perform(post("/api/variables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variableDto)))
                .andExpect(status().isBadRequest());

        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = variableRepository.findAll().size();
        // set the field null
        variable.setDataType(null);

        // Create the Variable, which fails.
        VariableDto variableDto = variableMapper.variableToVariableDto(variable);

        restVariableMockMvc.perform(post("/api/variables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variableDto)))
                .andExpect(status().isBadRequest());

        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkScaleLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = variableRepository.findAll().size();
        // set the field null
        variable.setScaleLevel(null);

        // Create the Variable, which fails.
        VariableDto variableDto = variableMapper.variableToVariableDto(variable);

        restVariableMockMvc.perform(post("/api/variables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variableDto)))
                .andExpect(status().isBadRequest());

        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVariables() throws Exception {
        // Initialize the database
        variableRepository.saveAndFlush(variable);

        // Get all the variables
        restVariableMockMvc.perform(get("/api/variables"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(variable.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
                .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
                .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
                .andExpect(jsonPath("$.[*].scaleLevel").value(hasItem(DEFAULT_SCALE_LEVEL.toString())));
    }

    @Test
    @Transactional
    public void getVariable() throws Exception {
        // Initialize the database
        variableRepository.saveAndFlush(variable);

        // Get the variable
        restVariableMockMvc.perform(get("/api/variables/{id}", variable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(variable.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.scaleLevel").value(DEFAULT_SCALE_LEVEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVariable() throws Exception {
        // Get the variable
        restVariableMockMvc.perform(get("/api/variables/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVariable() throws Exception {
        // Initialize the database
        variableRepository.saveAndFlush(variable);

		int databaseSizeBeforeUpdate = variableRepository.findAll().size();

        // Update the variable
        variable.setName(UPDATED_NAME);
        variable.setDataType(UPDATED_DATA_TYPE);
        variable.setLabel(UPDATED_LABEL);
        variable.setQuestion(UPDATED_QUESTION);
        variable.setScaleLevel(UPDATED_SCALE_LEVEL);
        VariableDto variableDto = variableMapper.variableToVariableDto(variable);

        restVariableMockMvc.perform(put("/api/variables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(variableDto)))
                .andExpect(status().isOk());

        // Validate the Variable in the database
        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeUpdate);
        Variable testVariable = variables.get(variables.size() - 1);
        assertThat(testVariable.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVariable.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testVariable.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testVariable.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testVariable.getScaleLevel()).isEqualTo(UPDATED_SCALE_LEVEL);
    }

    @Test
    @Transactional
    public void deleteVariable() throws Exception {
        // Initialize the database
        variableRepository.saveAndFlush(variable);

		int databaseSizeBeforeDelete = variableRepository.findAll().size();

        // Get the variable
        restVariableMockMvc.perform(delete("/api/variables/{id}", variable.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Variable> variables = variableRepository.findAll();
        assertThat(variables).hasSize(databaseSizeBeforeDelete - 1);
    }
}
