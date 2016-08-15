/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagementold.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.questionmanagementold.repository.AtomicQuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionnairemanagement.domain.Questionnaire;
import eu.dzhw.fdz.metadatamanagement.questionnairemanagement.repository.QuestionnaireRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class DataAcquisitionProjectPostValidationResourceTest extends AbstractTest{
  private static final String PROJECT_NAME = "testProject";
  private static final String API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI = "/api/data-acquisition-projects/" + PROJECT_NAME + "/post-validate";
  
  
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private DataAcquisitionProjectRepository rdcProjectRepository;
  
  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  @Autowired
  private QuestionnaireRepository questionnaireRepository;
  
  @Autowired
  private AtomicQuestionRepository atomicQuestionRepository;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
      .build();
  }

  @After
  public void cleanUp() {
    this.rdcProjectRepository.deleteAll();
    this.surveyRepository.deleteAll();
    this.variableRepository.deleteAll();
    this.dataSetRepository.deleteAll();
    this.questionnaireRepository.deleteAll();
    this.atomicQuestionRepository.deleteAll();
  }
  
  @Test
  public void testSimpleProjectForPostValidation() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    
    //Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable1.setId("testProject-name1");
    variable1.setName("name1");
    this.variableRepository.save(variable1);    
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable2.setId("testProject-name2");
    variable2.setName("name2");
    this.variableRepository.save(variable2);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Questionnaire
    Questionnaire questionnaire = UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);
    
    //Atomic Question
    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils.buildAtomicQuestion(project.getId(), questionnaire.getId(), variable1.getName());
    this.atomicQuestionRepository.save(atomicQuestion);
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(0)));//no errors
  }
  
  @Test
  public void testSimpleProjectForPostValidationWithWrongInformationForAtomicQuestion() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    
    //Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable1.setId("testProject-name1");
    variable1.setName("name1");
    this.variableRepository.save(variable1);    
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable2.setId("testProject-name2");
    variable2.setName("name2");
    this.variableRepository.save(variable2);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Questionnaire
    Questionnaire questionnaire = UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);
    
    //Atomic Question
    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils.buildAtomicQuestion(project.getId(), questionnaire.getId(), variable1.getName());
    atomicQuestion.setVariableId("testProject-Wrongname1");
    atomicQuestion.setQuestionnaireId("testProject-WrongQuestionname1");
    atomicQuestion.setId("testProject-Wrongname1");
    this.atomicQuestionRepository.save(atomicQuestion);
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(2)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.question-has-invalid-variable-id")))
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.question-has-invalid-questionnaire-id")));    
  }
  
  @Test
  public void testSimpleProjectForPostValidationWithWrongInformationForDataSet() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    
    //Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable1.setId("testProject-name1");
    variable1.setName("name1");
    this.variableRepository.save(variable1);    
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable2.setId("testProject-name2");
    variable2.setName("name2");
    this.variableRepository.save(variable2);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    List<String> variableIds = new ArrayList<>();
    variableIds.add(project.getId() + "-WrongVariableId");
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(project.getId() + "-WrongSurveyId");
    dataSet.setVariableIds(variableIds);
    dataSet.setSurveyIds(surveyIds);
    this.dataSetRepository.save(dataSet);
    
    //Questionnaire
    Questionnaire questionnaire = UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);
    
    //Atomic Question
    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils.buildAtomicQuestion(project.getId(), questionnaire.getId(), variable1.getName());    
    this.atomicQuestionRepository.save(atomicQuestion);
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(2)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.dataSetHasInvalidSurveyId")))
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.dataSetHasInvalidVariableId")));    
  }
  
  
  @Test
  public void testSimpleProjectForPostValidationWithWrongInformationForSurvey() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    survey.setQuestionnaireId(project.getId() + "-WrongQuestionnaireId");
    List<String> dataSetIds = new ArrayList<>();
    dataSetIds.add(project.getId() + "-WrongDataSetId");
    survey.setDataSetIds(dataSetIds);
    this.surveyRepository.save(survey);
    
    //Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable1.setId("testProject-name1");
    variable1.setName("name1");
    this.variableRepository.save(variable1);    
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable2.setId("testProject-name2");
    variable2.setName("name2");
    this.variableRepository.save(variable2);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Questionnaire
    Questionnaire questionnaire = UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);
    
    //Atomic Question
    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils.buildAtomicQuestion(project.getId(), questionnaire.getId(), variable1.getName());    
    this.atomicQuestionRepository.save(atomicQuestion);
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(2)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.surveyHasInvalidQuestionnaireId")))
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.surveyHasInvalidDataSetId")));    
  }
  
  @Test
  public void testSimpleProjectForPostValidationWithWrongInformationForVariable() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    
    //Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable1.setId("testProject-name1");
    variable1.setName("name1");
    variable1.getSameVariablesInPanel().add("testProject-name123");
    this.variableRepository.save(variable1);    
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable2.setId("testProject-name3");
    variable2.setName("name3");
    variable2.setAtomicQuestionId(project.getId()+  "-WrongAtomicQuestionId");
    List<String> dataSetIds = new ArrayList<>();
    dataSetIds.add(project.getId() + "-WrongDataSetId");
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(project.getId() + "-WrongSurveyId");
    variable2.setDataSetIds(dataSetIds);
    variable2.setSurveyIds(surveyIds);
    this.variableRepository.save(variable2);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Questionnaire
    Questionnaire questionnaire = UnitTestCreateDomainObjectUtils.buildQuestionnaire(project.getId());
    this.questionnaireRepository.save(questionnaire);
    
    //Atomic Question
    AtomicQuestion atomicQuestion = UnitTestCreateDomainObjectUtils.buildAtomicQuestion(project.getId(), questionnaire.getId(), variable1.getName());    
    this.atomicQuestionRepository.save(atomicQuestion);
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(7)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.dataSetHasInvalidVariableId")))
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.variableIdIsNotInInvalidVariablesPanel")))
      .andExpect(jsonPath("$.errors[2].messageId", containsString("error.post-validation.variableIdIsNotInInvalidVariablesPanel")))
      .andExpect(jsonPath("$.errors[3].messageId", containsString("error.post-validation.variableHasInvalidSurveyId")))
      .andExpect(jsonPath("$.errors[4].messageId", containsString("error.post-validation.variableHasInvalidDataSetId")))
      .andExpect(jsonPath("$.errors[5].messageId", containsString("error.post-validation.variableIdIsNotInInvalidVariablesPanel")))
      .andExpect(jsonPath("$.errors[6].messageId", containsString("error.post-validation.variableHasInvalidAtomicQuestionId")));
  }
  
}
