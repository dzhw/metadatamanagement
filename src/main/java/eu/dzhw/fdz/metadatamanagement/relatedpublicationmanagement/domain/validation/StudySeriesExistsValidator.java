package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is a study with the given study series.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class StudySeriesExistsValidator 
    implements ConstraintValidator<StudySeriesExists, I18nString> {

  private final StudyRepository studyRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(StudySeriesExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString studySeries, ConstraintValidatorContext context) {   
    return studyRepository.existsByStudySeries(studySeries);
  }
}
