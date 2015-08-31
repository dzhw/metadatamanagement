package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;

/**
 * This is the validator for the annoation {@link NoEditableId}. It checks in an edit process, that
 * the id is not changed.
 * 
 * @author Daniel Katzberg
 */
public class NoEditableIdValidator implements ConstraintValidator<NoEditableId, String> {

  /**
   * Repository for variables. Writes in different indexes which are depending at the supported
   * languages.
   */
  private VariableRepository variableRepository;

  /**
   * @param variableRepository The variableRepository writes and deletes variables to the
   *        repository.
   */
  @Autowired
  public NoEditableIdValidator(VariableRepository variableRepository) {
    this.variableRepository = variableRepository;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(NoEditableId constraintAnnotation) {
    // Do nothing!
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String variableDocumentId, ConstraintValidatorContext context) {
    // is an necessary field
    if (variableDocumentId == null) {
      return false;
    }

    // check id
    if (this.variableRepository.findOne(variableDocumentId) == null) {
      return false;
    } else {
      return true;
    }
  }

}
