package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.CustomDataPackage;

/**
 * Validation of access ways of {@link CustomDataPackage}s.
 */
public class ValidCustomDataPackageAccesWayValidator
    implements ConstraintValidator<ValidCustomDataPackageAccessWay, String> {
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidCustomDataPackageAccessWay constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String accessWay, ConstraintValidatorContext context) {
    if (!StringUtils.hasLength(accessWay)) {
      return true;
    }
    return CustomDataPackage.AVAILABLE_ACCESS_WAYS.contains(accessWay);
  }

}
