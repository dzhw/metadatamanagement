package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonSyntaxException;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Missing;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RuleExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Tests for the variable resource.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
public class VariableResourceControllerTest extends AbstractTest {
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
  private ElasticsearchAdminService elasticsearchAdminService;
  
  @Autowired
  private ElasticsearchUpdateQueueService queueService;
  
  @Autowired
  private JaversService javersService;
  
  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
      .build();
  }

  @After
  public void cleanUp() {
    dataAcquisitionProjectRepository.deleteAll();
    surveyRepository.deleteAll();
    variableRepository.deleteAll();
    queueService.clearQueue();
    elasticsearchAdminService.recreateAllIndices();
    javersService.deleteAll();
  }

  @Test
  public void testCreateVariable() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
   
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    queueService.processAllQueueItems();
    
    // check that there is one variable search document
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));

    // check that auditing attributes have been set
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.createdBy", is("user")))
      .andExpect(jsonPath("$.lastModifiedBy", is("user")));

    // call toString for test coverage :-)
    variable.toString();
  }

  @Test
  public void testCreateVariableWithPost() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);

    // create the variable with the given id
    mockMvc.perform(post(API_VARIABLES_URI).content(TestUtil.convertObjectToJsonBytes(variable))
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

    queueService.processAllQueueItems();

    // check that there is one variable search document
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(1L));

    // check that auditing attributes have been set
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId())).andExpect(status().isOk())
        .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
        .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
        .andExpect(jsonPath("$.createdBy", is("user")))
        .andExpect(jsonPath("$.lastModifiedBy", is("user")));
  }

  @Test
  public void testCreateVariableWithSurveyButWithoutProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(null, 1, "var1", 1, surveyNumbers);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithoutScaleLevel() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setScaleLevel(null);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateVariableWithWrongPanelIdentifier() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setPanelIdentifier("WrongPanelIdentifier");

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateVariableWithWrongDerivedVariablesIdentifier() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDerivedVariablesIdentifier("WrongDerivedVariablesIdentifier");

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateDateVariableWithOrdinalScaleLevel() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDataType(DataTypes.DATE);
    variable.setScaleLevel(ScaleLevels.ORDINAL);
    List<ValidResponse> validResponses = new ArrayList<>();    
    variable.getDistribution().setValidResponses(validResponses);
    variable.getDistribution().getStatistics().setMaximum("2017-01-31");
    variable.getDistribution().getStatistics().setMinimum("2017-01-30");
    variable.getDistribution().getStatistics().setMedian("2017-01-30");
    variable.getDistribution().getStatistics().setThirdQuartile("2017-01-31");
    variable.getDistribution().getStatistics().setFirstQuartile("2017-01-30");

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
  }
  
  @Test
  public void testCreateDateVariableWithNominalScaleLevel() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDataType(DataTypes.DATE);
    variable.setScaleLevel(ScaleLevels.NOMINAL);
    List<ValidResponse> validResponses = new ArrayList<>();    
    variable.getDistribution().setValidResponses(validResponses);
    variable.getDistribution().getStatistics().setMaximum("2017-01-31");
    variable.getDistribution().getStatistics().setMinimum("2017-01-30");
    variable.getDistribution().getStatistics().setMedian("2017-01-30");
    variable.getDistribution().getStatistics().setThirdQuartile("2017-01-31");
    variable.getDistribution().getStatistics().setFirstQuartile("2017-01-30");

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("variable-management.error.variable.only-ordinal-scale-level-for-date-data-type")));
  }
  
  @Test
  public void testCreateDateVariableWithContinousScaleLevel() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDataType(DataTypes.DATE);
    variable.setScaleLevel(ScaleLevels.INTERVAL);
    List<ValidResponse> validResponses = new ArrayList<>();    
    variable.getDistribution().setValidResponses(validResponses);
    variable.getDistribution().getStatistics().setMaximum("2017-01-31");
    variable.getDistribution().getStatistics().setMinimum("2017-01-30");
    variable.getDistribution().getStatistics().setMedian("2017-01-30");
    variable.getDistribution().getStatistics().setThirdQuartile("2017-01-31");
    variable.getDistribution().getStatistics().setFirstQuartile("2017-01-30");

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("variable-management.error.variable.only-ordinal-scale-level-for-date-data-type")));
  }
  
  @Test
  public void testCreateVariableWithoutLabel() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setLabel(null);

    // create the variable with a null label
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
    
    variable.setLabel(new I18nString());
    
    // create the variable with an empty label
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("variable-management.error.variable.label.i18n-string-not-empty")));
  }

  @Test
  public void testGenerationDetailsWithExpressionLanguageButWithoutRule() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.getGenerationDetails()
      .setRule(null);

    // create the variable with a null label
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("variable-management.error.generation-details.rule-expression-language-and-rule-filled-or-empty")));
  }

  @Test
  public void testCreateVariableWithNonUniqueCode() throws Exception {

    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    Missing value1 = variable.getDistribution()
      .getMissings()
      .get(0);

    Missing value2 = Missing.builder().code(value1.getCode())
      .label(value1.getLabel())
      .absoluteFrequency(value1.getAbsoluteFrequency())
      .relativeFrequency(value1.getRelativeFrequency())
      .build();

    variable.getDistribution()
      .getMissings()
      .add(value2);

    // create the variable with duplicate value codes
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("unique")));

  }
  
  @Test
  public void testCreateVariableWithNonUniqueValueClass() throws Exception {
    
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
      
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    ValidResponse validResponse = variable.getDistribution()
      .getValidResponses()
      .get(0);
    validResponse.setValue("123.456");

    ValidResponse value2 = ValidResponse.builder().value(validResponse.getValue())
      .label(validResponse.getLabel())
      .absoluteFrequency(validResponse.getAbsoluteFrequency())
      .relativeFrequency(validResponse.getRelativeFrequency())
      .validRelativeFrequency(validResponse.getValidRelativeFrequency())
      .build();

    variable.getDistribution()
      .getValidResponses()
      .add(value2);
      
    // create the variable with duplicate value classes
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("unique")));
     
  }

  @Test
  public void testCreateVariableWithNonNumericValueOnContinouosScaleLevel() throws Exception {

    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    ValidResponse validResponse = variable.getDistribution()
      .getValidResponses()
      .get(0);
    validResponse.setValue("hurz");

    // create the variable with duplicate value classes
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError())
      .andReturn();
  }

  @Test
  public void testCreateVariableWithInvalidScaleLevel() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setScaleLevel(I18nString.builder().de("InvalidValue")
      .en("InvalidValue")
      .build());

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithoutDataType() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDataType(null);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithInvalidDataType() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDataType(I18nString.builder().de("InvalidValue")
      .en("InvalidValue")
      .build());

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithoutAccessWays() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setAccessWays(null);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithInvalidAccessWays() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<String> accessWays = new ArrayList<>();
    accessWays.add("WrongAccessWay");
    
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setAccessWays(accessWays);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithNoMinusInId() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    // No Minus!
    variable.setId(project.getId() + variable.getName());

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithInvalidId() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var", 1, surveyNumbers);
    // 123 at the end is too much
    variable.setId(project.getId() + "-" + variable.getName() + "123");

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }


  @Test
  public void testCreateVariableWithDuplicateNameWithinProject() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var", 1, surveyNumbers);

    // create the first variable
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable2.setId(project.getId() + "-AnotherName");
    variable2.setName(variable.getName());

    // create the second variable with the same name
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable2.getId())// push to variables 1 field
      .content(TestUtil.convertObjectToJsonBytes(variable2)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void deleteVariable() throws JsonSyntaxException, IOException, Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    // change scale level (code coverage)
    variable.setScaleLevel(I18nString.builder().de(ScaleLevels.ORDINAL.getDe())
      .en(ScaleLevels.ORDINAL.getEn())
      .build());

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    // delete the variable
    mockMvc.perform(delete(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the variable has been deleted
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isNotFound());

    // check that there are no variable search documents anymore
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0L));
  }

  @Test
  public void testUpdateVariable() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "bdem08_v1", 1, surveyNumbers);

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    variable.setLabel(I18nString.builder().de("modified")
      .en("modified")
      .build());
    // with other scale level and data type (code coverage)
    variable.setScaleLevel(I18nString.builder().de(ScaleLevels.NOMINAL.getDe())
      .en(ScaleLevels.NOMINAL.getEn())
      .build());

    variable.setVersion(0L);
    // update the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().is2xxSuccessful());

    // read the updated variable and check the version
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(variable.getId())))
      .andExpect(jsonPath("$.version", is(1)))
      .andExpect(jsonPath("$.label.de", is("modified")));

    queueService.processAllQueueItems();
    
    // check that the variable search documents have been updated
    assertThat(elasticsearchAdminService.countAllDocuments(), is(1L));    
  }

  @Test
  public void testFilterExpressionLanguageIsEmpty() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.getFilterDetails()
      .setExpressionLanguage(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFilterExpressionLanguageWithWrongLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.getFilterDetails()
      .setExpressionLanguage("WrongLanguage");

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFilterExpressionLanguageWithSpelLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.getFilterDetails()
      .setExpressionLanguage(FilterExpressionLanguages.SPEL);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
  }

  @Test
  public void testRuleExpressionLanguageIsEmpty() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.getGenerationDetails()
      .setRuleExpressionLanguage(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testRuleExpressionLanguageWithWrongLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.getGenerationDetails()
      .setRuleExpressionLanguage("WrongLanguage");

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testRuleExpressionLanguageWithStataLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.getGenerationDetails()
      .setRuleExpressionLanguage(RuleExpressionLanguages.STATA);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeletingProjectDeletesVariable() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated());

    mockMvc.perform(delete("/api/data-acquisition-projects/" + project.getId()))
      .andExpect(status().is2xxSuccessful());

    // check that the variable has been deleted
    mockMvc.perform(get(API_VARIABLES_URI + "/" + variable.getId()))
      .andExpect(status().isNotFound());

    // check that there are no variable search documents anymore
    assertThat(elasticsearchAdminService.countAllDocuments(), equalTo(0L));
  }

  @Test  
  public void testVariableWithDataTypeDateWithError() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDataType(DataTypes.DATE);

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void testVariableWithDataTypeDate() throws Exception {
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "var1", 1, surveyNumbers);
    variable.setDataType(DataTypes.DATE);
    variable.getDistribution()
      .setValidResponses(null);
    List<ValidResponse> validResponses = new ArrayList<>();
    validResponses
      .add(ValidResponse.builder().label(I18nString.builder().de("Deutsches Label")
        .en("English Label")
        .build())
        .absoluteFrequency(1234)
        .relativeFrequency(87.5)
        .validRelativeFrequency(88.9)
        .value(LocalDateTime.now()
          .toString())
        .build());

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testCreateVariableShadowCopy() throws Exception {
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable("issue1991", 1, "varname", 1, Collections.singletonList(1));
    variable.setId(variable.getId() + "-1.0.0");

    mockMvc.perform(post(API_VARIABLES_URI)
        .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-create-not-allowed")));
  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testUpdateVariableShadowCopy() throws Exception {
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable("issue1991", 1, "varname", 1, Collections.singletonList(1));
    variable.setId(variable.getId() + "-1.0.0");
    variable = variableRepository.save(variable);

    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
        .content(TestUtil.convertObjectToJsonBytes(variable)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-save-not-allowed")));

  }

  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testDeleteInstrumentShadowCopy() throws Exception {
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable("issue1991", 1, "varname", 1, Collections.singletonList(1));
    variable.setId(variable.getId() + "-1.0.0");
    variableRepository.save(variable);

    mockMvc.perform(delete(API_VARIABLES_URI + "/" + variable.getId()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].message", containsString("global.error.shadow-delete-not-allowed")));
  }
}
