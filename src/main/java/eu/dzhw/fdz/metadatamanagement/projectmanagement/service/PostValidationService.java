package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectState;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Requirements;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationMessageDto;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * This service handles the post-validation of projects. It checks the foreign keys and references
 * between different domain objects. If a foreign key or reference is not valid, the service adds a
 * error message to a list. If everything is checked, the service returns a list with all errors.
 *
 * @author Daniel Katzberg
 *
 */
@Service
public class PostValidationService {

  /* Repositories for loading data from the repository */
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
  private DataAcquisitionProjectRepository projectRepository;

  /**
   * This method handles the complete post validation of a project.
   *
   * @param dataAcquisitionProjectId The id of the data acquisition project id.
   * @param version the version which the project will get, can be null
   * @return a list of all post validation errors.
   */
  public List<PostValidationMessageDto> postValidate(String dataAcquisitionProjectId,
      String version) {
    boolean activateFullReleaseChecks = false;
    if (version != null
        && Version.valueOf(version).greaterThanOrEqualTo(Version.valueOf("1.0.0"))) {
      activateFullReleaseChecks = true;
    }
    List<PostValidationMessageDto> errors = new ArrayList<>();

    if (SecurityUtils.isUserInRole(AuthoritiesConstants.PUBLISHER)) {
      Optional<DataAcquisitionProject> project =
          projectRepository.findById(dataAcquisitionProjectId);
      if (project.isPresent()) {
        errors = postValidateProject(project.get(), errors);
      } else {
        PostValidationMessageDto error = new PostValidationMessageDto(
            "data-acquisition-project" + "-management.error.post-validation.no-project",
            Collections.singletonList(dataAcquisitionProjectId));
        return Collections.singletonList(error);
      }
    }

    errors = this.postValidateStudies(errors, dataAcquisitionProjectId);

    errors = this.postValidateSurveys(errors, dataAcquisitionProjectId, activateFullReleaseChecks);

    errors = this.postValidateQuestions(errors, dataAcquisitionProjectId);

    errors = this.postValidateDataSets(errors, dataAcquisitionProjectId, activateFullReleaseChecks);
    
    errors = this.postValidateVariables(errors, dataAcquisitionProjectId);

    errors = this.postValidateInstruments(errors, dataAcquisitionProjectId);

    return errors;
  }

  private List<PostValidationMessageDto> postValidateSurveys(List<PostValidationMessageDto> errors,
      String dataAcquisitionProjectId, boolean activateFullReleaseChecks) {
    try (Stream<Survey> surveys =
        this.surveyRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      if (activateFullReleaseChecks && !surveys.findFirst().isPresent()) {
        String[] information = {dataAcquisitionProjectId, dataAcquisitionProjectId};
        errors.add(new PostValidationMessageDto(
            "data-acquisition-project-management.error." + "post-validation.project-has-no-survey",
            Arrays.asList(information)));
      }
    }
    return errors;
  }

  private List<PostValidationMessageDto> postValidateProject(DataAcquisitionProject project,
      List<PostValidationMessageDto> errors) {

    Configuration configuration = project.getConfiguration();
    Requirements requirements = configuration.getRequirements();
    List<String> information = new ArrayList<>();

    if (isProjectStateInvalid(requirements.isStudiesRequired(), configuration.getStudiesState())) {
      information.add("studies");
    }

    if (isProjectStateInvalid(requirements.isSurveysRequired(), configuration.getSurveysState())) {
      information.add("surveys");
    }

    if (isProjectStateInvalid(requirements.isDataSetsRequired(),
        configuration.getDataSetsState())) {
      information.add("data_sets");
    }

    if (isProjectStateInvalid(requirements.isInstrumentsRequired(),
        configuration.getInstrumentsState())) {
      information.add("instruments");
    }

    if (isProjectStateInvalid(requirements.isQuestionsRequired(),
        configuration.getQuestionsState())) {
      information.add("questions");
    }

    if (isProjectStateInvalid(requirements.isVariablesRequired(),
        configuration.getVariablesState())) {
      information.add("variables");
    }

    if (!information.isEmpty()) {
      PostValidationMessageDto message = new PostValidationMessageDto(
          "data-acquisition" + "-project-management.error.post-validation.requirements-not-met",
          information);
      errors.add(message);
    }

    return errors;
  }

  private boolean isProjectStateInvalid(boolean required, ProjectState projectState) {
    return required && (projectState == null || !projectState.isPublisherReady());
  }

  /**
   * This method checks all potential issues for study by post-validation.
   * 
   * @param errors The list of known errors.
   * @param dataAcquisitionProjectId The project id.
   * @return The updated list of errors.
   */
  private List<PostValidationMessageDto> postValidateStudies(List<PostValidationMessageDto> errors,
      String dataAcquisitionProjectId) {
    Study study = this.studyRepository.findOneByDataAcquisitionProjectId(dataAcquisitionProjectId);
    // check that there is a study for the project (all other domain objects might link to it)
    if (study == null) {
      String[] information = {dataAcquisitionProjectId, dataAcquisitionProjectId};
      errors.add(new PostValidationMessageDto(
          "data-acquisition-project-management.error." + "post-validation.project-has-no-study",
          Arrays.asList(information)));
    }

    return errors;
  }



