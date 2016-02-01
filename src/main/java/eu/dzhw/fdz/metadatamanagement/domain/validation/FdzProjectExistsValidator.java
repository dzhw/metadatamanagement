package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.repository.FdzProjectRepository;

/**
 * Validate that a project with the given id exists.
 * 
 * @author René Reitmann
 */
public class FdzProjectExistsValidator implements ConstraintValidator<FdzProjectExists, String> {

  @Autowired
  private FdzProjectRepository fdzProjectRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(FdzProjectExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String fdzProjectId, ConstraintValidatorContext context) {
    
    // if project id is mandatory use NotEmpty
    if (StringUtils.isEmpty(fdzProjectId)) {
      return true;
    }

    // Name is set -> validate
    return this.fdzProjectRepository.exists(fdzProjectId);
  }

}
