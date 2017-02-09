package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationMessageDto;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionImageService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * This service handels the post-validation of projects. It checks the foreign keys and references
 * between different domain objects. If a foreign key or reference is not valid, the service adds a
 * error message to a list. If everthing is checked, the service returns a list with all errors.
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
  private QuestionImageService questionImageService;

  /**
   * This method handels the complete post validation of a project.
   *
   * @param dataAcquisitionProjectId The id of the data acquisition project id.
   * @return a list of all post validation errors.
   */
  public List<PostValidationMessageDto> postValidate(String dataAcquisitionProjectId) {

    List<PostValidationMessageDto> errors = new ArrayList<>();
    
    //Check Study
    Study study = 
        this.studyRepository.findOneByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateStudies(study, errors);

    // Check questions
    List<Question> questions =
        this.questionRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateQuestions(questions, errors);

    // check data sets
    List<DataSet> dataSets =
        this.dataSetRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateDataSets(dataSets, errors);

    // check variables
    List<Variable> variables =
        this.variableRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateVariables(variables, errors);

    // check instruments
    List<Instrument> instruments =
        this.instrumentRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidateInstruments(instruments, errors);
    
    // check that there is a study for the project (all other domain objects might link to it)
    //TODO
    if (studyRepository.findOneByDataAcquisitionProjectId(dataAcquisitionProjectId) == null) {
      String[] information = {dataAcquisitionProjectId, dataAcquisitionProjectId};
      errors.add(new PostValidationMessageDto("data-acquisition-project-management.error."
          + "post-validation.project-has-no-study", Arrays.asList(information)));
    }

    return errors;
  }
  
  
  /**
   * This method checks all potential issues for study by post-validation.
   * @param studies All studies of a project.
   * @param errors The list of known errors.
   * @return The updated list of errors.
   */
  private List<PostValidationMessageDto> postValidateStudies(Study study,
      List<PostValidationMessageDto> errors) {
    
    //Check all AccessWays (if there some saved)
    if (study != null && study.getAccessWays().size() > 0) {
      List<DataSet> dataSets = 
          this.dataSetRepository.findByStudyId(study.getId());
      
      boolean found = false;
      for (String accessWay : study.getAccessWays()) {
        found = false; //Next Accessway is found yet
        dataSetLoop: for (DataSet dataSet : dataSets) {
          for (SubDataSet subDataSet : dataSet.getSubDataSets()) {
            if (subDataSet.getAccessWay().equals(accessWay)) {
              found = true;
              break dataSetLoop;
            }
          } //END FOR SUBDATASET
        } //END FOR DATASET
        
        //check if no AccessWay was found.
        if (!found) {
          String[] information = {study.getId(), accessWay};
          errors.add(new PostValidationMessageDto("study-management.error.post-validation."
              + "study-has-an-accessway-which-was-not-found-in-"
              + "sub-data-sets", Arrays.asList(information)));
        } 
      } // END FOR ACCESSWAY
    }
      
    return errors;
  }



  /**
   * This method checks all foreign keys and references within questions to other domain
   * objects.
   *
   * @return a list of errors of the post validation of questions.
   */
  private List<PostValidationMessageDto> postValidateQuestions(
      List<Question> questions, List<PostValidationMessageDto> errors) {

    for (Question question : questions) {
      
      // question.instrumentId: there must be a instrument with that id
      if (this.instrumentRepository.findOne(question.getInstrumentId()) == null) {
        String[] information = {question.getId(), question.getInstrumentId()};
        errors.add(new PostValidationMessageDto("question-management.error."
            + "post-validation.question-has-invalid-instrument-id", Arrays.asList(information)));
      }
      
      // question.successors: there must be a question with that id
      if (question.getSuccessors() != null && !question.getSuccessors().isEmpty()) {
        for (String successor : question.getSuccessors()) {
          if (questionRepository.findOne(successor) == null) {
            String[] information = {question.getId(), successor};
            errors.add(new PostValidationMessageDto("question-management.error."
                + "post-validation.question-has-invalid-successor", Arrays.asList(information)));
          }
        }
      }
      
      //check the image for question
      if (this.questionImageService.findQuestionImage(question.getId()) == null) {
        String[] information = {question.getId()};
        errors.add(new PostValidationMessageDto("question-management.error."
            + "post-validation.question-has-no-image", Arrays.asList(information)));
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
              + "post-validation.data-set-has-invalid-survey-id", Arrays.asList(information)));
        }
      }
      
      //check if all access ways of all sub dataset are in the study list of accessways.     
      Study study = this.studyRepository
          .findOneByDataAcquisitionProjectId(dataSet.getDataAcquisitionProjectId());
      if (study != null) {
        for (SubDataSet subDataSet : dataSet.getSubDataSets()) {
          if (!study.getAccessWays().contains(subDataSet.getAccessWay())) {
            String[] information = {subDataSet.getName(), subDataSet.getAccessWay()};
            errors.add(new PostValidationMessageDto("data-set-management.error.post-validation."
                + "sub-data-set-has-an-accessway-which-was-not-found-in-"
                + "study", Arrays.asList(information)));
          }        
        }
      }
    }

    return errors;
  }
  
  /**
   * This method checks all foreign keys and references within instruments to other domain objects.
   *
   * @return a list of errors of the post validation of instruments.
   */
  private List<PostValidationMessageDto> postValidateInstruments(List<Instrument> instruments,
      List<PostValidationMessageDto> errors) {

    for (Instrument instrument : instruments) {
      
      for (String surveyId : instrument.getSurveyIds()) {
        // surveyId: there must be a survey with that id
        if (this.surveyRepository.findOne(surveyId) == null) {
          String[] information = {instrument.getId(), surveyId};
          errors.add(new PostValidationMessageDto("instrument-management.error."
              + "post-validation.instrument-has-invalid-survey-id", Arrays.asList(information)));
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
      for (Integer surveyNumber : variable.getSurveyNumbers()) {
        String surveyId = variable.getDataAcquisitionProjectId() + "-sy" + surveyNumber;
        if (this.surveyRepository.findOne(surveyId) == null) {
          String[] information = {variable.getId(), surveyId};
          errors.add(new PostValidationMessageDto("variable-management.error."
              + "post-validation.variable-has-invalid-survey-id", Arrays.asList(information)));
        }
      }

      // variable.SameVariablesInPanel: there must be a variable with that id
      /*if (variable.getSameVariablesInPanel() != null) {
        for (String variableId : variable.getSameVariablesInPanel()) {
          if (this.variableRepository.findOne(variableId) == null) {
            String[] information = {variable.getId(), variableId};
            errors.add(new PostValidationMessageDto("variable-management.error."
                + "post-validation.variable-id-is-not-in-invalid-variables-panel",
                Arrays.asList(information)));
          }
        }
      }*/

      // variable.relatedQuestions.questionId: 
      // If there is no genereationDetail every variable needs a
      // questionId (and vice versa)
      if (variable.getRelatedQuestions() != null) {
        for (RelatedQuestion relatedQuestion : variable.getRelatedQuestions()) {
          if (relatedQuestion.getQuestionId() != null
              && this.questionRepository.findOne(relatedQuestion.getQuestionId()) == null) {
            String[] information = {variable.getId(), relatedQuestion.getQuestionId()};
            errors.add(new PostValidationMessageDto("variable-management.error."
                + "post-validation.variable-has-invalid-question-id", Arrays.asList(information)));
          }
        }
      }
      
      //variable.dataSetId: Check for for the data set id
      if (variable.getDataSetId() != null 
          && this.dataSetRepository.findOne(variable.getDataSetId()) == null) {
        String[] information = {variable.getId(), variable.getDataSetId()};
        errors.add(new PostValidationMessageDto("variable-management.error."
            + "post-validation.variable-has-invalid-data-set-id", Arrays.asList(information)));
      }
      
      //variable.relatedVariables: Check for variable ids
      if (variable.getRelatedVariables() != null) {
        for (String variableId : variable.getRelatedVariables()) {
          if (this.variableRepository.findOne(variableId) == null) {
            String[] information = {variable.getId(), variableId};
            errors.add(new PostValidationMessageDto("variable-management.error."
                + "post-validation.variable-id-is-not-valid-in-related-variables",
                Arrays.asList(information)));
          }
        }
      }
      
    }
    
    

    return errors;
  }

}
