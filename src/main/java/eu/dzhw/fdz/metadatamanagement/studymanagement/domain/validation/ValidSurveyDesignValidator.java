package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDesigns;

/**
 * Checks the surveydesign field for valid values.
 * 
 * @author Daniel Katzberg
 */
public class ValidSurveyDesignValidator implements 
    ConstraintValidator<ValidSurveyDesign, I18nString> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidSurveyDesign constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString surveyDesign, ConstraintValidatorContext context) {
    
    if (surveyDesign == null) {
      return true;
    }

    if (Strings.isNullOrEmpty(surveyDesign.getDe())
        && Strings.isNullOrEmpty(surveyDesign.getEn())) {
      return true;
    }

    // check for scale levels
    return SurveyDesigns.ALL.contains(surveyDesign);
  }

  
}
