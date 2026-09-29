package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;


/**
 * Checks year.
 */
public class ValidPublicationYearValidator
    implements ConstraintValidator<ValidPublicationYear, RelatedPublication> {

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidPublicationYear constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication, 
      ConstraintValidatorContext context) {
    if (relatedPublication.getYear() == null) {
      return true;
    }
    return relatedPublication.getYear() > 1960;
  }

}
