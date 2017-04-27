package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;

/**
 * Ensure that there is enough information to display a histogram
 * for ratio variables.
 */
public class SetHasBeenReleasedBeforeOnlyOnceValidator
    implements ConstraintValidator<SetHasBeenReleasedBeforeOnlyOnce, DataAcquisitionProject> {

  @Autowired
  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;
  
  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(SetHasBeenReleasedBeforeOnlyOnce constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(
   * java.lang.Object, javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DataAcquisitionProject value, ConstraintValidatorContext context) {
    
    //No id, no validation
    if (value.getId() == null) {
      return true;
    }
    
    DataAcquisitionProject oldProjectState = 
        this.dataAcquisitionProjectRepository.findOne(value.getId());

    //No old version. Project will be saved the first time
    if (oldProjectState == null) {
      return true;
    }
    
    //it is not allowed to set the has been released before to false, if it was true before
    if (oldProjectState.getHasBeenReleasedBefore() && !value.getHasBeenReleasedBefore()) {
      return false;
    }
    
    //all other cases is true
    return true;
  } 
}
