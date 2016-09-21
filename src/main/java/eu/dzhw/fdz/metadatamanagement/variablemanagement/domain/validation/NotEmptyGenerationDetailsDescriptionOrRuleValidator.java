package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.GenerationDetails;

/**
 * Validates the rule or description field of the generation details. Checks for the not emptyness
 * of these fields. One of the fields have to be set. Both is acceptable too.
 * 
 * @author Daniel Katzberg
 *
 */
public class NotEmptyGenerationDetailsDescriptionOrRuleValidator
    implements ConstraintValidator<NotEmptyGenerationDetailsDescriptionOrRule, GenerationDetails> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(NotEmptyGenerationDetailsDescriptionOrRule constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(GenerationDetails generationDetails, ConstraintValidatorContext context) {
    
    //check for generation details
    if (generationDetails == null) {
      return true;
    }
    
    //checks for description
    if (generationDetails.getDescription() != null
        && (!Strings.isNullOrEmpty(generationDetails.getDescription().getDe())
            || !Strings.isNullOrEmpty(generationDetails.getDescription().getEn()))) {
      return true;
    }
    
    // checks for rule
    if (!Strings.isNullOrEmpty(generationDetails.getRule())) {
      return true;
    }
      
    // Case both are okay: No special check is needed.

    // Non of them is set ->
    return false;
  }
}
