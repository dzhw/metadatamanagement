package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Ensure that a related publication id is valid.
 * 
 * @author Daniel Katzberg
 */
public class ValidRelatedPublicationIdValidator implements 
    ConstraintValidator<ValidRelatedPublicationId, RelatedPublication> {

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidRelatedPublicationId constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication, 
      ConstraintValidatorContext context) {
    
    return relatedPublication.getId().matches("^pub\\-.*\\$$");
  }
}
