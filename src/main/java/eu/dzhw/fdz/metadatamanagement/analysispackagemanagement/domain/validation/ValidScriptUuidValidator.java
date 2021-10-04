package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ScriptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;

/**
 * Ensure that the referenced script exists.
 */
public class ValidScriptUuidValidator
    implements ConstraintValidator<ValidScriptUuid, ScriptAttachmentMetadata> {
  @Autowired
  private AnalysisPackageRepository analysisPackageRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidScriptUuid constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(ScriptAttachmentMetadata attachmentMetadata,
      ConstraintValidatorContext context) {
    if (!StringUtils.hasText(attachmentMetadata.getScriptUuid())) {
      return true;
    }
    AnalysisPackage analysisPackage =
        analysisPackageRepository.findById(attachmentMetadata.getAnalysisPackageId()).orElse(null);

    if (analysisPackage == null) {
      return false;
    }

    return analysisPackage.getScripts().stream()
        .anyMatch(script -> script.getUuid().equals(attachmentMetadata.getScriptUuid()));
  }

}
