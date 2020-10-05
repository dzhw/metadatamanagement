package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackageAttachmentTypes;

/**
 * Validates the type of an {@link DataPackageAttachmentMetadata}.
 */
public class ValidDataPackageAttachmentTypeValidator
    implements ConstraintValidator<ValidDataPackageAttachmentType, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataPackageAttachmentType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString type, ConstraintValidatorContext context) {
    return DataPackageAttachmentTypes.ALL.contains(type);
  }

}
