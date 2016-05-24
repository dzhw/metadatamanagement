package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.AtomicQuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionnairemanagement.repository.QuestionnaireRepository;
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
// TODO Dkatzberg ... only prototype code at the moment.
@Service
public class PostValidationService {

  @Inject
  private MessageSource messageSource;

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

  @Inject
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private Locale locale;

  /**
   * This method handels the complete post validation of a project.
   * 
   * @param dataAcquisitionProjectId The id of the data acquisition project id.
   * @return a list of all post validation errors.
   */
  public List<String> postValidation(String dataAcquisitionProjectId) {

    // Set locale
    this.locale = LocaleContextHolder.getLocale();

    List<String> errors = new ArrayList<>();
    errors.addAll(this.postValidationOfAtomicQuestions());
    errors.addAll(this.postValidationOfDataSets());
    errors.addAll(this.postValidationOfSurverys());

    // check variables
    List<Variable> variables =
        this.variableRepository.findByDataAcquisitionProjectId(dataAcquisitionProjectId);
    errors = this.postValidationOfVariables(variables, errors);

    return errors;
  }

  /**
   * This method checks all foreign keys and references within atomic questions to other domain
   * objects.
   * 
   * @return a list of errors of the post validation of atomic questions.
   */
  private List<String> postValidationOfAtomicQuestions() {

    return new ArrayList<>();
  }

  /**
   * This method checks all foreign keys and references within data sets to other domain objects.
   * 
   * @return a list of errors of the post validation of data sets.
   */
  private List<String> postValidationOfDataSets() {

    return new ArrayList<>();
  }


  /**
   * This method checks all all foreign keys and references within surveys to other domain objects.
   * 
   * @return a list of errors of the post validation of surveys.
   */
  private List<String> postValidationOfSurverys() {

    // survey.VariableIds: there must be a variable with that id
    // survey.QuestionnaireId: there must be a questionaire with that id
    // survey.DataSetId: there must be a dataset with that id

    return new ArrayList<>();
  }

  /**
   * This method checks all foreign keys and references within variables to other domain objects.
   * 
   * @return a list of errors of the post validation of variables.
   */
  private List<String> postValidationOfVariables(List<Variable> variables, List<String> errors) {

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
      if (variable.getSameVariablesInPanel() != null 
          && !variable.getSameVariablesInPanel().contains(variable.getId())) {
        String[] information = {variable.getId()};
        errors.add(this.messageSource
            .getMessage("error.postValidation.variableIdIsNotInInvalidVariablesPanel", 
                information, locale));
      }
      

      // variable.atomicQuestionId: If there is no genereationDetail every variable needs a
      // atomicQuestionId (and vice versa)
      if (variable.getGenerationDetails() == null && variable.getAtomicQuestionId() != null
          && this.atomicQuestionRepository.findOne(variable.getAtomicQuestionId()) == null) {
        String[] information = {variable.getId(), variable.getAtomicQuestionId()};
        errors.add(this.messageSource
            .getMessage("error.postValidation.variableHasInvalidAtomicQuestionId", 
                information, locale));
      }
    }
    return errors;
  }
  
}
