package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;

/**
 * Validator which ensures that there is a study with the given id.
 * 
 * @author René Reitmann
 */
public class StudyExistsValidator implements ConstraintValidator<StudyExists, String> {

  @Autowired
  private StudyRepository studyRepository;
  
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
