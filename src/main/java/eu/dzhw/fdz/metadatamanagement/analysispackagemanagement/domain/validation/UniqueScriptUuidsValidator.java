package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.Script;

/**
 * Ensure that all {@link Script} uuids are unique within the {@link AnalysisPackage}.
 * 
 * @author René Reitmann
 */
public class UniqueScriptUuidsValidator
    implements ConstraintValidator<UniqueScriptUuids, AnalysisPackage> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueScriptUuids constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(AnalysisPackage analysisPackage, ConstraintValidatorContext context) {
    if (analysisPackage.getScripts() == null || analysisPackage.getScripts().isEmpty()) {
      return true;
    }
    List<String> uniqueUuids = analysisPackage.getScripts().stream().map(Script::getUuid).distinct()
        .collect(Collectors.toList());
    return uniqueUuids.size() == analysisPackage.getScripts().size();
  }
}
