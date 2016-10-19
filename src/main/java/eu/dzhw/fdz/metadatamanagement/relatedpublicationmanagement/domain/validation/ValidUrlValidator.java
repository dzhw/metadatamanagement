package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.util.StringUtils;


/**
 * Validates the name of a id.
 */
public class ValidUrlValidator
    implements ConstraintValidator<ValidUrl, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidUrl constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String url, ConstraintValidatorContext context) {
    if (!StringUtils.isEmpty(url)) {
      UrlValidator urlValidator = new UrlValidator();
      if (!urlValidator.isValid(url)) {
        return false;
      }     
    }
    return true;
  }

}
