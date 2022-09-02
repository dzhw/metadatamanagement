package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;


/**
 * Checks year.
 */
public class ValidPublicationYearValidator
    implements ConstraintValidator<ValidPublicationYear, RelatedPublication> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidPublicationYear constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication, 
      ConstraintValidatorContext context) {
    if (relatedPublication.getYear() == null) {
      return true;
    }
    LocalDate date = LocalDate.now();
    return date.getYear() >= relatedPublication.getYear() 
        && relatedPublication.getYear() > 1960;
  }

}
