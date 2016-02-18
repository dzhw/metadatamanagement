package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.repository.DataAcquisitionProjectRepository;

/**
 * Validate that a project with the given id exists.
 * 
 * @author René Reitmann
 */
public class DataAcquisitionProjectExistsValidator
    implements ConstraintValidator<DataAcquisitionProjectExists, String> {

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(DataAcquisitionProjectExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String dataAcquisitionProjectId, ConstraintValidatorContext context) {

    // if project id is mandatory use NotEmpty
    if (StringUtils.isEmpty(dataAcquisitionProjectId)) {
      return true;
    }

    // Name is set -> validate
    return this.dataAcquisitionProjectRepository.exists(dataAcquisitionProjectId);
  }

}
