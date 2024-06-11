
package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
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
import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationMessageDto;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;

/**
 * This service handles the post-validation of projects. It checks the foreign keys and references
 * between different domain objects. If a foreign key or reference is not valid, the service adds a
 * error message to a list. If everything is checked, the service returns a list with all errors.
 *
 * @author Daniel Katzberg
 *
 */
@Service
@RequiredArgsConstructor
public class PostValidationService {

  private final VariableRepository variableRepository;

  private final SurveyRepository surveyRepository;

  private final DataSetRepository dataSetRepository;

  private final InstrumentRepository instrumentRepository;

  private final QuestionRepository questionRepository;

  private final DataPackageRepository dataPackageRepository;

  private final AnalysisPackageRepository analysisPackageRepository;

  private final RelatedPublicationRepository relatedPublicationRepository;

  private final DataAcquisitionProjectRepository projectRepository;

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

    Optional<DataAcquisitionProject> project = projectRepository.findById(dataAcquisitionProjectId);
    if (!project.isPresent()) {
      PostValidationMessageDto error = new PostValidationMessageDto(
          "data-acquisition-project" + "-management.error.post-validation.no-project",
          Collections.singletonList(dataAcquisitionProjectId));
      return Collections.singletonList(error);
    }

    if (SecurityUtils.isUserInRole(AuthoritiesConstants.PUBLISHER)) {
      errors = postValidateProject(project.get(), errors);
    }

    if (project.get().getConfiguration().getRequirements().isDataPackagesRequired()) {
      errors = this.postValidateDataPackages(errors, dataAcquisitionProjectId);
      errors =
          this.postValidateSurveys(errors, dataAcquisitionProjectId, activateFullReleaseChecks);

      errors = this.postValidateQuestions(errors, dataAcquisitionProjectId);

      errors =
          this.postValidateDataSets(errors, dataAcquisitionProjectId, activateFullReleaseChecks);

      errors = this.postValidateVariables(errors, dataAcquisitionProjectId);

      errors =
          this.postValidateInstruments(errors, dataAcquisitionProjectId, activateFullReleaseChecks);
    } else if (project.get().getConfiguration().getRequirements().isAnalysisPackagesRequired()) {
      errors = this.postValidateAnalysisPackages(errors, dataAcquisitionProjectId,
          activateFullReleaseChecks);
    }

