package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.AtomicQuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionnairemanagement.repository.QuestionnaireRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * This service handels the post-validation of projects. It checks the foreign keys and references
 * between different domain objects. If a foreign key or reference is not valid, the service adds a
 * error message to a list. If everthing is checked, the service returns a list with all errors.
 * 
 * @author dkatzberg
 *
 */
@Service
public class PostValidationService {

  @Inject
  private MessageSource messageSource;

  /* Repositories for loading data from the repository */
  @Inject
  private VariableRepository variableRepository;

  @Inject
  private SurveyRepository surveyRepository;

  @Inject
  private DataSetRepository dataSetRepository;

  @Inject
  private QuestionnaireRepository questionnaireRepository;

  @Inject
  private AtomicQuestionRepository atomicQuestionRepository;


  /**
   * This method handels the complete post validation of a project.
   * 
   * @param dataAcquisitionProjectId The id of the data acquisition project id.
   * @return a list of all post validation errors.
   */
  public List<String> postValidate(String dataAcquisitionProjectId) {

    // Set locale
    Locale locale = LocaleContextHolder.getLocale();

    List<String> errors = new ArrayList<>();

    // Check atomic questions
    List<AtomicQuestion> atomicQuestions =
        this.atomicQuestionRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateAtomicQuestions(atomicQuestions, errors, locale);

    // check data sets
    List<DataSet> dataSets =
        this.dataSetRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateDataSets(dataSets, errors, locale);

    // check surveys
    List<Survey> surveys =
        this.surveyRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateSurverys(surveys, errors, locale);

    // check variables
    List<Variable> variables =
        this.variableRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateVariables(variables, errors, locale);

    return errors;
  }

  /**
   * This method checks all foreign keys and references within atomic questions to other domain
   * objects.
   * 
   * @return a list of errors of the post validation of atomic questions.
   */
  private List<String> postValidateAtomicQuestions(List<AtomicQuestion> atomicQuestions,
      List<String> errors, Locale locale) {

    for (AtomicQuestion atomicQuestion : atomicQuestions) {

      // atomicQuestion.VariableId: there must be a variable with that id
      if (this.variableRepository.findOne(atomicQuestion.getVariableId()) == null) {
        String[] information = {atomicQuestion.getId(), atomicQuestion.getVariableId()};
        errors.add(this.messageSource.getMessage(
            "error.postValidation.atomicQuestionHasInvalidVariableId", information, locale));
      }

      // atomicQuestion.QuestionnaireId: there must be a questionaire with that id
      if (this.questionnaireRepository.findOne(atomicQuestion.getQuestionnaireId()) == null) {
        String[] information = {atomicQuestion.getId(), atomicQuestion.getQuestionnaireId()};
        errors.add(this.messageSource.getMessage(
            "error.postValidation.atomicQuestionHasInvalidQuestionnaireId", information, locale));
      }
    }

    return errors;
  }

  /**
   * This method checks all foreign keys and references within data sets to other domain objects.
   * 
   * @return a list of errors of the post validation of data sets.
   */
  private List<String> postValidateDataSets(List<DataSet> dataSets, List<String> errors,
      Locale locale) {

    for (DataSet dataSet : dataSets) {

      // dataSet.SurveyIds: there must be a survey with that id
      for (String surveyId : dataSet.getSurveyIds()) {
        if (this.surveyRepository.findOne(surveyId) == null) {
          String[] information = {dataSet.getId(), surveyId};
          errors.add(this.messageSource.getMessage("error.postValidation.dataSetHasInvalidSurveyId",
              information, locale));
        }
      }

      // dataSet.VariableIds: there must be a variable with that id
      for (String variableId : dataSet.getVariableIds()) {
        if (this.variableRepository.findOne(variableId) == null) {
          String[] information = {dataSet.getId(), variableId};
          errors.add(this.messageSource
              .getMessage("error.postValidation.dataSetHasInvalidVariableId", information, locale));
        }
      }
    }

    return errors;
  }


  /**
   * This method checks all all foreign keys and references within surveys to other domain objects.
   * 
   * @return a list of errors of the post validation of surveys.
   */
  private List<String> postValidateSurverys(List<Survey> surveys, List<String> errors,
      Locale locale) {

    for (Survey survey : surveys) {

      // survey.QuestionnaireId: there must be a questionaire with that id
      if (this.questionnaireRepository.findOne(survey.getQuestionnaireId()) == null) {
        String[] information = {survey.getId(), survey.getQuestionnaireId()};
        errors.add(this.messageSource
            .getMessage("error.postValidation.surveyHasInvalidQuestionnaireId", information,
                locale));
      }

      // survey.DataSetId: there must be a dataset with that id
      for (String dataSetId : survey.getDataSetIds()) {
        if (this.dataSetRepository.findOne(dataSetId) == null) {
          String[] information = {survey.getId(), dataSetId};
          errors.add(this.messageSource.getMessage("error.postValidation.surveyHasInvalidDataSetId",
              information, locale));
        }
      }
    }

    return errors;
  }

  /**
   * This method checks all foreign keys and references within variables to other domain objects.
   * 
   * @return a list of errors of the post validation of variables.
   */
  private List<String> postValidateVariables(List<Variable> variables, List<String> errors,
      Locale locale) {

    for (Variable variable : variables) {

      // variable.SurveyId: there must be a survey with that id
      for (String surveyId : variable.getSurveyIds()) {
        if (this.surveyRepository.findOne(surveyId) == null) {
          String[] information = {variable.getId(), surveyId};
          errors.add(this.messageSource
              .getMessage("error.postValidation.variableHasInvalidSurveyId", information, locale));
        }
      }

      // variable.DataSetIds: there must be a dataset with that id
      for (String dataSetId : variable.getDataSetIds()) {
        if (this.dataSetRepository.findOne(dataSetId) == null) {
          String[] information = {variable.getId(), dataSetId};
          errors.add(this.messageSource
              .getMessage("error.postValidation.variableHasInvalidDataSetId", information, locale));
        }
      }

      // variable.SameVariablesInPanel: there must be a variable with that id
      for (String variableId : variable.getSameVariablesInPanel()) {
        if (this.variableRepository.findOne(variableId) == null) {
          String[] information = {variable.getId()};
          errors.add(this.messageSource.getMessage(
              "error.postValidation.variableIdIsNotInInvalidVariablesPanel",
              information, locale));
        }        
      }


      // variable.atomicQuestionId: If there is no genereationDetail every variable needs a
      // atomicQuestionId (and vice versa)
      if (variable.getAtomicQuestionId() != null
          && this.atomicQuestionRepository.findOne(variable.getAtomicQuestionId()) == null) {
        String[] information = {variable.getId(), variable.getAtomicQuestionId()};
        errors.add(this.messageSource.getMessage(
            "error.postValidation.variableHasInvalidAtomicQuestionId", information, locale));
      }
    }

    return errors;
  }

}
