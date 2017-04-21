package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.SubDataSet;

/**
 * This annotation is for the validator, which checks the accesways of subdataset within a project. 
 * The sub data set accessways has to be unique within the data set.
 *
 * @author Daniel Katzberg
 *
 */
public class UniqueSubDataSetAccessWayInDataSetValidator
    implements ConstraintValidator<UniqueSubDatasetAccessWayInDataSet, List<SubDataSet>> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueSubDatasetAccessWayInDataSet constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(List<SubDataSet> subDataSetList, ConstraintValidatorContext context) {
    if (subDataSetList == null) {
      // will be handled by not empty validator
      return true;
    }
    
    // ignore the same object (for an update)
    List<String> foundAccessWays = new ArrayList<>();
    for (SubDataSet subDataSetFromList : subDataSetList) {
      
      //found and double entry
      if (foundAccessWays.contains(subDataSetFromList.getAccessWay())) {
        return false;
      } else {
        foundAccessWays.add(subDataSetFromList.getAccessWay());
      }
      
    }
    
    // if no double found -> good!
    return true;
  }

}
