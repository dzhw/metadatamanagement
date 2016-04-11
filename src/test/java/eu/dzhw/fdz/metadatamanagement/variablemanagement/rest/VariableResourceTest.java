package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
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
import java.util.ArrayList;
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
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.builders.I18nStringBuilder;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.builders.DataAcquisitionProjectBuilder;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchAdminService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RuleExpressionLanguages;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Value;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders.ValueBuilder;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.search.VariableSearchDao;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.search.document.VariableSearchDocument;

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

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());

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
      .andExpect(jsonPath("$.createdDate", not(isEmptyOrNullString())))
      .andExpect(jsonPath("$.lastModifiedDate", not(isEmptyOrNullString())))
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

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable = UnitTestCreateDomainObjectUtils.buildVariable(null, survey.getId());

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithoutScaleLevel() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.setScaleLevel(null);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateVariableWithoutLabel() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.setLabel(null);

    // create the variable with a null label
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
    
    variable.setLabel(new I18nString());
    
    // create the variable with an empty label
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("At least an english")));
  }

  @Test
  public void testCreateVariableWithNonUniqueCode() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    Value value1 = variable.getValues()
      .get(0);

    Value value2 = new ValueBuilder().withCode(value1.getCode())
      .withLabel(value1.getLabel())
      .withAbsoluteFrequency(value1.getAbsoluteFrequency())
      .withRelativeFrequency(value1.getRelativeFrequency())
      .withValidRelativeFrequency(value1.getValidRelativeFrequency())
      .build();

    variable.getValues()
      .add(value2);

    // create the variable with duplicate value codes
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("value codes must be unique")));
  }
  
  @Test
  public void testCreateVariableWithNonUniqueValueClass() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    Value value1 = variable.getValues()
      .get(0);
    value1.setCode(null);
    value1.setValueClass("hurz");

    Value value2 = new ValueBuilder().withValueClass(value1.getValueClass())
      .withLabel(value1.getLabel())
      .withAbsoluteFrequency(value1.getAbsoluteFrequency())
      .withRelativeFrequency(value1.getRelativeFrequency())
      .withValidRelativeFrequency(value1.getValidRelativeFrequency())
      .build();

    variable.getValues()
      .add(value2);

    // create the variable with duplicate value classes
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors[0].message", containsString("value classes must be unique")));
  }

  @Test
  public void testCreateVariableWithInvalidScaleLevel() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.setScaleLevel(new I18nStringBuilder().withDe("InvalidValue")
      .withEn("InvalidValue")
      .build());

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithoutDataType() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.setDataType(null);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithInvalidDataType() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.setDataType(new I18nStringBuilder().withDe("InvalidValue")
      .withEn("InvalidValue")
      .build());

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithoutAccessWays() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.setAccessWays(null);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithInvalidAccessWays() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    List<String> accessWays = new ArrayList<String>();
    accessWays.add("WrongAccessWay");
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.setAccessWays(accessWays);

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithNoMinusInId() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    // No Minus!
    variable.setId(project.getId() + variable.getName());

    // create the variable with a survey but without a project
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateVariableWithInvalidId() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    // 123 at the end is too much
    variable.setId(project.getId() + "-" + variable.getName() + "123");

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

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());

    // create the first variable
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable2.setId(project.getId() + "-AnotherName");
    variable2.setName(variable.getName());

    // create the second variable with the same name
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable2.getId())// push to variables 1 field
      .content(TestUtil.convertObjectToJsonBytes(variable2)))
      .andExpect(status().is4xxClientError());
  }

  @Test
  public void deleteVariable() throws JsonSyntaxException, IOException, Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    // change scale level (code coverage)
    variable.setScaleLevel(new I18nStringBuilder().withDe(ScaleLevels.ORDINAL.getDe())
      .withEn(ScaleLevels.ORDINAL.getEn())
      .build());

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

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());

    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());

    variable.setLabel(new I18nStringBuilder().withDe("modified")
      .withEn("modified")
      .build());
    // with other scale level and data type (code coverage)
    variable.setScaleLevel(new I18nStringBuilder().withDe(ScaleLevels.NOMINAL.getDe())
      .withEn(ScaleLevels.NOMINAL.getEn())
      .build());
    variable.setDataType(new I18nStringBuilder().withDe(DataTypes.STRING.getDe())
      .withEn(DataTypes.STRING.getEn())
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

  @Test
  public void testValueClassAndCodeAreFilled() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    Value value = variable.getValues()
      .get(0);
    value.setCode(1234);
    value.setValueClass("A Value Class");
    variable.getValues()
      .set(0, value);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testValueClassAndCodeAreEmpty() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    Value value = variable.getValues()
      .get(0);
    value.setCode(null);
    value.setValueClass("");
    variable.getValues()
      .set(0, value);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFilterExpressionLanguageIsEmpty() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.getFilterDetails()
      .setFilterExpressionLanguage(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFilterExpressionLanguageWithWrongLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.getFilterDetails()
      .setFilterExpressionLanguage("WrongLanguage");

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testFilterExpressionLanguageWithSpelLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.getFilterDetails()
      .setFilterExpressionLanguage(FilterExpressionLanguages.SPEL);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());
  }

  @Test
  public void testRuleExpressionLanguageIsEmpty() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.getGenerationDetails()
      .setRuleExpressionLanguage(null);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testRuleExpressionLanguageWithWrongLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.getGenerationDetails()
      .setRuleExpressionLanguage("WrongLanguage");

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void testRuleExpressionLanguageWithStataLanguage() throws Exception {
    // Arrange
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);
    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable.getGenerationDetails()
      .setRuleExpressionLanguage(RuleExpressionLanguages.STATA);

    // Act and Assert
    // create the variable with the given id
    mockMvc.perform(put(API_VARIABLES_URI + "/" + variable.getId())
      .content(TestUtil.convertObjectToJsonBytes(variable)))
      .andExpect(status().isCreated());
  }

  @Test
  public void testDeletingProjectDeletesVariable() throws Exception {
    DataAcquisitionProject project = new DataAcquisitionProjectBuilder().withId("testProject")
      .withSurveySeries(new I18nStringBuilder().build())
      .withPanelName(new I18nStringBuilder().build())
      .build();
    dataAcquisitionProjectRepository.save(project);

    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    surveyRepository.save(survey);

    Variable variable =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());

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
