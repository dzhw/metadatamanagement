package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.elasticsearch.common.lang3.StringUtils;
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
    if (StringUtils.isEmpty(fdzProjectName)) {
      return false;
    }

    // Name is set -> validate
    return this.fdzProjectRepository.exists(fdzProjectName);
  }

}
