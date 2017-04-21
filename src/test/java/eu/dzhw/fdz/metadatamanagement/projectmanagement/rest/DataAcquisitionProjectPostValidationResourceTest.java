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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateValidIds;
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
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
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
  
  @Autowired 
  private QuestionImageService questionImageService;

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
    this.instrumentRepository.deleteAll();
    this.questionRepository.deleteAll();
    this.studyRepository.deleteAll();
    this.questionImageService.deleteAll();
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testSimpleProjectForPostValidation() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    dataSet.setSurveyNumbers(surveyNumbers);
    dataSet.setSurveyIds(listOfSurveyIds);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    instrument.setSurveyNumbers(surveyNumbers);
    instrument.setSurveyIds(listOfSurveyIds);
    this.instrumentRepository.save(instrument);
    
    //Variables
    Variable variable1 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1", 1, surveyNumbers);
    this.variableRepository.save(variable1);    
    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2", 2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3", 3, surveyNumbers);
    this.variableRepository.save(variable3);    
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 1, instrument.getId(), 
        survey.getId());
    this.questionRepository.save(question);
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    
    
    Study study = UnitTestCreateDomainObjectUtils.buildStudy(project.getId());    
    this.studyRepository.save(study);

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(0)));//no errors
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testPostValidationQuestionImageIsMissing() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());
    
    //Variables
    Variable variable1 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1", 1, surveyNumbers);
    this.variableRepository.save(variable1);    
    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2", 2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3", 3, surveyNumbers);
    this.variableRepository.save(variable3);    
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    instrument.setSurveyIds(listOfSurveyIds);
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, instrument.getId(), 
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
      .andExpect(jsonPath("$.errors[0].messageId", containsString("question-management.error.post-validation.question-has-no-image")));
      
  }
  
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
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
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());
    
    //Variables
    Variable variable1 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1", 1, surveyNumbers);
    this.variableRepository.save(variable1);    
    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2", 2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3", 3, surveyNumbers);
    this.variableRepository.save(variable3);    
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, "testProject-WrongQuestionname1", 
        survey.getId());
    question.getSuccessors().add("testProject-WrongQuestion");
    this.questionRepository.save(question);
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(2)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.question-has-invalid-instrument-id")))    
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.question-has-invalid-successor")));
  }
    
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
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
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());
    
    //Variables
    Variable variable1 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1", 1, surveyNumbers);
    this.variableRepository.save(variable1);    
    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2", 2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3", 3, surveyNumbers);
    this.variableRepository.save(variable3);    
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(project.getId() + "-WrongSurveyId");
    dataSet.setSurveyIds(surveyIds);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, instrument.getId(), 
        survey.getId());    
    this.questionRepository.save(question);
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(1)))
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.data-set-has-invalid-survey-id")));    
  }
  
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
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
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());
    
    //Variables
    Variable variable1 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1", 1, surveyNumbers);
    this.variableRepository.save(variable1);    
    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2", 2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3", 3, surveyNumbers);
    this.variableRepository.save(variable3);    
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, instrument.getId(), 
        survey.getId());    
    this.questionRepository.save(question);
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    
    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(0)));   
  }
  
  @Test
  @WithMockUser(authorities=AuthoritiesConstants.PUBLISHER)
  public void testSimpleProjectForPostValidationWithWrongInformationForVariable() throws IOException, Exception {
    
    //Arrange
    //Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();    
    this.rdcProjectRepository.save(project);
    
    //Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(UnitTestCreateValidIds.buildSurveyId(project.getId(), 1));
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());
    
    //Variables
    Variable variable1 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1", 1, surveyNumbers);
    this.variableRepository.save(variable1);    
    Variable variable2 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2", 2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 =
        UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3", 3, surveyNumbers);
    surveyNumbers.add(4);
    surveyIds.add(UnitTestCreateValidIds.buildSurveyId(project.getId(), 4));
    variable3.setSurveyNumbers(surveyNumbers);
    variable3.setSurveyIds(surveyIds);
    this.variableRepository.save(variable3);    
    
    //DataSet
    DataSet dataSet = UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    this.dataSetRepository.save(dataSet);
    
    //Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);
    
    //Atomic Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, instrument.getId(), 
        survey.getId());    
    this.questionRepository.save(question);
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, question.getId());
    
    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.errors", hasSize(2)))      
      .andExpect(jsonPath("$.errors[0].messageId", containsString("error.post-validation.project-has-no-study")))
      .andExpect(jsonPath("$.errors[1].messageId", containsString("error.post-validation.variable-has-invalid-survey-id")));
    }
  
}
