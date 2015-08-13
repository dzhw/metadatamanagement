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
  public boolean isValid(List<AnswerOption> value, ConstraintValidatorContext context) {

    // check for an empty field
    if (value == null) {
      return true;
    }

    // Are there
    for (AnswerOption answerOption : value) {

      if (answerOption.getCode() == null) {
        continue;
      }

      int code = answerOption.getCode();
      int countCode = 0;

      for (AnswerOption answerOptionCheck : value) {
        if (code == answerOptionCheck.getCode()) {
          countCode++;
        }
      }

      if (countCode > 1) {
        return false;
      }
    }

    return true;
  }

}
