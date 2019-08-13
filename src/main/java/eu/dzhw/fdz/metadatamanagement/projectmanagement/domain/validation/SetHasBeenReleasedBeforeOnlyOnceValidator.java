package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that there is enough information to display a histogram
 * for ratio variables.
 */
@RequiredArgsConstructor
public class SetHasBeenReleasedBeforeOnlyOnceValidator
    implements ConstraintValidator<SetHasBeenReleasedBeforeOnlyOnce, DataAcquisitionProject> {

  private final DataAcquisitionProjectRepository dataAcquisitionProjectRepository;
  
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
        this.dataAcquisitionProjectRepository.findById(value.getId())
        .orElse(null);

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
