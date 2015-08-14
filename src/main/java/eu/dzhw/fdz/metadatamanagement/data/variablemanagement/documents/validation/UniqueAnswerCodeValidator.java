package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.AnswerOption;

/**
 * This is the validator for the {@link UniqueAnswerCode} annotation. It checks the unique
 * 
 * @author Daniel Katzberg
 *
 */
public class UniqueAnswerCodeValidator
    implements ConstraintValidator<UniqueAnswerCode, List<AnswerOption>> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueAnswerCode constraintAnnotation) {
    // Do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(List<AnswerOption> answerOptions, ConstraintValidatorContext context) {

    // check for an empty field
    if (answerOptions == null) {
      return true;
    }

    // check answer option for double used codes
    // performance O(n^2)
    for (AnswerOption answerOption : answerOptions) {

      //check for empty code field
      if (answerOption.getCode() == null) {
        continue;
      }

      int code = answerOption.getCode();
      int countCode = 0;

      //check double entry
      for (AnswerOption answerOptionCheck : answerOptions) {
        
        //check for empty code field
        if (answerOptionCheck.getCode() == null) {
          continue;
        }
        
        if (code == answerOptionCheck.getCode()) {
          countCode++;
        }
      }

      // = 1 : find itself
      // > 1 : find double or more use
      if (countCode > 1) {
        return false;
      }
    }

    return true;
  }

}
