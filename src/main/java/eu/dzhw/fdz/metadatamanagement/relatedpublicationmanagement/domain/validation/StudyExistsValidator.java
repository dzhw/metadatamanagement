package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is a study with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class StudyExistsValidator implements ConstraintValidator<StudyExists, String> {

  private final StudyRepository studyRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(StudyExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String studyId, ConstraintValidatorContext context) {   
    return studyRepository.existsById(studyId);
  }
}