  /**
   * This method checks all foreign keys and references within questions to other domain objects.
   *
   * @return a list of errors of the post validation of questions.
   */
  private List<PostValidationMessageDto> postValidateQuestions(
      List<PostValidationMessageDto> errors, String dataAcquisitionProjectId) {

    try (Stream<Question> questions =
        this.questionRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      questions.forEach(question -> {
        // question.instrumentId: there must be a instrument with that id
        if (!this.instrumentRepository.findById(question.getInstrumentId()).isPresent()) {
          String[] information = {question.getId(), question.getInstrumentId()};
          errors.add(new PostValidationMessageDto(
              "question-management.error." + "post-validation.question-has-invalid-instrument-id",
              Arrays.asList(information)));
        }

        // question.successors: there must be a question with that id
        if (question.getSuccessors() != null && !question.getSuccessors().isEmpty()) {
          for (String successor : question.getSuccessors()) {
            if (!questionRepository.findById(successor).isPresent()) {
              String[] information = {question.getId(), successor};
              errors.add(new PostValidationMessageDto(
                  "question-management.error." + "post-validation.question-has-invalid-successor",
                  Arrays.asList(information)));
            }
          }
        }
      });
    }
    return errors;
  }

  /**
   * This method checks all foreign keys and references within data sets to other domain objects.
   *
   * @return a list of errors of the post validation of data sets.
   */
  private List<PostValidationMessageDto> postValidateDataSets(List<PostValidationMessageDto> errors,
      String dataAcquisitionProjectId, boolean activateFullReleaseChecks) {
    AtomicReference<Boolean> noDataSets = new AtomicReference<Boolean>(true);
    try (Stream<DataSet> dataSets =
        this.dataSetRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      dataSets.forEach(dataSet -> {
        noDataSets.set(false);
        // dataSet.SurveyIds: there must be a survey with that id
        for (String surveyId : dataSet.getSurveyIds()) {
          if (!this.surveyRepository.findById(surveyId).isPresent()) {
            String[] information = {dataSet.getId(), surveyId};
            errors.add(new PostValidationMessageDto(
                "data-set-management.error." + "post-validation.data-set-has-invalid-survey-id",
                Arrays.asList(information)));
          }
        }
      });
    }

    if (activateFullReleaseChecks && noDataSets.get()) {
      String[] information = {dataAcquisitionProjectId, dataAcquisitionProjectId};
      errors.add(new PostValidationMessageDto(
          "data-acquisition-project-management.error." + "post-validation.project-has-no-data-set",
          Arrays.asList(information)));
    }

    return errors;
  }

  /**
   * This method checks all foreign keys and references within instruments to other domain objects.
   *
   * @return a list of errors of the post validation of instruments.
   */
  private List<PostValidationMessageDto> postValidateInstruments(
      List<PostValidationMessageDto> errors, String dataAcquisitionProjectId) {
    try (Stream<Instrument> instruments =
        this.instrumentRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      instruments.forEach(instrument -> {
        for (String surveyId : instrument.getSurveyIds()) {
          // surveyId: there must be a survey with that id
          if (!this.surveyRepository.findById(surveyId).isPresent()) {
            String[] information = {instrument.getId(), surveyId};
            errors.add(new PostValidationMessageDto(
                "instrument-management.error." + "post-validation.instrument-has-invalid-survey-id",
                Arrays.asList(information)));
          }
        }
      });
    }

    return errors;
  }

  /**
   * This method checks all foreign keys and references within variables to other domain objects.
   *
   * @return a list of errors of the post validation of variables.
   */
  private List<PostValidationMessageDto> postValidateVariables(
      List<PostValidationMessageDto> errors, String dataAcquisitionProjectId) {

    try (Stream<Variable> variables =
        this.variableRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      variables.forEach(variable -> {
        // variable.SurveyId: there must be a survey with that id
        for (String surveyId : variable.getSurveyIds()) {
          if (!this.surveyRepository.findById(surveyId).isPresent()) {
            String[] information = {variable.getId(), surveyId};
            errors.add(new PostValidationMessageDto(
                "variable-management.error." + "post-validation.variable-has-invalid-survey-id",
                Arrays.asList(information)));
          }
        }

        // variable.relatedQuestions.questionId:
        // If there is no genereationDetail every variable needs a
        // questionId (and vice versa)
        if (variable.getRelatedQuestions() != null) {
          for (RelatedQuestion relatedQuestion : variable.getRelatedQuestions()) {
            if (relatedQuestion.getQuestionId() != null
                && !this.questionRepository.findById(relatedQuestion.getQuestionId()).isPresent()) {
              String[] information = {variable.getId(), relatedQuestion.getQuestionId()};
              errors.add(new PostValidationMessageDto(
                  "variable-management.error." + "post-validation.variable-has-invalid-question-id",
                  Arrays.asList(information)));
            }
          }
        }

        // variable.dataSetId: Check for for the data set id
        if (variable.getDataSetId() != null) {
          DataSet dataSet = this.dataSetRepository.findById(variable.getDataSetId()).orElse(null);
          if (dataSet == null) {
            String[] information = {variable.getId(), variable.getDataSetId()};
            errors.add(new PostValidationMessageDto(
                "variable-management.error." + "post-validation.variable-has-invalid-data-set-id",
                Arrays.asList(information)));
          } else {
            // check that variable.surveyIds is a subset of dataSet.surveyIds
            if (!dataSet.getSurveyIds().containsAll(variable.getSurveyIds())) {
              String[] information = {variable.getId(), variable.getDataSetId()};
              errors.add(new PostValidationMessageDto(
                  "variable-management.error."
                      + "post-validation.variable-survey-ids-are-not-consistent-with-data-set",
                  Arrays.asList(information)));
            }
          }
        }

        // variable.relatedVariables: Check for variable ids
        if (variable.getRelatedVariables() != null) {
          for (String variableId : variable.getRelatedVariables()) {
            if (!this.variableRepository.findById(variableId).isPresent()) {
              String[] information = {variable.getId(), variableId};
              errors.add(new PostValidationMessageDto(
                  "variable-management.error."
                      + "post-validation.variable-id-is-not-valid-in-related-variables",
                  Arrays.asList(information)));
            }
          }
        }

      });
    }

    return errors;
  }

}
