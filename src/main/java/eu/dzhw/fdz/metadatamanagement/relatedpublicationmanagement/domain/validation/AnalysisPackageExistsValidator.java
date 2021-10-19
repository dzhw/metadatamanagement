package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is an analysis package with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class AnalysisPackageExistsValidator
    implements ConstraintValidator<AnalysisPackageExists, String> {

  private final AnalysisPackageRepository analysisPackageRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(AnalysisPackageExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String analysisPackageId, ConstraintValidatorContext context) {
    return analysisPackageRepository.existsById(analysisPackageId);
  }
}
