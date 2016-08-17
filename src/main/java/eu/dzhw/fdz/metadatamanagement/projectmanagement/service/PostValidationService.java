package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationMessageDto;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
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
  private QuestionRepository questionRepository;


  /**
   * This method handels the complete post validation of a project.
   * 
   * @param dataAcquisitionProjectId The id of the data acquisition project id.
   * @return a list of all post validation errors.
   */
  public List<PostValidationMessageDto> postValidate(String dataAcquisitionProjectId) {

    List<PostValidationMessageDto> errors = new ArrayList<>();

    // Check atomic questions
    List<Question> questions =
        this.questionRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateQuestions(questions, errors);

    // check data sets
    List<DataSet> dataSets =
        this.dataSetRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateDataSets(dataSets, errors);

    // check surveys
    List<Survey> surveys =
        this.surveyRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateSurverys(surveys, errors);

    // check variables
    List<Variable> variables =
        this.variableRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateVariables(variables, errors);

    return errors;
  }

  /**
   * This method checks all foreign keys and references within atomic questions to other domain
   * objects.
   * 
   * @return a list of errors of the post validation of atomic questions.
   */
  private List<PostValidationMessageDto> postValidateQuestions(
      List<Question> questions, List<PostValidationMessageDto> errors) {

    for (Question question : questions) {

      // question.VariableIds: there must be variables with that id
      for (String variableId : question.getVariableIds()) {
        if (this.variableRepository.findOne(variableId) == null) {
          String[] information = {question.getId(), variableId};
          errors.add(new PostValidationMessageDto("question-management.error." 
              + "post-validation.question-has-invalid-variable-id", information));
        }
      }

      // question.QuestionnaireId: there must be a questionaire with that id
      if (this.questionnaireRepository.findOne(question.getInstrumentId()) == null) {
        String[] information = {question.getId(), question.getInstrumentId()};
        errors.add(new PostValidationMessageDto("question-management.error." 
            + "post-validation.question-has-invalid-questionnaire-id", information));
      }
      
      // question.QuestionnaireId: there must be a survey with that id
      if (this.surveyRepository.findOne(question.getSurveyId()) == null) {
        String[] information = {question.getId(), question.getSurveyId()};
        errors.add(new PostValidationMessageDto("question-management.error." 
            + "post-validation.question-has-invalid-survey-id", information));
      }
    }

    return errors;
  }

  /**
   * This method checks all foreign keys and references within data sets to other domain objects.
   * 
   * @return a list of errors of the post validation of data sets.
   */
  private List<PostValidationMessageDto> postValidateDataSets(List<DataSet> dataSets, 
      List<PostValidationMessageDto> errors) {

    for (DataSet dataSet : dataSets) {

      // dataSet.SurveyIds: there must be a survey with that id
      for (String surveyId : dataSet.getSurveyIds()) {
        if (this.surveyRepository.findOne(surveyId) == null) {
          String[] information = {dataSet.getId(), surveyId};
          errors.add(new PostValidationMessageDto("data-set-management.error." 
              + "post-validation.data-set-has-invalid-survey-id", information));
        }
      }

      // dataSet.VariableIds: there must be a variable with that id
      for (String variableId : dataSet.getVariableIds()) {
        if (this.variableRepository.findOne(variableId) == null) {
          String[] information = {dataSet.getId(), variableId};
          errors.add(new PostValidationMessageDto("data-set-management.error." 
              + "post-validation.data-set-has-invalid-variable-id", information));
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
  private List<PostValidationMessageDto> postValidateSurverys(List<Survey> surveys, 
      List<PostValidationMessageDto> errors) {

    for (Survey survey : surveys) {

      // survey.QuestionnaireId: there must be a questionaire with that id
      if (this.questionnaireRepository.findOne(survey.getQuestionnaireId()) == null) {
        String[] information = {survey.getId(), survey.getQuestionnaireId()};
        errors.add(new PostValidationMessageDto("survey-management.error." 
            + "post-validation.survey-has-invalid-questionnaire-id", information));
      }

      // survey.DataSetId: there must be a dataset with that id
      for (String dataSetId : survey.getDataSetIds()) {
        if (this.dataSetRepository.findOne(dataSetId) == null) {
          String[] information = {survey.getId(), dataSetId};
          errors.add(new PostValidationMessageDto("survey-management.error." 
              + "post-validation.survey-has-invalid-data-set-id", information));
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
  private List<PostValidationMessageDto> postValidateVariables(List<Variable> variables, 
      List<PostValidationMessageDto> errors) {

    for (Variable variable : variables) {

      // variable.SurveyId: there must be a survey with that id
      for (String surveyId : variable.getSurveyIds()) {
        if (this.surveyRepository.findOne(surveyId) == null) {
          String[] information = {variable.getId(), surveyId};
          errors.add(new PostValidationMessageDto("variable-management.error." 
              + "post-validation.variable-has-invalid-survey-id", information));
        }
      }

      // variable.DataSetIds: there must be a dataset with that id
      for (String dataSetId : variable.getDataSetIds()) {
        if (this.dataSetRepository.findOne(dataSetId) == null) {
          String[] information = {variable.getId(), dataSetId};
          errors.add(new PostValidationMessageDto("variable-management.error." 
              + "post-validation.variable-has-invalid-data-set-id", information));
        }
      }

      // variable.SameVariablesInPanel: there must be a variable with that id
      if (variable.getSameVariablesInPanel() != null) {
        for (String variableId : variable.getSameVariablesInPanel()) {
          if (this.variableRepository.findOne(variableId) == null) {
            String[] information = {variable.getId(), variableId};
            errors.add(new PostValidationMessageDto("variable-management.error." 
                + "post-validation.variable-id-is-not-in-invalid-variables-panel", information));
          }        
        }        
      }

      // variable.questionId: If there is no genereationDetail every variable needs a
      // questionId (and vice versa)
      if (variable.getAtomicQuestionId() != null
          && this.questionRepository.findOne(variable.getAtomicQuestionId()) == null) {
        String[] information = {variable.getId(), variable.getAtomicQuestionId()};
        errors.add(new PostValidationMessageDto("variable-management.error." 
            + "post-validation.variable-has-invalid-question-id", information));
      }
    }

    return errors;
  }

}
