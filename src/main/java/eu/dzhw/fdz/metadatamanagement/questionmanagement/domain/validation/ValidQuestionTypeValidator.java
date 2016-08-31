package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.QuestionTypes;

/**
 * Validator of the question type. Only values from the class {@link QuestionTypes} are allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidQuestionTypeValidator implements 
    ConstraintValidator<ValidQuestionType, I18nString> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidQuestionType constraintAnnotation) { }

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString questionType, ConstraintValidatorContext context) {
    
    if (questionType == null) {
      return true;
    }

    if (Strings.isNullOrEmpty(questionType.getDe()) 
        && Strings.isNullOrEmpty(questionType.getEn())) {
      return true;
    }

    // check for scale levels
    return QuestionTypes.ALL.contains(questionType);
  }

}