    return errors;
  }

  /**
   * This method handles a reduced post validation of a project. Validation includes checks whether the
   * project is defined and whether it includes a data package or an analysis packages.
   *
   * @param dataAcquisitionProjectId The id of the data acquisition project id.
   * @return a list of all post validation errors.
   */
  public List<PostValidationMessageDto> postValidatePreRelease(String dataAcquisitionProjectId) {
    List<PostValidationMessageDto> errors = new ArrayList<>();

    Optional<DataAcquisitionProject> project = projectRepository.findById(dataAcquisitionProjectId);
    if (!project.isPresent()) {
      PostValidationMessageDto error = new PostValidationMessageDto(
          "data-acquisition-project" + "-management.error.post-validation.no-project",
          Collections.singletonList(dataAcquisitionProjectId));
      return Collections.singletonList(error);
    }

    if (SecurityUtils.isUserInRole(AuthoritiesConstants.PUBLISHER)) {
      errors = postValidatePreReleaseProject(project.get(), errors);
    }

    if (project.get().getConfiguration().getRequirements().isDataPackagesRequired()) {
      errors = this.postValidateDataPackages(errors, dataAcquisitionProjectId);
    } else if (project.get().getConfiguration().getRequirements().isAnalysisPackagesRequired()) {
      errors = this.postValidateAnalysisPackages(errors, dataAcquisitionProjectId,
        false);
    }

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

    if (isProjectStateInvalid(requirements.isDataPackagesRequired(),
        configuration.getDataPackagesState())) {
      information.add("dataPackages");
    }

    if (isProjectStateInvalid(requirements.isAnalysisPackagesRequired(),
        configuration.getAnalysisPackagesState())) {
      information.add("analysisPackages");
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

    if (isProjectStateInvalid(requirements.isPublicationsRequired(),
        configuration.getPublicationsState())) {
      information.add("publications");
    }

    if (isProjectStateInvalid(requirements.isConceptsRequired(),
        configuration.getConceptsState())) {
      information.add("concepts");
    }

    if (!information.isEmpty()) {
      PostValidationMessageDto message = new PostValidationMessageDto(
          "data-acquisition-project-management.error.post-validation.requirements-not-met",
          information);
      errors.add(message);
    }

    return errors;
  }

  /**
   * This method handles a reduced validation of a data acquisition project for pre-releases.
   * Checks include whether the project has an embargo date, and valid data for data packages or
   * analysis packages.
   * @param project the project to be validated
   * @param errors the list of validation errors
   * @return the complete list of validation errors
   */
  private List<PostValidationMessageDto> postValidatePreReleaseProject(DataAcquisitionProject project,
                                                             List<PostValidationMessageDto> errors) {

    Configuration configuration = project.getConfiguration();
    Requirements requirements = configuration.getRequirements();
    List<String> information = new ArrayList<>();

    if (project.getEmbargoDate() == null) {
      PostValidationMessageDto message = new PostValidationMessageDto(
          "data-acquisition-project-management.error.post-validation.no-embargo-date",
          Collections.singletonList(project.getId()));
      errors.add(message);
    }

    if (isProjectStateInvalid(requirements.isDataPackagesRequired(),
        configuration.getDataPackagesState())) {
      information.add("dataPackages");
    }

    if (isProjectStateInvalid(requirements.isAnalysisPackagesRequired(),
        configuration.getAnalysisPackagesState())) {
      information.add("analysisPackages");
    }

    if (!information.isEmpty()) {
      PostValidationMessageDto message = new PostValidationMessageDto(
          "data-acquisition-project-management.error.post-validation.requirements-not-met",
          information);
      errors.add(message);
    }

    return errors;
  }

  /**
   * Checks if the required project part is marked as ready by publisher.
   * @param required if the part of the project is required (e.g. data package)
   * @param projectState the state of the project part
   * @return true if the part is required but not marked as ready, else false
   */
  private boolean isProjectStateInvalid(boolean required, ProjectState projectState) {
    return required && (projectState == null || !projectState.isPublisherReady());
  }

  /**
   * This method checks all potential issues for dataPackage by post-validation.
   *
   * @param errors The list of known errors.
   * @param dataAcquisitionProjectId The project id.
   * @return The updated list of errors.
   */
  private List<PostValidationMessageDto> postValidateDataPackages(
      List<PostValidationMessageDto> errors, String dataAcquisitionProjectId) {
    DataPackage dataPackage =
        this.dataPackageRepository.findOneByDataAcquisitionProjectId(dataAcquisitionProjectId);
    // check that there is a dataPackage for the project (all other domain objects might link to it)
    if (dataPackage == null) {
      String[] information = {dataAcquisitionProjectId, dataAcquisitionProjectId};
      errors.add(new PostValidationMessageDto("data-acquisition-project-management.error."
          + "post-validation.project-has-no-dataPackage", Arrays.asList(information)));
    }

    return errors;
  }

  /**
   * This method checks all potential issues for analysis package by post-validation.
   *
   * @param errors The list of known errors.
   * @param dataAcquisitionProjectId The project id.
   * @return The updated list of errors.
   */
  private List<PostValidationMessageDto> postValidateAnalysisPackages(
      List<PostValidationMessageDto> errors, String dataAcquisitionProjectId,
      boolean activateFullReleaseChecks) {
    AnalysisPackage analysisPackage =
        this.analysisPackageRepository.findOneByDataAcquisitionProjectId(dataAcquisitionProjectId);
    // check that there is an analysis package for the project (all other domain objects might link
    // to it)
    if (analysisPackage == null) {
      String[] information = {dataAcquisitionProjectId, dataAcquisitionProjectId};
      errors.add(new PostValidationMessageDto("data-acquisition-project-management.error."
          + "post-validation.project-has-no-analysisPackage", Arrays.asList(information)));
    } else {
      if (activateFullReleaseChecks && relatedPublicationRepository
          .countByAnalysisPackageIdsContaining(analysisPackage.getId()) != 1) {
        String[] information = {dataAcquisitionProjectId, analysisPackage.getId()};
        errors.add(new PostValidationMessageDto(
            "data-acquisition-project-management.error."
                + "post-validation.project-must-have-exactly-one-publication",
            Arrays.asList(information)));
      }
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
      List<PostValidationMessageDto> errors, String dataAcquisitionProjectId,
      boolean activateFullReleaseChecks) {

    if (activateFullReleaseChecks
        && !instrumentRepository.existsByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      errors.add(new PostValidationMessageDto(
          "instrument-management.error." + "post-validation.no-instruments",
          Collections.emptyList()));
      return errors;
    }

    try (Stream<Instrument> instruments =
        this.instrumentRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      instruments.forEach(instrument -> {
        for (String surveyId : instrument.getSurveyIds()) {
          // surveyId: there must be a survey with that id
          if (!this.surveyRepository.findById(surveyId).isPresent()) {
            String[] information = {instrument.getId(), surveyId};
            errors.add(new PostValidationMessageDto(
                "instrument-management.error.post-validation." + "instrument-has-invalid-survey-id",
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
