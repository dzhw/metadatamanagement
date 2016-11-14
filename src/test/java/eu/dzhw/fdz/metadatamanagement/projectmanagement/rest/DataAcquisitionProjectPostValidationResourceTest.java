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
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestImageHelper;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
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
  private InstrumentRepository instrumentRepository;
  
  @Autowired
  private QuestionRepository questionRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired QuestionImageService questionImageService;

  private MockMvc mockMvc;
  
  private String questionId;

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
    this.instrumentRepository.deleteAll();
    this.questionRepository.deleteAll();
    this.studyRepository.deleteAll();
    if (questionId != null) {
      this.questionImageService.deleteQuestionImage(this.questionId);
      this.questionId = null;
    }
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
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable3.setId("testProject-name3");
    variable3.setName("name3");
    this.variableRepository.save(variable3);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    instrument.setSurveyId(survey.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), instrument.getId(), 
        survey.getId());
    this.questionRepository.save(question);
    this.questionId = question.getId();
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());    
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(survey.getId());
    List<String> instrumentIds = new ArrayList<>();
    instrumentIds.add(instrument.getId());
    List<String> dataSetIds = new ArrayList<>();
    dataSetIds.add(dataSet.getId());
    this.studyRepository.save(study);
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(0)));//no errors 
  }
  
  @Test
  public void testPostValidationQuestionImageIsMissing() throws IOException, Exception {
    
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
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable3.setId("testProject-name3");
    variable3.setName("name3");
    this.variableRepository.save(variable3);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    instrument.setSurveyId(survey.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), instrument.getId(), 
        survey.getId());
    this.questionRepository.save(question);
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());    
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(survey.getId());
    List<String> instrumentIds = new ArrayList<>();
    instrumentIds.add(instrument.getId());
    List<String> dataSetIds = new ArrayList<>();
    dataSetIds.add(dataSet.getId());
    this.studyRepository.save(study);
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(1)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("question-management.error.post-validation.question-has-no-image")));//no errors 
  }
  
  
  @Test
  public void testSimpleProjectForPostValidationWithWrongInformationForQuestion() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Study (each project must have one)
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    this.studyRepository.save(study);
    
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
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable3.setId("testProject-name3");
    variable3.setName("name3");
    this.variableRepository.save(variable3);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(),"testProject-WrongQuestionname1", 
        survey.getId());
    question.getSuccessors().add("testProject-WrongQuestion");
    this.questionRepository.save(question);
    this.questionId = question.getId();
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(2)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.question-has-invalid-instrument-id")))    
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.question-has-invalid-successor")));
  }
  
  @Test
  public void testPostValidationWithMissingSubDataAccessWays() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Study (each project must have one)
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    this.studyRepository.save(study);
    
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
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable3.setId("testProject-name3");
    variable3.setName("name3");
    this.variableRepository.save(variable3);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    dataSet.getSubDataSets().remove(3);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), instrument.getId(), 
        survey.getId());
    this.questionRepository.save(question);
    this.questionId = question.getId();
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(1)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("study-management.error.post-validation.survey-has-an-accessway-which-was-not-found-in-sub-data-sets")));
  }
  
  @Test
  public void testSimpleProjectForPostValidationWithWrongInformationForDataSet() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Study (each project must have one)
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    this.studyRepository.save(study);
    
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
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable3.setId("testProject-name3");
    variable3.setName("name3");
    this.variableRepository.save(variable3);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    List<String> variableIds = new ArrayList<>();
    variableIds.add(project.getId() + "-WrongVariableId");
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(project.getId() + "-WrongSurveyId");
    dataSet.setVariableIds(variableIds);
    dataSet.setSurveyIds(surveyIds);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), instrument.getId(), 
        survey.getId());    
    this.questionRepository.save(question);
    this.questionId = question.getId();
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(2)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.data-set-has-invalid-survey-id")))
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.data-set-has-invalid-variable-id")));    
  }
  
  
  @Test
  public void testSimpleProjectForPostValidationWithCorrectInformationForSurvey() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Study (each project must have one)
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());
    this.studyRepository.save(study);
    
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
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), survey.getId());
    variable3.setId("testProject-name3");
    variable3.setName("name3");
    this.variableRepository.save(variable3);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), instrument.getId(), 
        survey.getId());    
    this.questionRepository.save(question);
    this.questionId = question.getId();
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    
    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(0)));   
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
    variable2.setQuestionId(project.getId()+  "-WrongAtomicQuestionId");
    List<String> dataSetIds = new ArrayList<>();
    dataSetIds.add(project.getId() + "-WrongDataSetId");
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(project.getId() + "-WrongSurveyId");
    variable2.setSurveyIds(surveyIds);
    this.variableRepository.save(variable2);
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId());
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), instrument.getId(), 
        survey.getId());    
    this.questionRepository.save(question);
    this.questionId = question.getId();
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(7)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.data-set-has-invalid-variable-id")))
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.variable-id-is-not-in-invalid-variables-panel")))
      .andExpect(jsonPath("$.errors[2].messageId", containsString("error.post-validation.variable-id-is-not-in-invalid-variables-panel")))
      .andExpect(jsonPath("$.errors[3].messageId", containsString("error.post-validation.variable-has-invalid-survey-id")))
      .andExpect(jsonPath("$.errors[4].messageId", containsString("error.post-validation.variable-id-is-not-in-invalid-variables-panel")))
      .andExpect(jsonPath("$.errors[5].messageId", containsString("error.post-validation.variable-has-invalid-question-id")))
      .andExpect(jsonPath("$.errors[6].messageId", containsString("error.post-validation.project-has-no-study")));
  }
  
}
