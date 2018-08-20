package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * Validator which ensures that there is a study with the given study series.
 * 
 * @author René Reitmann
 */
public class StudySeriesExistsValidator 
    implements ConstraintValidator<StudySeriesExists, I18nString> {

  @Autowired
  private StudyRepository studyRepository;
  
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
