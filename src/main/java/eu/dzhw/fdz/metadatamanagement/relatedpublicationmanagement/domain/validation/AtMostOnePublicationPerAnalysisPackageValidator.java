package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that there is at most one publication per analysis package.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class AtMostOnePublicationPerAnalysisPackageValidator
    implements ConstraintValidator<AtMostOnePublicationPerAnalysisPackage, RelatedPublication> {

  private final RelatedPublicationRepository relatedPublicationRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(AtMostOnePublicationPerAnalysisPackage constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication,
      ConstraintValidatorContext context) {
    if (relatedPublication.getAnalysisPackageIds() == null
        || relatedPublication.getAnalysisPackageIds().isEmpty()) {
      return true;
    }
    for (String analysisPackageId : relatedPublication.getAnalysisPackageIds()) {
      List<IdAndVersionProjection> existingPublications =
          relatedPublicationRepository.findIdsByAnalysisPackageIdsContaining(analysisPackageId);
      for (IdAndVersionProjection existingPublication : existingPublications) {
        if (!existingPublication.getId().equals(relatedPublication.getId())) {
          return false;
        }
      }
    }   
    return true;
  }
}
