/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.service.JaversService;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateValidIds;
import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestImageHelper;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectState;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Requirements;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionImageMetadata;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * @author Daniel Katzberg
 *
 */
public class DataAcquisitionProjectPostValidationResourceTest extends AbstractTest {
  private static final String PROJECT_NAME = "testproject";
  private static final String API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI =
      "/api/data-acquisition-projects/" + PROJECT_NAME + "/post-validate";


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
  private DataPackageRepository dataPackageRepository;

  @Autowired
  private QuestionImageService questionImageService;

  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Autowired
  private JaversService javersService;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @AfterEach
  public void cleanUp() {
    this.rdcProjectRepository.deleteAll();
    this.surveyRepository.deleteAll();
    this.variableRepository.deleteAll();
    this.dataSetRepository.deleteAll();
    this.instrumentRepository.deleteAll();
    this.questionRepository.deleteAll();
    this.dataPackageRepository.deleteAll();
    this.analysisPackageRepository.deleteAll();
    this.questionImageService.deleteAll();
    this.javersService.deleteAll();
    this.relatedPublicationRepository.deleteAll();
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testSimpleProjectForPostValidation() throws IOException, Exception {
    buildValidProject();

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(0)));// no errors
  }

  private DataAcquisitionProject buildValidProject() {
    // Arrange
    // Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setDataPackagesState(new ProjectState(true, true));
    this.rdcProjectRepository.save(project);

    // Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());

    // DataSet
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    dataSet.setSurveyNumbers(surveyNumbers);
    dataSet.setSurveyIds(listOfSurveyIds);
    this.dataSetRepository.save(dataSet);

    // Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    instrument.setSurveyNumbers(surveyNumbers);
    instrument.setSurveyIds(listOfSurveyIds);
    this.instrumentRepository.save(instrument);

    // Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1",
        1, surveyNumbers);
    this.variableRepository.save(variable1);
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2",
        2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3",
        3, surveyNumbers);
    this.variableRepository.save(variable3);

    // Atomic Question
    Question question =
        UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 1, instrument.getId());
    this.questionRepository.save(question);
    QuestionImageMetadata questionImageMetadata = UnitTestCreateDomainObjectUtils
        .buildQuestionImageMetadata(project.getId(), question.getId());
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, questionImageMetadata);


    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    this.dataPackageRepository.save(dataPackage);

    return project;
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testSimpleProjectForPostValidationWithWrongInformationForQuestion()
      throws IOException, Exception {

    // Arrange
    // Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setDataPackagesState(new ProjectState(true, true));
    this.rdcProjectRepository.save(project);

    // DataPackage (each project must have one)
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    this.dataPackageRepository.save(dataPackage);

    // Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());

    // Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1",
        1, surveyNumbers);
    this.variableRepository.save(variable1);
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2",
        2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3",
        3, surveyNumbers);
    this.variableRepository.save(variable3);

    // DataSet
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    this.dataSetRepository.save(dataSet);

    // Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);

    // Question
    Question question = UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123,
        "testProject-WrongQuestionname1");
    question.getSuccessors().add("testProject-WrongQuestion");
    this.questionRepository.save(question);
    QuestionImageMetadata questionImageMetadata = UnitTestCreateDomainObjectUtils
        .buildQuestionImageMetadata(project.getId(), question.getId());
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, questionImageMetadata);


    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(2)))
        .andExpect(jsonPath("$.errors[0].messageId",
            containsString("error.post-validation.question-has-invalid-instrument-id")))
        .andExpect(jsonPath("$.errors[1].messageId",
            containsString("error.post-validation.question-has-invalid-successor")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testSimpleProjectForPostValidationWithWrongInformationForDataSet()
      throws IOException, Exception {

    // Arrange
    // Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setDataPackagesState(new ProjectState(true, true));
    this.rdcProjectRepository.save(project);

    // DataPackage (each project must have one)
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    this.dataPackageRepository.save(dataPackage);

    // Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());

    // Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1",
        1, surveyNumbers);
    this.variableRepository.save(variable1);
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2",
        2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3",
        3, surveyNumbers);
    this.variableRepository.save(variable3);

    // DataSet
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(project.getId() + "-WrongSurveyId");
    dataSet.setSurveyIds(surveyIds);
    this.dataSetRepository.save(dataSet);

    // Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);

    // Atomic Question
    Question question =
        UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, instrument.getId());
    this.questionRepository.save(question);
    QuestionImageMetadata questionImageMetadata = UnitTestCreateDomainObjectUtils
        .buildQuestionImageMetadata(project.getId(), question.getId());
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, questionImageMetadata);


    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(4)))
        .andExpect(jsonPath("$.errors[0].messageId",
            containsString("error.post-validation.data-set-has-invalid-survey-id")))
        .andExpect(jsonPath("$.errors[1].messageId", containsString(
            "error.post-validation.variable-survey-ids-are-not-consistent-with-data-set")));
  }


  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testSimpleProjectForPostValidationWithCorrectInformationForSurvey()
      throws IOException, Exception {

    // Arrange
    // Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setDataPackagesState(new ProjectState(true, true));
    this.rdcProjectRepository.save(project);

    // DataPackage (each project must have one)
    DataPackage dataPackage = UnitTestCreateDomainObjectUtils.buildDataPackage(project.getId());
    this.dataPackageRepository.save(dataPackage);

    // Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());

    // Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1",
        1, surveyNumbers);
    this.variableRepository.save(variable1);
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2",
        2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3",
        3, surveyNumbers);
    this.variableRepository.save(variable3);

    // DataSet
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    this.dataSetRepository.save(dataSet);

    // Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);

    // Atomic Question
    Question question =
        UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, instrument.getId());
    this.questionRepository.save(question);
    QuestionImageMetadata questionImageMetadata = UnitTestCreateDomainObjectUtils
        .buildQuestionImageMetadata(project.getId(), question.getId());
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, questionImageMetadata);

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testSimpleProjectForPostValidationWithWrongInformationForVariable()
      throws IOException, Exception {

    // Arrange
    // Project
    DataAcquisitionProject project = UnitTestCreateDomainObjectUtils.buildDataAcquisitionProject();
    project.getConfiguration().setDataPackagesState(new ProjectState(true, true));
    this.rdcProjectRepository.save(project);

    // Survey
    Survey survey = UnitTestCreateDomainObjectUtils.buildSurvey(project.getId());
    this.surveyRepository.save(survey);
    List<Integer> surveyNumbers = new ArrayList<>();
    surveyNumbers.add(1);
    List<String> surveyIds = new ArrayList<>();
    surveyIds.add(UnitTestCreateValidIds.buildSurveyId(project.getId(), 1));
    List<String> listOfSurveyIds = new ArrayList<>();
    listOfSurveyIds.add(survey.getId());

    // Variables
    Variable variable1 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name1",
        1, surveyNumbers);
    this.variableRepository.save(variable1);
    Variable variable2 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name2",
        2, surveyNumbers);
    this.variableRepository.save(variable2);
    Variable variable3 = UnitTestCreateDomainObjectUtils.buildVariable(project.getId(), 1, "name3",
        3, surveyNumbers);
    surveyNumbers.add(4);
    surveyIds.add(UnitTestCreateValidIds.buildSurveyId(project.getId(), 4));
    variable3.setSurveyNumbers(surveyNumbers);
    variable3.setSurveyIds(surveyIds);
    this.variableRepository.save(variable3);

    // DataSet
    DataSet dataSet =
        UnitTestCreateDomainObjectUtils.buildDataSet(project.getId(), survey.getId(), 1);
    dataSet.setSurveyIds(surveyIds);
    this.dataSetRepository.save(dataSet);

    // Instrument
    Instrument instrument = UnitTestCreateDomainObjectUtils.buildInstrument(project.getId());
    this.instrumentRepository.save(instrument);

    // Atomic Question
    Question question =
        UnitTestCreateDomainObjectUtils.buildQuestion(project.getId(), 123, instrument.getId());
    this.questionRepository.save(question);
    QuestionImageMetadata questionImageMetadata = UnitTestCreateDomainObjectUtils
        .buildQuestionImageMetadata(project.getId(), question.getId());
    UnitTestImageHelper.saveQuestionImage(this.questionImageService, questionImageMetadata);

    // Act & Assert
    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(3)))
        .andExpect(jsonPath("$.errors[0].messageId",
            containsString("error.post-validation.project-has-no-dataPackage")))
        .andExpect(jsonPath("$.errors[1].messageId",
            containsString("error.post-validation.data-set-has-invalid-survey-id")))
        .andExpect(jsonPath("$.errors[2].messageId",
            containsString("error.post-validation.variable-has-invalid-survey-id")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void testProjectRequirements() throws Exception {
    DataAcquisitionProject dataAcquisitionProject = buildValidProject();
    applyNotReadyState(dataAcquisitionProject);
    rdcProjectRepository.save(dataAcquisitionProject);

    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].messageId",
            equalTo("data-acquisition"
                + "-project-management.error.post-validation.requirements-not-met")))
        .andExpect(jsonPath("$.errors[0].messageParameter", hasSize(6)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.DATA_PROVIDER)
  public void testProjectRequirements_publishers_only() throws Exception {
    DataAcquisitionProject dataAcquisitionProject = buildValidProject();
    // dataPackage is mandatory so this would produce an error if a publisher validates a project
    dataAcquisitionProject.getConfiguration().setDataPackagesState(new ProjectState(false, false));
    rdcProjectRepository.save(dataAcquisitionProject);

    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(0)));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldFailToPostvalidateProjectWithoutAnalysisPackage() throws Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    project.getConfiguration().setAnalysisPackagesState(new ProjectState(true, true));
    project.getConfiguration().setPublicationsState(new ProjectState(true, true));
    rdcProjectRepository.save(project);

    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].messageId", equalTo("data-acquisition"
            + "-project-management.error.post-validation.project-has-no-analysisPackage")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldFailToPostvalidateProjectWithoutPublication() throws Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    project.getConfiguration().setAnalysisPackagesState(new ProjectState(true, true));
    project.getConfiguration().setPublicationsState(new ProjectState(true, true));
    rdcProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    analysisPackageRepository.save(analysisPackage);

    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI + "?version=1.0.0"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].messageId", equalTo("data-acquisition"
            + "-project-management.error.post-validation."
            + "project-must-have-exactly-one-publication")));
  }

  @Test
  @WithMockUser(authorities = AuthoritiesConstants.PUBLISHER)
  public void shouldSucceedToPostvalidateProjectWithAnalysisPackage() throws Exception {
    DataAcquisitionProject project =
        UnitTestCreateDomainObjectUtils.buildDataAcquisitionProjectForAnalysisPackages();
    project.getConfiguration().setAnalysisPackagesState(new ProjectState(true, true));
    project.getConfiguration().setPublicationsState(new ProjectState(true, true));
    rdcProjectRepository.save(project);

    AnalysisPackage analysisPackage =
        UnitTestCreateDomainObjectUtils.buildAnalysisPackage(project.getId());
    analysisPackageRepository.save(analysisPackage);

    RelatedPublication relatedPublication =
        UnitTestCreateDomainObjectUtils.buildRelatedPublication();
    relatedPublication.setDataPackageIds(null);
    relatedPublication.setAnalysisPackageIds(List.of(analysisPackage.getId()));
    relatedPublicationRepository.save(relatedPublication);

    mockMvc.perform(post(API_DATA_ACQUISITION_PROJECTS_POST_VALIDATION_URI))
        .andExpect(status().isOk()).andExpect(jsonPath("$.errors", hasSize(0)));
  }

  private void applyNotReadyState(DataAcquisitionProject project) {
    Configuration configuration = project.getConfiguration();
    Requirements requirements = configuration.getRequirements();
    requirements.setDataSetsRequired(true);
    requirements.setInstrumentsRequired(true);
    requirements.setQuestionsRequired(true);
    requirements.setSurveysRequired(true);
    requirements.setVariablesRequired(true);
    ProjectState state = new ProjectState(false, false);
    configuration.setDataSetsState(state);
    configuration.setInstrumentsState(state);
    configuration.setQuestionsState(state);
    configuration.setDataPackagesState(state);
    configuration.setSurveysState(state);
    configuration.setVariablesState(state);
  }
}
