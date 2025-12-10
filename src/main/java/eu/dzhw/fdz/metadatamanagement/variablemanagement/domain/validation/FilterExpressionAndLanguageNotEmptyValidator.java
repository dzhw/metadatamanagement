package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.FilterDetails;

/**
 * Ensure that either both of filterDetails.expressionLanguage and filterDetails.expression are set
 * or none of them.
 * 
 * @author René Reitmann
 *
 */
public class FilterExpressionAndLanguageNotEmptyValidator
    implements ConstraintValidator<FilterExpressionAndLanguageNotEmpty, FilterDetails> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(FilterExpressionAndLanguageNotEmpty constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(FilterDetails filterDetails, ConstraintValidatorContext context) {
    
    //check for generation details
    if (filterDetails == null) {
      return true;
    }
    
    // checks if both are present
    if (!Strings.isNullOrEmpty(filterDetails.getExpression())
        && !Strings.isNullOrEmpty(filterDetails.getExpressionLanguage())) {
      return true;
    }
    
    // checks if none is present
    if (Strings.isNullOrEmpty(filterDetails.getExpression())
        && Strings.isNullOrEmpty(filterDetails.getExpressionLanguage())) {
      return true;
    }
      
    return false;
  }
}
