package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

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

  /**
   * This method handels the complete post validation of a project.
   * 
   * @param dataAcquisitionProjectId The id of the data acquisition project id.
   * @return a list of all post validation errors.
   */
  public List<String> postValidation(String dataAcquisitionProjectId) {

    // Set locale
    Locale locale = LocaleContextHolder.getLocale();

    List<String> errors = new ArrayList<>();
    errors.addAll(this.postValidationOfAtomicQuestions());
    errors.addAll(this.postValidationOfDataSets());
    errors.addAll(this.postValidationOfSurverys());
    errors.addAll(this.postValidationOfVariables());

    // Test Message
    errors.add(this.messageSource.getMessage("error.postValidation.test", null, locale));

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

    return new ArrayList<>();
  }

  /**
   * This method checks all foreign keys and references within variables to other domain objects.
   * 
   * @return a list of errors of the post validation of variables.
   */
  private List<String> postValidationOfVariables() {

    return new ArrayList<>();
  }



}
