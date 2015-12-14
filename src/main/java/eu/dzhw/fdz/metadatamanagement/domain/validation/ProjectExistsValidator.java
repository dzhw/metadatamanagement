package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;

/**
 * Validates the name of a fdz project name, if it exists.
 * 
 * @author Daniel Katzberg
 *
 */
public class ProjectExistsValidator implements ConstraintValidator<ProjectExists, String> {

  @Autowired
  private FdzProjectRepository fdzProjectRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ProjectExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String fdzProjectName, ConstraintValidatorContext context) {
    
    // No ProjectName, no validation.
    if (fdzProjectName == null || fdzProjectName.trim()
        .length() == 0) {
      return true;
    }

    // Name is set -> validate
    if (this.fdzProjectRepository.exists(fdzProjectName)) {
      return true;
    } else {
      return false;
    }
  }

}
